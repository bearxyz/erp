package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.Goods;
import com.bearxyz.domain.po.business.Purchasing;
import com.bearxyz.domain.po.business.PurchasingDetail;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.service.business.GoodsService;
import com.bearxyz.service.business.PurchasingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

/**
 * Created by bearxyz on 2017/8/16.
 */
@Controller
@RequestMapping("/purchasing")
@SessionAttributes("purchasing")
public class PurchasingController {

    @Autowired
    private PurchasingService service;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "/complete")
    public String complete(@RequestParam("bid") String bid, @RequestParam("tid") String tid, @RequestParam("applyer") String applyer, Model model) {
        Purchasing purchasing = service.getOneById(bid);
        for (PurchasingDetail item : purchasing.getItems()) {
            Goods goods = goodsService.getById(item.getGoodsId());
            item.setGoods(goods);
        }
        String memo = "";
        model.addAttribute("purchasing", purchasing);
        model.addAttribute("applyer", applyer);
        Task task = taskService.createTaskQuery().taskId(tid).singleResult();
        if (!task.getTaskDefinitionKey().equals("deptLeader")&&taskService.getVariable(task.getId(), "deptLeaderMemo") != null)
            memo = taskService.getVariable(task.getId(), "deptLeaderMemo").toString();
        model.addAttribute("taskId", tid);
        model.addAttribute("taskKey", task.getTaskDefinitionKey());
        model.addAttribute("memo", memo);
        return "/purchasing/complete";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/purchasing/index";
    }

    @ResponseBody
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public String getList(@RequestBody PaginationCriteria req) throws JsonProcessingException {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        DataTable<Purchasing> purchasing = service.getPurchasing(user.getId(), false, req);
        purchasing.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(purchasing);
    }

    @RequestMapping(value = "/apply", method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("purchasing", new Purchasing());
        return "/purchasing/apply";
    }

    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    @ResponseBody
    public String doCreate(@ModelAttribute("purchasing") Purchasing forUse, SessionStatus status) {
        service.apply(forUse);
        status.setComplete();
        return "{success: true}";
    }

    @RequestMapping(value = "/reApply/{id}", method = RequestMethod.GET)
    public String reApply(@PathVariable("id") String id, Model model) {
        Purchasing purchasing = service.getOneById(id);
        for (PurchasingDetail item : purchasing.getItems()) {
            Goods goods = goodsService.getById(item.getGoodsId());
            item.setGoods(goods);
        }
        String deptMemo = "";
        String managerMemo = "";
        Task task = taskService.createTaskQuery().processInstanceBusinessKey(id).singleResult();
        if (taskService.getVariable(task.getId(), "deptLeaderMemo") != null)
            deptMemo = taskService.getVariable(task.getId(), "deptLeaderMemo").toString();
        if (taskService.getVariable(task.getId(), "managerMemo") != null)
            managerMemo = taskService.getVariable(task.getId(), "managerMemo").toString();
        model.addAttribute("purchasing", purchasing);
        model.addAttribute("taskId", task.getId());
        model.addAttribute("deptMemo", deptMemo);
        model.addAttribute("managerMemo", managerMemo);
        return "/purchasing/reApply";
    }

    @RequestMapping(value = "/reApply", method = RequestMethod.POST)
    @ResponseBody
    public String doReApply(@ModelAttribute("purchasing") Purchasing purchasing, SessionStatus status) {
        service.save(purchasing);
        status.setComplete();
        return "{success: true}";
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public String delete(String id) {
        service.deleteItemById(id);
        return "{success: true}";
    }

    @RequestMapping(value = "/show/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") String id, Model model) {
        Purchasing purchasing = service.getOneById(id);
        for (PurchasingDetail item : purchasing.getItems()) {
            Goods goods = goodsService.getById(item.getGoodsId());
            item.setGoods(goods);
        }
        model.addAttribute("purchasing", purchasing);
        return "/purchasing/show";
    }

}
