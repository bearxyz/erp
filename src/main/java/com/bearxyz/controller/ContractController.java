package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.Contract;
import com.bearxyz.service.business.ContractService;
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
 * Created by bearxyz on 2017/7/27.
 */

@Controller
@RequestMapping("/contract")
@SessionAttributes("contract")
public class ContractController {

    @Autowired
    private ContractService service;
    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "/complete")
    public String complete(@RequestParam("bid") String bid, @RequestParam("tid") String tid, @RequestParam("applyer") String applyer, Model model) {
        Contract purchasing = service.getById(bid);
        String memo = "";
        model.addAttribute("contract", purchasing);
        model.addAttribute("applyer", applyer);
        Task task = taskService.createTaskQuery().taskId(tid).singleResult();
        if (!task.getTaskDefinitionKey().equals("deptLeader")&&taskService.getVariable(task.getId(), "deptLeaderMemo") != null)
            memo = taskService.getVariable(task.getId(), "deptLeaderMemo").toString();
        model.addAttribute("taskId", tid);
        model.addAttribute("taskKey", task.getTaskDefinitionKey());
        model.addAttribute("memo", memo);
        return "/contract/complete";
    }

    @RequestMapping(value = "/index/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String index(@RequestBody PaginationCriteria req, @PathVariable("id")String id) throws JsonProcessingException {
        DataTable<Contract> records = service.getContacts(id, req);
        records.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(records);
    }

    @RequestMapping(value = "/create/{id}", method = RequestMethod.GET)
    public String createRecord(@PathVariable("id")String id, Model model) {
        model.addAttribute("companyId", id);
        model.addAttribute("contract", new Contract());
        return "/contract/create";
    }

    @ResponseBody
    @RequestMapping(value = {"/create"}, method = RequestMethod.POST)
    public String save(@ModelAttribute("contract")Contract record, SessionStatus status, HttpServletRequest request) throws IOException {
        List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("attachment");
        service.apply(record, files);
        status.setComplete();
        return "{success: true}";
    }

    @RequestMapping(value = "/reApply/{id}", method = RequestMethod.GET)
    public String reApply(@PathVariable("id")String id, Model model) {
        Task task = taskService.createTaskQuery().processInstanceBusinessKey(id).singleResult();
        Contract contract = service.getById(id);
        model.addAttribute("companyId", id);
        model.addAttribute("contract", contract);
        model.addAttribute("taskId", task.getId());
        return "/contract/reApply";
    }

    @RequestMapping(value = "/reApply", method = RequestMethod.POST)
    @ResponseBody
    public String doReApply(@ModelAttribute("contract")Contract contract, SessionStatus status, HttpServletRequest request) throws IOException {
        List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("attachment");
        service.save(contract, files);
        status.setComplete();
        return "{success: true}";
    }

}
