package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.Goods;
import com.bearxyz.domain.po.business.Sale;
import com.bearxyz.domain.po.business.SaleItem;
import com.bearxyz.service.business.GoodsService;
import com.bearxyz.service.business.SaleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
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

    @ResponseBody
    @RequestMapping(value = "/getItems", method = RequestMethod.POST)
    public String getItems(@RequestParam("id")String id) throws JsonProcessingException{
        List<SaleItem> results = service.getItemsBySaleId(id);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(results);
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
        for (SaleItem item : sale.getItems()) {
            Goods goods = goodsService.getById(item.getGoodsId());
            item.setGoods(goods);
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
        for (SaleItem item : sale.getItems()) {
            Goods goods = goodsService.getById(item.getGoodsId());
            item.setGoods(goods);
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

}
