package com.bearxyz.controller;


import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.*;
import com.bearxyz.domain.po.business.Package;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.repository.*;
import com.bearxyz.service.business.GoodsService;
import com.bearxyz.service.business.OfficialPartnerService;
import com.bearxyz.service.business.StockService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.List;

@Controller
@RequestMapping("/stock")
@SessionAttributes("stock")
@Transactional
public class StockController {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private OfficialPartnerService officialPartnerService;

    @Autowired
    private PurchasingOrderItemRepository purchasingOrderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private StockService service;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private PackageRepository packageRepository;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/stock/index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @ResponseBody
    public String getIndex(@RequestBody PaginationCriteria req) throws JsonProcessingException {
        DataTable<Goods> goods = goodsService.getGoods();
        goods.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(goods);
    }

    @RequestMapping(value = "/list/{type}", method = RequestMethod.GET)
    public String list(@PathVariable("type") String type, Model model) {
        String title = "";
        if (type.equals("STOCK-IN")) title = "入库";
        if (type.equals("STOCK-OUT")) title = "出库";
        model.addAttribute("type", type);
        model.addAttribute("title", title);
        return "/stock/list";
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public String getList(@RequestBody PaginationCriteria req, @RequestParam("type") String type) throws JsonProcessingException {
        Subject u = SecurityUtils.getSubject();
        DataTable<Stock> stocks = new DataTable<>();
        if (type.equals("STOCK-IN")||(type.equals("STOCK-OUT") && u.hasRole("ROLE_ADMINISTRATION_MANAGER")))
            stocks = service.getStocks(type, false, req);
        else{
            List<Stock> result = stockRepository.findStockOutByUserId(((User)u.getPrincipal()).getId());
            stocks.setData(result);
            stocks.setRecordsFiltered((long)result.size());
            stocks.setRecordsTotal((long)result.size());
        }
        stocks.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(stocks);
    }

    @ResponseBody
    @RequestMapping(value = "/getItems", method = RequestMethod.POST)
    public String getItems(@RequestParam("id") String id) throws JsonProcessingException {
        List<StockItem> results = service.getItemsByStockId(id);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(results);
    }

    @RequestMapping(value = "/purchasing-in-list", method = RequestMethod.GET)
    public String purchasingInList() {
        return "/stock/purchasing-in-list";
    }

    @RequestMapping(value = "/purchasing-in-list", method = RequestMethod.POST)
    @ResponseBody
    public String getPurchasingInList(@RequestBody PaginationCriteria req) throws JsonProcessingException {
        DataTable<PurchasingOrderItem> purchasing = service.getPurchasingOrderItems(true, req);
        purchasing.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(purchasing);
    }


    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String detail() {
        return "/stock/detail";
    }

    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ResponseBody
    public String getDetail(@RequestBody PaginationCriteria req) throws JsonProcessingException {
        DataTable<StockItem> purchasing = service.getStockItems(false);
        purchasing.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(purchasing);
    }

    @RequestMapping(value = "/applyIn", method = RequestMethod.GET)
    public String applyIn(@RequestParam("ids") String ids, Model model) {
        List<PurchasingOrderItem> list = service.getPurchasingOrderItemsByIds(ids);
        PurchasingOrder order = list.get(0).getOrder();
        model.addAttribute("oid", order.getId());
        model.addAttribute("list", list);
        model.addAttribute("stock", new Stock());
        return "/stock/applyIn";
    }

    @RequestMapping(value = "/applyIn", method = RequestMethod.POST)
    @ResponseBody
    public String doApplyIn(@ModelAttribute(name = "stock") Stock stock, SessionStatus status) {
        stock.setMask("STOCK_IN_PURCHASING");
        service.applyLoad(stock);
        status.setComplete();
        return "{success: true}";
    }

    @RequestMapping(value = "/applyOut", method = RequestMethod.GET)
    public String applyOut() {
        return "/stock/applyOut";
    }

    @RequestMapping(value = "/selectTransport/{id}", method = RequestMethod.GET)
    public String selectTransport(@PathVariable("id") String id, Model model) {
        List<OfficialPartner> officialPartners = officialPartnerService.getListByType("LOGISTICS_COMPANY");
        List<Warehouse> warehouses = warehouseRepository.findAll();
        Stock stock = service.getStockById(id);
        model.addAttribute("stock", stock);
        model.addAttribute("transport", officialPartners);
        model.addAttribute("warehouses", warehouses);
        return "/stock/selectTransport";
    }

    @RequestMapping(value = "/selectTransport", method = RequestMethod.POST)
    @ResponseBody
    public String doSelectTransport(@ModelAttribute(name = "stock") Stock stock, SessionStatus status) {
        stock.setApproved(true);
        if (stock.getOrderId() != null) {
            Order order = orderRepository.findOne(stock.getOrderId());
            if (order != null) {
                order.setStatus(3);
                orderRepository.save(order);
            }
        }
        for (StockItem item : stock.getItems()) {
            Goods goods = goodsRepository.findOne(item.getGoodsId());
            Package pak = packageRepository.findOne(item.getPackageId());
            goods.setStock(goods.getStock()-pak.getAmmount()*item.getCount());
            item.setApproved(true);
            goodsRepository.save(goods);
        }
        service.save(stock);
        status.setComplete();
        return "{success: true}";
    }

    @RequestMapping(value = "/print/{id}", method = RequestMethod.GET)
    public String print(@PathVariable("id") String id, Model model) {
        Stock stock = service.getStockById(id);
        model.addAttribute("stock", stock);
        return "/stock/print";
    }

    @RequestMapping(value = "/complete")
    public String complete(@RequestParam("bid") String bid, @RequestParam("tid") String tid, @RequestParam("applyer") String applyer, Model model) {
        Stock order = service.getStockById(bid);
        Float totalPrice = (float) 0.0;
        String pid = "";
        for (StockItem item : order.getItems()) {
            totalPrice += item.getPrice() * item.getCount();
            Goods goods = goodsService.getById(item.getGoodsId());
            item.setGoods(goods);
            pid = item.getPurchasingOrderItemId();
        }
        PurchasingOrderItem poi = purchasingOrderItemRepository.findOne(pid);
        model.addAttribute("advance", poi.getOrder().getAdvance());
        model.addAttribute("stock", order);
        model.addAttribute("totalPrice", totalPrice);
        String memo = "";
        model.addAttribute("applyer", applyer);
        Task task = taskService.createTaskQuery().taskId(tid).singleResult();
        if (!task.getTaskDefinitionKey().equals("deptLeader") && taskService.getVariable(task.getId(), "deptLeaderMemo") != null)
            memo = taskService.getVariable(task.getId(), "deptLeaderMemo").toString();
        model.addAttribute("taskId", tid);
        model.addAttribute("taskKey", task.getTaskDefinitionKey());
        model.addAttribute("memo", memo);
        return "/stock/complete";
    }

}
