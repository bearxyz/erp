package com.bearxyz.controller;

import com.bearxyz.common.ActionResponse;
import com.bearxyz.common.DataTable;
import com.bearxyz.common.Select;
import com.bearxyz.common.TreeNode;
import com.bearxyz.domain.po.business.Goods;
import com.bearxyz.domain.po.sys.Dict;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.domain.vo.TaskVO;
import com.bearxyz.domain.vo.Variable;
import com.bearxyz.service.business.GoodsService;
import com.bearxyz.service.sys.SysService;
import com.bearxyz.utility.RelativeDateFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    ProcessEngine processEngine;

    @Autowired
    ProcessEngineConfiguration processEngineConfiguration;

    @RequestMapping(value = "/getDict")
    @ResponseBody
    public String getDict(@RequestParam("mask") String mask) throws JsonProcessingException {
        List<Dict> dicts = sysService.getAllDictByParentMask(mask);
        List<Select> selects = new ArrayList<>();
        for (Dict dict : dicts) {
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
    public String selectProduct() {
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
    public String tasks(Model model) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateOrAssigned(user.getId()).list();
        for (Task task : tasks) {
        }
        model.addAttribute("tasks", tasks);
        return "/common/tasks";
    }

    @RequestMapping(value = "/task/todo", method = RequestMethod.GET)
    public String todo(Model model) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateOrAssigned(user.getId()).list();
        List<TaskVO> taskVOS = new ArrayList<>();
        for (Task task : tasks) {
            TaskVO vo = new TaskVO();
            vo.setTaskId(task.getId());
            if(taskService.getVariable(task.getId(),"name")!=null)
                vo.setName(taskService.getVariable(task.getId(),"name").toString());
            if(taskService.getVariable(task.getId(),"url")!=null)
                vo.setDetailUrl(taskService.getVariable(task.getId(),"url").toString());
            if(taskService.getVariable(task.getId(),"bid")!=null)
                vo.setBussinessId(taskService.getVariable(task.getId(),"bid").toString());
            if(taskService.getVariable(task.getId(),"applyer")!=null) {
                User u = sysService.getUserById(taskService.getVariable(task.getId(), "applyer").toString());
                vo.setApplyer(u.getUsername());
            }
            vo.setAssignee(task.getAssignee());
            vo.setStage(task.getName());
            vo.setProcessInstanceId(task.getProcessInstanceId());
            vo.setCreateDate(RelativeDateFormat.format(task.getCreateTime()));
            vo.setCreateDatetime(task.getCreateTime());
            vo.setTaskDefinitionKey(task.getTaskDefinitionKey());

            taskVOS.add(vo);
        }
        model.addAttribute("tasks", taskVOS);
        return "/common/task/todo";
    }

    @RequestMapping(value = "/task/complete/{taskId}", method = RequestMethod.GET)
    public String complete(@PathVariable("taskId") String taskId, Model model){
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        model.addAttribute("taskId", taskId);
        model.addAttribute("taskKey", task.getTaskDefinitionKey());
        return "/common/task/complete";
    }

    @RequestMapping(value = "/task/complete/{taskId}", method = RequestMethod.POST)
    @ResponseBody
    public String doComplete(@PathVariable("taskId") String taskId, Variable var) throws JsonProcessingException {
        Map<String, Object> variables = var.getVariableMap();
        taskService.complete(taskId, variables);
        ObjectMapper mapper = new ObjectMapper();
        ActionResponse response = new ActionResponse();
        response.setSuccess(true);
        return mapper.writeValueAsString(response);
    }

    @RequestMapping(value = "/task/trace/{executionId}")
    public String trace(@PathVariable("executionId") String executionId, Model model) {
        model.addAttribute("executionId", executionId);
        return "/common/task/trace";
    }

    @RequestMapping(value = "/task/trace/show/{executionId}")
    public void readResource(@PathVariable("executionId") String executionId, HttpServletResponse response) throws IOException {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(executionId).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        List<String> activeActivityIds = runtimeService.getActiveActivityIds(executionId);
        processEngineConfiguration = processEngine.getProcessEngineConfiguration();
        Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);

        ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
        InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds);

        byte[] b = new byte[1024];
        int len;
        while ((len = imageStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }

    @RequestMapping(value = "/task/claim", method = RequestMethod.POST)
    @ResponseBody
    public String claim(String taskId) throws JsonProcessingException {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        taskService.claim(taskId, user.getId());
        ObjectMapper mapper = new ObjectMapper();
        ActionResponse response = new ActionResponse();
        response.setSuccess(true);
        return mapper.writeValueAsString(response);
    }

    @RequestMapping(value = "/task/setAssignee", method = RequestMethod.POST)
    @ResponseBody
    public String setAssignee(String taskId, String uid) throws JsonProcessingException {
        taskService.setAssignee(taskId, uid);
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
