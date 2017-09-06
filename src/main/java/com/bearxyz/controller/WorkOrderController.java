package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.Company;
import com.bearxyz.domain.po.business.SupportApply;
import com.bearxyz.domain.po.business.WorkOrder;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.repository.CompanyRepository;
import com.bearxyz.repository.WorkOrderRepository;
import com.bearxyz.service.workflow.WorkflowService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bearxyz on 2017/9/6.
 */
@Controller
@RequestMapping("/workorder")
@SessionAttributes("wo")
public class WorkOrderController {

    @Autowired
    private WorkOrderRepository workOrderRepository;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/workorder/index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @ResponseBody
    public String list(@RequestBody PaginationCriteria req) throws JsonProcessingException {
        Subject u = SecurityUtils.getSubject();
        List<WorkOrder> list = new ArrayList<>();
        if (u.hasRole("ROLE_SERVICE_MANAGER")) {
            list = workOrderRepository.findAll();
        } else {
            list = workOrderRepository.findAllByClientId(((User) u.getPrincipal()).getId());
        }

        for (WorkOrder wo : list) {
            String result = "";
            if (wo.getClientMemo() != null)
                result = wo.getClientMemo();
            if (wo.getDepMemo() != null)
                result += "<br/>" + wo.getDepMemo();
            if (wo.getCooMemo() != null)
                result += "<br />" + wo.getCooMemo();
            if (wo.getCeoMemo() != null)
                result += "<br />" + wo.getCeoMemo();
            wo.setResult(result);
            Company company = companyRepository.findOne(wo.getCompanyId());
            wo.setCompanyName(company.getName());
            Task task = workflowService.getTaskByBussinessId(wo.getId());
            if (task != null) {
                wo.setTaskId(task.getId());
                wo.setTaskName(task.getName());
                wo.setFinishedDate(task.getDueDate());
            } else {
                HistoricProcessInstance historicTaskInstance = workflowService.getHistoryProcessByBussinessId(wo.getId());
                if (historicTaskInstance != null) {
                    if (wo.getFinished() &&workflowService.getHistoryVarByProcessId(historicTaskInstance.getId(), "cando")!=null&& (boolean) workflowService.getHistoryVarByProcessId(historicTaskInstance.getId(), "cando"))
                        wo.setTaskName("已完成");
                    else
                        wo.setTaskName("已取消");
                    wo.setTaskId(historicTaskInstance.getId());
                    wo.setFinishedDate(historicTaskInstance.getEndTime());
                }
            }
        }
        DataTable<WorkOrder> workOrder = new DataTable<>();
        workOrder.setData(list);
        workOrder.setRecordsFiltered((long) list.size());
        workOrder.setRecordsTotal((long) list.size());
        workOrder.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(workOrder);
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id")String id, Model model){
        WorkOrder wo = workOrderRepository.findOne(id);
        model.addAttribute("wo", wo);
        return "/workorder/edit";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public String doEdit(@ModelAttribute("wo") WorkOrder wo, SessionStatus status){
        workOrderRepository.save(wo);
        if(!wo.getFinished()) {
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("name", "工单");
            variables.put("url", "/workorder/");
            variables.put("bid", wo.getId());
            variables.put("applyer", wo.getCreatedBy());
            wo.setProcessInstanceId(workflowService.startWorkflow("work-order", wo.getId(), wo.getCreatedBy(), variables));
        }
        status.setComplete();
        return "{success: true}";
    }

    @RequestMapping(value = "/complete", method = RequestMethod.GET)
    public String complete(@RequestParam("bid") String bid, @RequestParam("tid") String tid, @RequestParam("applyer") String applyer, Model model){
        Task task = taskService.createTaskQuery().taskId(tid).singleResult();
        WorkOrder workOrder = workOrderRepository.findOne(bid);
        model.addAttribute("wo",workOrder);
        model.addAttribute("taskId", tid);
        model.addAttribute("taskKey", task.getTaskDefinitionKey());
        return "/workorder/complete";
    }

    @RequestMapping(value = "/complete", method = RequestMethod.POST)
    @ResponseBody
    public String doComplete(@ModelAttribute("wo") WorkOrder wo, SessionStatus status){
        workOrderRepository.save(wo);
        status.setComplete();
        return "{success: true}";
    }

}
