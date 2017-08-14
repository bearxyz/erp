package com.bearxyz.controller;

import com.bearxyz.common.ActionResponse;
import com.bearxyz.common.DataTable;
import com.bearxyz.common.Select;
import com.bearxyz.common.TreeNode;
import com.bearxyz.domain.po.business.Goods;
import com.bearxyz.domain.po.sys.Dict;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.service.business.GoodsService;
import com.bearxyz.service.sys.SysService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bearxyz on 2017/6/23.
 */
@Controller
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private SysService sysService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "/getDict")
    @ResponseBody
    public String getDict(@RequestParam("mask") String mask) throws JsonProcessingException {
        List<Dict> dicts = sysService.getAllDictByParentMask(mask);
        List<Select> selects = new ArrayList<>();
        for(Dict dict:dicts){
            Select select = new Select();
            select.setId(dict.getMask());
            select.setText(dict.getName());
            selects.add(select);
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(selects);
    }

    @ResponseBody
    @RequestMapping(value = "/getDictTree", method = RequestMethod.GET)
    public List<TreeNode> getTree(@RequestParam("mask") String mask) {
        List<TreeNode> treeNodeList = sysService.getDictTreeByParentMask(mask);
        return treeNodeList;
    }

    @RequestMapping(value = "/selectProduct", method = RequestMethod.GET)
    public String selectProduct(){
        return "/common/selectProduct";
    }

    @RequestMapping(value = "/selectProduct", method = RequestMethod.POST)
    @ResponseBody
    public String selectProductList(@RequestParam(value = "mask", required = false) String mask, @RequestParam("draw") String draw) throws JsonProcessingException {
        DataTable<Goods> goods = goodsService.getByNature(mask);
        goods.setDraw(Integer.parseInt(draw));
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(goods);
    }

    @RequestMapping(value = "/listProduct", method = RequestMethod.POST)
    @ResponseBody
    public String getProduct(String[] ids) throws JsonProcessingException {
        List<Goods> goods = goodsService.getByIds(ids);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(goods);
    }

    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    public String tasks(Model model){
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateOrAssigned(user.getId()).list();
        for(Task task: tasks){
        }
        model.addAttribute("tasks", tasks);
        return "/common/tasks";
    }

    @RequestMapping(value = "/task/todo", method = RequestMethod.GET)
    public String todo(){
        return "/common/task/todo";
    }

    @RequestMapping(value = "/task/claim/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String claim(@PathVariable("id")String taskId) throws JsonProcessingException {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        taskService.claim(taskId, user.getId());
        ObjectMapper mapper = new ObjectMapper();
        ActionResponse response = new ActionResponse();
        response.setSuccess(true);
        return mapper.writeValueAsString(response);
    }

    @RequestMapping(value = "/task/addUser", method = RequestMethod.POST)
    @ResponseBody
    public String addCandidateUser(String taskId, String uid) throws JsonProcessingException {
        taskService.addCandidateUser(taskId, uid);
        ObjectMapper mapper = new ObjectMapper();
        ActionResponse response = new ActionResponse();
        response.setSuccess(true);
        return mapper.writeValueAsString(response);
    }

    @RequestMapping(value = "/task/delegate", method = RequestMethod.POST)
    @ResponseBody
    public String delegateTask(String taskId, String uid) throws JsonProcessingException {
        taskService.delegateTask(taskId, uid);
        ObjectMapper mapper = new ObjectMapper();
        ActionResponse response = new ActionResponse();
        response.setSuccess(true);
        return mapper.writeValueAsString(response);
    }

}
