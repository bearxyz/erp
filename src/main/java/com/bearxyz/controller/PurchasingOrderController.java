package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.*;
import com.bearxyz.domain.po.sys.Dict;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.service.business.OfficialPartnerService;
import com.bearxyz.service.business.PurchasingOrderService;
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
import org.springframework.web.bind.support.SessionStatus;

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
    public String doCreate(@ModelAttribute("purchasingOrder") PurchasingOrder order, SessionStatus status) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        order.setOperator(user.getId());
        service.apply(order);
        status.setComplete();
        return "{success: true}";
    }

    @RequestMapping(value = "/reApply/{id}", method = RequestMethod.GET)
    public String reApply(@PathVariable("id") String id, Model model) {
        PurchasingOrder order = service.getOneById(id);
        List<OfficialPartner> officialPartners = officialPartnerService.getListByType("SUPPLIER_COMPANY");
        Task task = taskService.createTaskQuery().processInstanceBusinessKey(id).singleResult();
        model.addAttribute("taskId", task.getId());
        model.addAttribute("purchasingOrder", order);
        model.addAttribute("suppliers", officialPartners);
        return "/purchasingorder/reApply";
    }

    @RequestMapping(value = "/reApply", method = RequestMethod.POST)
    @ResponseBody
    public String doReApply(@ModelAttribute("purchasingOrder")PurchasingOrder order, SessionStatus status) {
        service.save(order);
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

}
