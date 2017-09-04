package com.bearxyz.controller;

import com.bearxyz.common.ActionResponse;
import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.*;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.domain.vo.Variable;
import com.bearxyz.repository.CompanyRepository;
import com.bearxyz.repository.SaleRepository;
import com.bearxyz.repository.SupportApplyRepository;
import com.bearxyz.service.business.SupportApplyService;
import com.bearxyz.service.sys.SysService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bearxyz on 2017/9/4.
 */
@Controller
@RequestMapping("/supportapply")
@SessionAttributes("supportapply")
public class SupportApplyController {

    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private SupportApplyService service;
    @Autowired
    private TaskService taskService;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private SysService sysService;
    @Autowired
    private SupportApplyRepository repository;

    @RequestMapping("/index")
    public String index() {
        return "/supportapply/index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @ResponseBody
    public String list(@RequestBody PaginationCriteria req) throws JsonProcessingException {
        Subject u = SecurityUtils.getSubject();
        DataTable<SupportApply> support = new DataTable<>();
        if (u.hasRole("ROLE_SERVICE_MANAGER")) {
            support = service.getApplyByConditions(null, null);
        } else {
            List<SupportApply> res = repository.getAllByClientId(((User) u.getPrincipal()).getId());
            support.setData(res);
            support.setRecordsTotal((long)res.size());
            support.setRecordsFiltered((long)res.size());
        }
        support.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(support);
    }

    @RequestMapping("/create/{sid}")
    public String create(@PathVariable("sid") String sid, Model model) {
        Sale sale = saleRepository.findOne(sid);
        model.addAttribute("sale", sale);
        model.addAttribute("supportapply", new SupportApply());
        return "/supportapply/apply";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public String doCreate(@ModelAttribute("supportapply") SupportApply supportapply, SessionStatus status) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        supportapply.setCompanyId(user.getCompanyId());
        service.save(supportapply);
        status.setComplete();
        return "{success: true}";
    }

    @RequestMapping("/apply/{sid}")
    public String apply(@PathVariable("sid") String sid, Model model) {
        Sale sale = saleRepository.findOne(sid);
        model.addAttribute("sale", sale);
        model.addAttribute("supportapply", new SupportApply());
        return "/supportapply/apply";
    }

    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    @ResponseBody
    public String doApply(@ModelAttribute("supportapply") SupportApply supportapply, SessionStatus status) {
        service.apply(supportapply);
        status.setComplete();
        return "{success: true}";
    }

    @RequestMapping(value = "/complete", method = RequestMethod.GET)
    public String complete(@RequestParam("bid") String bid, @RequestParam("tid") String tid, @RequestParam("applyer") String applyer, Model model) {
        SupportApply sale = service.getById(bid);
        Company company = companyRepository.findOne(sale.getCompanyId());
        Sale s = saleRepository.findOne(sale.getSaleId());
        sale.setSale(s);
        sale.setApplyer(company.getName());
        model.addAttribute("supportapply", sale);
        String memo = "";
        model.addAttribute("applyer", applyer);
        Task task = taskService.createTaskQuery().taskId(tid).singleResult();
        if (!task.getTaskDefinitionKey().equals("deptLeader") && taskService.getVariable(task.getId(), "deptLeaderMemo") != null)
            memo = taskService.getVariable(task.getId(), "deptLeaderMemo").toString();
        model.addAttribute("taskId", tid);
        model.addAttribute("taskKey", task.getTaskDefinitionKey());
        model.addAttribute("memo", memo);
        return "/supportapply/complete";
    }

    @RequestMapping(value = "/assign/{taskId}", method = RequestMethod.GET)
    public String transfer(@PathVariable("taskId") String taskId, Model model) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        List<User> managers = sysService.getDeparmentManager();
        model.addAttribute("taskId", taskId);
        model.addAttribute("managers", managers);
        model.addAttribute("taskKey", task.getTaskDefinitionKey());
        return "/supportapply/assign";
    }

    @RequestMapping(value = "/assign", method = RequestMethod.POST)
    @ResponseBody
    public String doComplete(String taskId, String uid) throws JsonProcessingException {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("userId", uid);
        taskService.complete(taskId, variables);
        ObjectMapper mapper = new ObjectMapper();
        ActionResponse response = new ActionResponse();
        response.setSuccess(true);
        return mapper.writeValueAsString(response);
    }

}
