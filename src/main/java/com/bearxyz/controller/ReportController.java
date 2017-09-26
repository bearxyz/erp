package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.domain.po.business.*;
import com.bearxyz.domain.po.sys.Dict;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.mapper.PurchasingOrderItemMapper;
import com.bearxyz.mapper.StockMapper;
import com.bearxyz.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private PurchasingOrderItemRepository purchasingOrderItemRepository;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private DictRepository dictRepository;
    @Autowired
    private OfficialPartnerRepository officialPartnerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PurchasingOrderItemMapper purchasingOrderItemMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private StockItemRepository stockItemRepository;

    @RequestMapping(value = "/purchasing", method = RequestMethod.GET)
    public String purchasingReport() {
        return "/report/purchasing";
    }

    @RequestMapping(value = "/purchasing", method = RequestMethod.POST)
    @ResponseBody
    public String purchasingReportList(Integer draw, String startDate, String endDate, String project, String type, String name) throws JsonProcessingException {
        DataTable<PurchasingOrderItem> result = new DataTable<>();
        startDate = (startDate == null) ? "1970-1-1" : startDate;
        endDate = (endDate == null) ? "1970-1-1" : endDate;
        project = (project == null) ? "" : project;
        type = (type == null) ? "" : type;
        name = (name == null) ? "" : name;
        List<PurchasingOrderItem> list = purchasingOrderItemMapper.reportPurchasingOrderItem(startDate, endDate, project, type, name);
        Integer totalCount = 0;
        Float totalPrice = (float) 0.0;
        for (PurchasingOrderItem item : list) {
            Goods goods = goodsRepository.findOne(item.getGoodsId());
            if (goods.getProject() != null) {
                Dict proj = dictRepository.findByMask(goods.getProject());
                goods.setProjectName(proj.getName());
            }
            if (goods.getType() != null) {
                Dict tp = dictRepository.findByMask(goods.getType());
                goods.setTypeName(tp.getName());
            }
            item.setGoods(goods);
            if (item.getSupplier() != null) {
                OfficialPartner partner = officialPartnerRepository.findOne(item.getSupplier());
                item.setSupplierName(partner.getName());
            }
            PurchasingOrderItem i = purchasingOrderItemRepository.findOne(item.getId());
            item.setOrder(i.getOrder());
            if (item.getOrder().getOperator() != null) {
                User user = userRepository.findOne(item.getOrder().getOperator());
                item.getOrder().setApplyer(user.getFirstName() + user.getLastName());
            }
            totalCount += item.getCount();
            totalPrice += item.getPrice();
        }
        PurchasingOrderItem total = new PurchasingOrderItem();
        total.setCount(totalCount);
        total.setPrice(totalPrice);
        PurchasingOrder o = new PurchasingOrder();
        o.setCode("合计：");
        Goods goods = new Goods();
        total.setGoods(goods);
        total.setOrder(o);
        list.add(total);
        result.setDraw(draw);
        result.setRecordsFiltered((long) list.size());
        result.setRecordsTotal((long) list.size());
        result.setData(list);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(result);
    }

    @RequestMapping(value = "/stockin", method = RequestMethod.GET)
    public String stockin() {
        return "/report/stockin";
    }

    @RequestMapping(value = "/stockin", method = RequestMethod.POST)
    @ResponseBody
    public String stockinReport(Integer draw, String startDate, String endDate, String project, String type, String name) throws JsonProcessingException {
        DataTable<StockItem> result = new DataTable<>();
        startDate = (startDate == null) ? "1970-1-1" : startDate;
        endDate = (endDate == null) ? "1970-1-1" : endDate;
        project = (project == null) ? "" : project;
        type = (type == null) ? "" : type;
        name = (name == null) ? "" : name;
        List<StockItem> list = stockMapper.reportStockIn(startDate, endDate, project, type, name);
        Integer totalCount = 0;
        Float totalPrice = (float) 0.0;
        Integer orderCount = 0;
        for (StockItem item : list) {
            Goods goods = goodsRepository.findOne(item.getGoodsId());
            if (goods.getProject() != null) {
                Dict proj = dictRepository.findByMask(goods.getProject());
                goods.setProjectName(proj.getName());
            }
            if (goods.getType() != null) {
                Dict tp = dictRepository.findByMask(goods.getType());
                goods.setTypeName(tp.getName());
            }
            item.setGoods(goods);
            if (item.getSupplier() != null) {
                OfficialPartner partner = officialPartnerRepository.findOne(item.getSupplier());
                if(partner!=null)
                    item.setSupplierName(partner.getName());
            }
            if(item.getPurchasingOrderItemId()!=null){
                PurchasingOrderItem orderItem = purchasingOrderItemRepository.findOne(item.getPurchasingOrderItemId());
                item.setOrderItem(orderItem);
                orderCount+=orderItem.getCount();
            }else
            {
                item.setOrderItem(new PurchasingOrderItem());
            }
            StockItem i = stockItemRepository.findOne(item.getId());
            item.setStock(i.getStock());
            User user = userRepository.findOne(item.getStock().getCreatedBy());
            item.getStock().setOperator(user.getFirstName() + user.getLastName());
            totalCount += item.getCount();
            totalPrice += item.getPrice();
        }
        StockItem total = new StockItem();
        total.setCount(totalCount);
        total.setPrice(totalPrice);
        Stock o = new Stock();
        o.setCode("合计：");
        Goods goods = new Goods();
        total.setGoods(goods);
        PurchasingOrderItem orderItem = new PurchasingOrderItem();
        orderItem.setCount(orderCount);
        total.setOrderItem(orderItem);
        total.setStock(o);
        list.add(total);
        result.setDraw(draw);
        result.setRecordsFiltered((long) list.size());
        result.setRecordsTotal((long) list.size());
        result.setData(list);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(result);

    }
}
