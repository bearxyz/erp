package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.Goods;
import com.bearxyz.domain.po.business.GroupBuy;
import com.bearxyz.domain.po.business.Package;
import com.bearxyz.domain.po.business.Sale;
import com.bearxyz.repository.GroupBuyRepository;
import com.bearxyz.repository.PackageRepository;
import com.bearxyz.repository.SaleRepository;
import com.bearxyz.service.business.GoodsService;
import com.bearxyz.service.business.SaleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * Created by bearxyz on 2017/8/30.
 */
@Controller
@RequestMapping("/sale")
@SessionAttributes("sale")
public class SaleController {

    @Autowired
    private SaleService service;

    @Autowired
    private TaskService taskService;

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private GroupBuyRepository groupBuyRepository;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(){
        return "/sale/list";
    }

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public String getList(@RequestBody PaginationCriteria req) throws JsonProcessingException {
        DataTable<Sale> foruses = service.getSales(true, null);
        foruses.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(foruses);
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(){
        return "/sale/index";
    }

    @ResponseBody
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public String getIndex(@RequestBody PaginationCriteria req) throws JsonProcessingException {
        DataTable<Sale> foruses = service.getSales(false, null);
        foruses.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(foruses);
    }

    @RequestMapping(value = "/apply", method = RequestMethod.GET)
    public String apply(Model model) {
        model.addAttribute("sale", new Sale());
        return "/sale/apply";
    }

    @ResponseBody
    @RequestMapping(value = {"/apply"}, method = RequestMethod.POST)
    public String doApply(@ModelAttribute("sale")Sale sale, SessionStatus status, HttpServletRequest request) throws IOException {
        List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("attachment");
        service.apply(sale, null,files);
        status.setComplete();
        return "{success: true}";
    }

    @RequestMapping(value = "/reApply/{id}", method = RequestMethod.GET)
    public String reApply(@PathVariable("id")String id, Model model) {
        Task task = taskService.createTaskQuery().processInstanceBusinessKey(id).singleResult();
        Sale sale = service.getById(id);
        if(sale.getPackageId()!=null) {
            Package pk = packageRepository.findOne(sale.getPackageId());
            if(pk!=null)
                sale.setaPackage(pk);
        }
        if(sale.getGoodsId()!=null) {
            Goods goods = goodsService.getById(sale.getGoodsId());
            if(goods!=null)
                sale.setGoods(goods);
        }
        model.addAttribute("sale", sale);
        model.addAttribute("taskId", task.getId());
        model.addAttribute("taskKey", task.getTaskDefinitionKey());
        return "/sale/reApply";
    }

    @RequestMapping(value = "/reApply", method = RequestMethod.POST)
    @ResponseBody
    public String doReApply(@ModelAttribute("sale")Sale sale, SessionStatus status, HttpServletRequest request) throws IOException {
        List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("attachment");
        List<MultipartFile> pic = ((MultipartHttpServletRequest)request).getFiles("picture");
        service.save(sale, pic, files);
        status.setComplete();
        return "{success: true}";
    }

    @ResponseBody
    @RequestMapping(value = {"/setStatus"}, method = RequestMethod.POST)
    public String setStatus(@RequestParam("id") String id){
        service.setStatus(id);
        return "{success: true}";
    }

    @RequestMapping(value = "/complete", method = RequestMethod.GET)
    public String complete(@RequestParam("bid") String bid, @RequestParam("tid") String tid, @RequestParam("applyer") String applyer, Model model) {
        Sale sale = service.getById(bid);
        if(sale.getPackageId()!=null) {
            Package pk = packageRepository.findOne(sale.getPackageId());
            if(pk!=null)
                sale.setaPackage(pk);
        }
        if(sale.getGoodsId()!=null) {
            Goods goods = goodsService.getById(sale.getGoodsId());
            if(goods!=null)
                sale.setGoods(goods);
        }
        model.addAttribute("sale", sale);
        String memo = "";
        model.addAttribute("applyer", applyer);
        Task task = taskService.createTaskQuery().taskId(tid).singleResult();
        if (!task.getTaskDefinitionKey().equals("deptLeader")&&taskService.getVariable(task.getId(), "deptLeaderMemo") != null)
            memo = taskService.getVariable(task.getId(), "deptLeaderMemo").toString();
        model.addAttribute("taskId", tid);
        model.addAttribute("taskKey", task.getTaskDefinitionKey());
        model.addAttribute("memo", memo);
        return "/sale/complete";
    }

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public String detail(@PathVariable("id")String id, Model model) {
        Sale sale =service.getById(id);
        model.addAttribute("sale",sale);
        return "/sale/detail";
    }

    @RequestMapping(value = "/getGoods")
    @ResponseBody
    public String getGoods(String project, String type, String subtype) throws JsonProcessingException {
        List<Goods> goods=goodsService.getGoodsByType(project, type, subtype);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(goods);
    }

    @RequestMapping(value = "/getPackage")
    @ResponseBody
    public String getPackage(String goodsId) throws JsonProcessingException {
        List<Package> goods=packageRepository.findAllByGoodsId(goodsId);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(goods);
    }

    @ResponseBody
    @RequestMapping(value = {"/setPublic"}, method = RequestMethod.POST)
    public String setPublic(@RequestParam("id") String id){
        Sale sale = saleRepository.findOne(id);
        sale.setPublic(!sale.getPublic());
        saleRepository.save(sale);
        return "{success: true}";
    }

    @ResponseBody
    @RequestMapping(value = {"/setCoupon"}, method = RequestMethod.POST)
    public String setCoupon(@RequestParam("id") String id){
        Sale sale = saleRepository.findOne(id);
        sale.setCanUseCoupon(!sale.getCanUseCoupon());
        saleRepository.save(sale);
        return "{success: true}";
    }

    @RequestMapping(value = "/groupbuy/{id}", method = RequestMethod.GET)
    public String groupBuy(@PathVariable("id")String id, Model model){
        Sale sale = saleRepository.findOne(id);
        model.addAttribute("sale", sale);
        return "/sale/groupbuy";
    }

    @RequestMapping(value = "/groupbuy/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String getGroupBuy(@PathVariable("id")String id,@RequestBody PaginationCriteria req) throws JsonProcessingException {
        List<GroupBuy> groupBuys = groupBuyRepository.findAllBySaleId(id);
        DataTable<GroupBuy> results = new DataTable<>();
        results.setData(groupBuys);
        results.setDraw(req.getDraw());
        results.setRecordsTotal((long)groupBuys.size());
        results.setRecordsFiltered((long)groupBuys.size());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(results);
    }

    @RequestMapping(value = "/createGroupbuy/{id}", method = RequestMethod.GET)
    public String createGroupbuy(@PathVariable("id")String id, Model model){
        Sale sale = saleRepository.findOne(id);
        model.addAttribute("sale", sale);
        return "/sale/createGroupbuy";
    }

    @RequestMapping(value = "/createGroupbuy", method = RequestMethod.POST)
    @ResponseBody
    public String doGroupBuy(String saleId, Integer manCount, Float price){
        GroupBuy groupBuy = new GroupBuy();
        groupBuy.setSaleId(saleId);
        groupBuy.setManCount(manCount);
        groupBuy.setDisCount(price);
        groupBuyRepository.save(groupBuy);
        return "{success: true}";
    }

    @ResponseBody
    @RequestMapping(value = {"/deleteGroupbuy"}, method = RequestMethod.POST)
    public String delete(@Param("ids") String[] ids) {
        for(String id: ids){
            groupBuyRepository.delete(id);
        }
        return "{success: true}";
    }

    @RequestMapping(value = "/company", method = RequestMethod.GET)
    public String company(@PathVariable("id")String id){

        return "/sale/company";
    }

}
