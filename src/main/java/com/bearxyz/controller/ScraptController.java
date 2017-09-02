package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.Goods;
import com.bearxyz.domain.po.business.Scrapt;
import com.bearxyz.domain.po.business.ScraptItem;
import com.bearxyz.service.business.GoodsService;
import com.bearxyz.service.business.ScraptService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

/**
 * Created by bearxyz on 2017/8/28.
 */
@Controller
@RequestMapping("/scrapt")
@SessionAttributes("scrapt")
public class ScraptController {

    @Autowired
    private ScraptService service;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/scrapt/index";
    }

    @ResponseBody
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public String getList(@RequestBody PaginationCriteria req) throws JsonProcessingException {
        DataTable<ScraptItem> scrapt = service.getScraptItems(false, req);
        scrapt.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(scrapt);
    }

    @RequestMapping(value = "/apply", method = RequestMethod.GET)
    public String apply(Model model) {
        model.addAttribute("scrapt", new Scrapt());
        return "/scrapt/apply";
    }

    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    @ResponseBody
    public String doApply(@ModelAttribute("scrapt")Scrapt scrapt, SessionStatus status) {
        service.apply(scrapt);
        status.setComplete();
        return "{success: true}";
    }

    @RequestMapping(value = "/reApply/{id}", method = RequestMethod.GET)
    public String reApply(@PathVariable("id") String id, Model model) {
        Scrapt scrapt = service.getById(id);
        for(ScraptItem item: scrapt.getItems()){
            Goods goods = goodsService.getById(item.getGoodsId());
            item.setGoods(goods);
        }
        Task task = taskService.createTaskQuery().processInstanceBusinessKey(id).singleResult();
        String deptMemo = "";
        String managerMemo = "";
        if (taskService.getVariable(task.getId(), "deptLeaderMemo") != null)
            deptMemo = taskService.getVariable(task.getId(), "deptLeaderMemo").toString();
        if (taskService.getVariable(task.getId(), "managerMemo") != null)
            managerMemo = taskService.getVariable(task.getId(), "managerMemo").toString();
        model.addAttribute("deptMemo", deptMemo);
        model.addAttribute("managerMemo", managerMemo);
        model.addAttribute("scrapt", scrapt);
        model.addAttribute("taskId", task.getId());
        return "/scrapt/reApply";
    }

    @RequestMapping(value = "/reApply", method = RequestMethod.POST)
    @ResponseBody
    public String doReApply(@ModelAttribute("scrapt")Scrapt scrapt, SessionStatus status) {
        service.save(scrapt);
        status.setComplete();
        return "{success: true}";
    }

    @RequestMapping(value = "/complete")
    public String complete(@RequestParam("bid") String bid, @RequestParam("tid") String tid, @RequestParam("applyer") String applyer, Model model) {
        Scrapt scrapt = service.getById(bid);
        for(ScraptItem item: scrapt.getItems()){
            Goods goods = goodsService.getById(item.getGoodsId());
            item.setGoods(goods);
        }
        model.addAttribute("scrapt", scrapt);
        String memo = "";
        model.addAttribute("applyer", applyer);
        Task task = taskService.createTaskQuery().taskId(tid).singleResult();
        if (!task.getTaskDefinitionKey().equals("deptLeader")&&taskService.getVariable(task.getId(), "deptLeaderMemo") != null)
            memo = taskService.getVariable(task.getId(), "deptLeaderMemo").toString();
        model.addAttribute("taskId", tid);
        model.addAttribute("taskKey", task.getTaskDefinitionKey());
        model.addAttribute("memo", memo);
        return "/scrapt/complete";
    }

}
