package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.Goods;
import com.bearxyz.domain.po.business.Purchasing;
import com.bearxyz.domain.po.business.Replenishment;
import com.bearxyz.domain.po.business.ReplenishmentItem;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.service.business.GoodsService;
import com.bearxyz.service.business.ReplenishmentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

/**
 * Created by bearxyz on 2017/9/2.
 */
@Controller
@RequestMapping(name = "/replenishment")
@SessionAttributes("replenishment")
public class ReplenishmentController {

    @Autowired
    private ReplenishmentService service;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "/complete")
    public String complete(@RequestParam("bid") String bid, @RequestParam("tid") String tid, @RequestParam("applyer") String applyer, Model model) {
        Replenishment purchasing = service.getOneById(bid);
        for (ReplenishmentItem item : purchasing.getItems()) {
            Goods goods = goodsService.getById(item.getGoodsId());
            item.setGoods(goods);
        }
        String memo = "";
        model.addAttribute("replenishment", purchasing);
        model.addAttribute("applyer", applyer);
        Task task = taskService.createTaskQuery().taskId(tid).singleResult();
        if (!task.getTaskDefinitionKey().equals("deptLeader")&&taskService.getVariable(task.getId(), "deptLeaderMemo") != null)
            memo = taskService.getVariable(task.getId(), "deptLeaderMemo").toString();
        model.addAttribute("taskId", tid);
        model.addAttribute("taskKey", task.getTaskDefinitionKey());
        model.addAttribute("memo", memo);
        return "/replenishment/complete";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/replenishment/index";
    }

    @ResponseBody
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public String getList(@RequestBody PaginationCriteria req) throws JsonProcessingException {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        DataTable<Replenishment> purchasing = service.getReplenishment(user.getId(), null, req);
        purchasing.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(purchasing);
    }

    @RequestMapping(value = "/apply", method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("purchasing", new Purchasing());
        return "/replenishment/apply";
    }

    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    @ResponseBody
    public String doCreate(@ModelAttribute("replenishment") Replenishment replenishment, SessionStatus status) {
        service.apply(replenishment);
        status.setComplete();
        return "{success: true}";
    }

    @RequestMapping(value = "/reApply/{id}", method = RequestMethod.GET)
    public String reApply(@PathVariable("id") String id, Model model) {
        Replenishment replenishment = service.getOneById(id);
        for (ReplenishmentItem item : replenishment.getItems()) {
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
        model.addAttribute("replenishment", replenishment);
        model.addAttribute("taskId", task.getId());
        model.addAttribute("deptMemo", deptMemo);
        model.addAttribute("managerMemo", managerMemo);
        return "/replenishment/reApply";
    }

    @RequestMapping(value = "/reApply", method = RequestMethod.POST)
    @ResponseBody
    public String doReApply(@ModelAttribute("replenishment") Replenishment replenishment, SessionStatus status) {
        service.save(replenishment);
        status.setComplete();
        return "{success: true}";
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public String delete(String id) {
        service.deleteItemById(id);
        return "{success: true}";
    }

}
