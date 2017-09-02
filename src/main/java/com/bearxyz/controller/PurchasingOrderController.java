package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.OfficialPartner;
import com.bearxyz.domain.po.business.PurchasingOrder;
import com.bearxyz.domain.po.business.PurchasingOrderItem;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.service.business.GoodsService;
import com.bearxyz.service.business.OfficialPartnerService;
import com.bearxyz.service.business.PurchasingOrderService;
import com.bearxyz.service.sys.SysService;
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
 * Created by bearxyz on 2017/8/23.
 */
@Controller
@RequestMapping("/purchasingorder")
@SessionAttributes("purchasingOrder")
public class PurchasingOrderController {

    @Autowired
    private PurchasingOrderService service;
    @Autowired
    private OfficialPartnerService officialPartnerService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private SysService sysService;
    @Autowired
    private GoodsService goodsService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/purchasingorder/index";
    }

    @ResponseBody
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public String getList(@RequestBody PaginationCriteria req) throws JsonProcessingException {
        DataTable<PurchasingOrder> purchasing = service.getPurchasingOrders(false, req);
        purchasing.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(purchasing);
    }

    @ResponseBody
    @RequestMapping(value = "/getItems", method = RequestMethod.POST)
    public String getItems(@RequestParam("id")String id) throws JsonProcessingException{
        List<PurchasingOrderItem> results = service.getItemsByPurchasingId(id);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(results);
    }

    @RequestMapping(value = "/apply/{id}", method = RequestMethod.GET)
    public String create(@PathVariable("id") String id, Model model) {
        PurchasingOrder order = service.getOneById(id);
        List<OfficialPartner> officialPartners = officialPartnerService.getListByType("SUPPLIER_COMPANY");
        model.addAttribute("purchasingOrder", order);
        model.addAttribute("suppliers", officialPartners);
        return "/purchasingorder/apply";
    }

    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    @ResponseBody
    public String doCreate(@RequestParam("id")String id,@RequestParam("uid")String uid) {
        PurchasingOrder order = service.getOneById(id);
        order.setOperator(uid);
        service.apply(order);
        Task task = taskService.createTaskQuery().processInstanceBusinessKey(id).singleResult();
        taskService.setAssignee(task.getId(), uid);
        return "{success: true}";
    }

    @RequestMapping(value = "/reApply/{id}", method = RequestMethod.GET)
    public String reApply(@PathVariable("id") String id, Model model) {
        PurchasingOrder order = service.getOneById(id);
        List<OfficialPartner> officialPartners = officialPartnerService.getListByType("SUPPLIER_COMPANY");
        Task task = taskService.createTaskQuery().processInstanceBusinessKey(id).singleResult();
        String deptMemo = "";
        String managerMemo = "";
        if (taskService.getVariable(task.getId(), "deptLeaderMemo") != null)
            deptMemo = taskService.getVariable(task.getId(), "deptLeaderMemo").toString();
        if (taskService.getVariable(task.getId(), "managerMemo") != null)
            managerMemo = taskService.getVariable(task.getId(), "managerMemo").toString();
        model.addAttribute("deptMemo", deptMemo);
        model.addAttribute("managerMemo", managerMemo);
        model.addAttribute("taskId", task.getId());
        model.addAttribute("purchasingOrder", order);
        model.addAttribute("suppliers", officialPartners);
        return "/purchasingorder/reApply";
    }

    @RequestMapping(value = "/reApply", method = RequestMethod.POST)
    @ResponseBody
    public String doReApply(@ModelAttribute("purchasingOrder")PurchasingOrder order, SessionStatus status, HttpServletRequest request) throws IOException {
        List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("attachment");
        service.save(order,files);
        status.setComplete();
        return "{success: true}";
    }

    @RequestMapping(value = "/show/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") String id, Model model) {
        PurchasingOrder order = service.getOneById(id);
        Float totalPrice = (float) 0.0;
        for (PurchasingOrderItem item : order.getItems()) {
            totalPrice += item.getPrice() * item.getAmmount();
        }
        model.addAttribute("purchasingOrder", order);
        model.addAttribute("totalPrice",totalPrice);
        return "/purchasingorder/show";
    }

    @RequestMapping(value = "/assign/{id}", method = RequestMethod.GET)
    public String man(@PathVariable(value = "id") String id, Model model) {
        DataTable<User> users =sysService.getUserByRole("ROLE_DEPARTMENT_ADMINISTRATION");
        model.addAttribute("users", users.getData());
        model.addAttribute("id", id);
        return "/purchasingorder/assign";
    }

    @RequestMapping(value = "/complete")
    public String complete(@RequestParam("bid") String bid, @RequestParam("tid") String tid, @RequestParam("applyer") String applyer, Model model) {
        PurchasingOrder order = service.getOneById(bid);
        Float totalPrice = (float) 0.0;
        for (PurchasingOrderItem item : order.getItems()) {
            totalPrice += item.getPrice() * item.getCount();
        }
        model.addAttribute("purchasingOrder", order);
        model.addAttribute("totalPrice",totalPrice);
        String memo = "";
        model.addAttribute("applyer", applyer);
        Task task = taskService.createTaskQuery().taskId(tid).singleResult();
        if (!task.getTaskDefinitionKey().equals("deptLeader")&&taskService.getVariable(task.getId(), "deptLeaderMemo") != null)
            memo = taskService.getVariable(task.getId(), "deptLeaderMemo").toString();
        model.addAttribute("taskId", tid);
        model.addAttribute("taskKey", task.getTaskDefinitionKey());
        model.addAttribute("memo", memo);
        return "/purchasingorder/complete";
    }

}
