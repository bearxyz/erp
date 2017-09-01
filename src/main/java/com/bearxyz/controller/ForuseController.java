package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.*;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.service.business.ForUseService;
import com.bearxyz.service.business.GoodsService;
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
 * Created by bearxyz on 2017/8/2.
 */

@Controller
@RequestMapping("/foruse")
@SessionAttributes("foruse")
public class ForuseController {

    @Autowired
    private ForUseService service;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(){
        return "/foruse/list";
    }

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public String getList(@RequestBody PaginationCriteria req) throws JsonProcessingException {
        DataTable<ForUse> foruses = service.getForUse(null, "OFFICE",true, req);
        foruses.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(foruses);
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(){
        return "/foruse/index";
    }

    @ResponseBody
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public String getIndex(@RequestBody PaginationCriteria req) throws JsonProcessingException {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        DataTable<ForUse> foruses = service.getForUse(user.getId(),"OFFICE", false, req);
        foruses.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(foruses);
    }

    @RequestMapping(value = "/apply", method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("foruse", new ForUse());
        return "/foruse/apply";
    }

    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    @ResponseBody
    public String doCreate(@ModelAttribute("foruse")ForUse forUse, SessionStatus status) {
        forUse.setType("OFFICE");
        service.apply(forUse);
        status.setComplete();
        return "{success: true}";
    }

    @RequestMapping(value = "/reApply/{id}", method = RequestMethod.GET)
    public String reApply(@PathVariable("id") String id, Model model) {
        ForUse forUse = service.getOneById(id);
        for(ForUseItem item: forUse.getItems()){
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
        model.addAttribute("foruse", forUse);
        model.addAttribute("taskId", task.getId());
        return "/foruse/reApply";
    }

    @RequestMapping(value = "/reApply", method = RequestMethod.POST)
    @ResponseBody
    public String doReApply(@ModelAttribute("foruse")ForUse forUse, SessionStatus status) {
        service.save(forUse);
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
        ForUse forUse = service.getOneById(id);
        for(ForUseItem item: forUse.getItems()){
            Goods goods = goodsService.getById(item.getGoodsId());
            item.setGoods(goods);
        }
        model.addAttribute("foruse", forUse);
        return "/foruse/show";
    }

    @RequestMapping(value = "/complete")
    public String complete(@RequestParam("bid") String bid, @RequestParam("tid") String tid, @RequestParam("applyer") String applyer, Model model) {
        ForUse forUse = service.getOneById(bid);
        for(ForUseItem item: forUse.getItems()){
            Goods goods = goodsService.getById(item.getGoodsId());
            item.setGoods(goods);
        }
        model.addAttribute("foruse", forUse);
        String memo = "";
        model.addAttribute("applyer", applyer);
        Task task = taskService.createTaskQuery().taskId(tid).singleResult();
        if (!task.getTaskDefinitionKey().equals("deptLeader")&&taskService.getVariable(task.getId(), "deptLeaderMemo") != null)
            memo = taskService.getVariable(task.getId(), "deptLeaderMemo").toString();
        model.addAttribute("taskId", tid);
        model.addAttribute("taskKey", task.getTaskDefinitionKey());
        model.addAttribute("memo", memo);
        return "/foruse/complete";
    }

}
