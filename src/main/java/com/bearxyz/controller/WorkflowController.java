package com.bearxyz.controller;

import com.bearxyz.common.ActionResponse;
import com.bearxyz.common.DataTable;
import com.bearxyz.domain.po.sys.Dict;
import com.bearxyz.service.workflow.WorkflowService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.activiti.engine.repository.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by bearxyz on 2017/8/8.
 */
@Controller
@RequestMapping(value = "/workflow")
public class WorkflowController {

    @Autowired
    private WorkflowService workflowService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/workflow/index";
    }

    @ResponseBody
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public String getList(@RequestParam("draw") String draw) throws JsonProcessingException {
        DataTable<Model> models = workflowService.getAllModelList();
        models.setDraw(Integer.parseInt(draw));
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(models);
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create() {
        return "/workflow/create";
    }

    @ResponseBody
    @RequestMapping(value = {"/create"}, method = RequestMethod.POST)
    public String save(@RequestParam("name") String name, @RequestParam("key") String key, @RequestParam("description") String description) throws UnsupportedEncodingException, JsonProcessingException {
        String id = workflowService.create(name, key, description);
        ActionResponse response = new ActionResponse();
        response.setSuccess(true);
        response.setId(id);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(response);
    }

    @ResponseBody
    @RequestMapping(value = {"/delete"}, method = RequestMethod.POST)
    public String delete(@Param("id") String id) {
        workflowService.delete(id);
        return "{success: true}";
    }

    @ResponseBody
    @RequestMapping(value = {"/deploy"}, method = RequestMethod.POST)
    public String deploy(@Param("id") String id) throws IOException {
        workflowService.deploy(id);
        return "{success: true}";
    }

}
