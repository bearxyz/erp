package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.common.TreeNode;
import com.bearxyz.domain.po.business.*;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.repository.CompanyRepository;
import com.bearxyz.repository.OrderRepository;
import com.bearxyz.repository.WorkOrderRepository;
import com.bearxyz.service.business.SaleService;
import com.bearxyz.service.business.SupportApplyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.bearxyz.service.business.OrderService;

import java.util.List;


@Controller
@RequestMapping("/my")
public class MyOrderController {

    @Autowired
    private OrderService orderServce;
    @Autowired
    private SaleService saleService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private SupportApplyService supportApplyService;
    @Autowired
    private WorkOrderRepository workOrderRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @RequestMapping("/order")
    public String order() {
        return "/my/order";
    }

    @ResponseBody
    @RequestMapping(value = "/getOrderState", method = RequestMethod.POST)
    public String getOrderState(int status) throws JsonProcessingException {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<Order> orderList = orderServce.getOrderByStateList(status, user.getCompanyId());
        if (orderList != null && orderList.size() > 0) {
            for (Order order : orderList) {
                if (order.getSaleId() != null && (!order.getSaleId().equals(""))) {
                    order.setSale(saleService.getById(order.getSaleId()));
                }
                if (order != null && order.getItems() != null && order.getItems().size() > 0) {
                    for (OrderItem orderItem : order.getItems()) {
                        if (orderItem.getSaleId() != null && (!orderItem.getSaleId().equals(""))) {
                            Sale sale = saleService.getById(orderItem.getSaleId());
                            if (sale != null) {
                                orderItem.setSale(sale);
                            }
                        }
                    }
                }
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(orderList);
    }

    @ResponseBody
    @RequestMapping(value = "/updateOrderState", method = RequestMethod.POST)
    public String updateOrderState(String orderId, int status) throws JsonProcessingException {
        Order order = orderServce.getOrderById(orderId);
        order.setStatus(status);
        orderRepository.save(order);
        return "{success: true}";
    }

    @RequestMapping(value = "/support", method = RequestMethod.GET)
    public String support() {
        return "/my/support";
    }

    @RequestMapping(value = "/support", method = RequestMethod.POST)
    @ResponseBody
    public String supportList(@RequestBody PaginationCriteria req) throws JsonProcessingException {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        DataTable<SupportApply> support = supportApplyService.getApplyByConditions(null, user.getCompanyId());
        support.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(support);
    }

    @RequestMapping(value = "/workorder", method = RequestMethod.GET)
    public String workOrder() {
        return "/my/workorder";
    }

    @RequestMapping(value = "/workorder", method = RequestMethod.POST)
    @ResponseBody
    public String workOrderList(@RequestBody PaginationCriteria req) throws JsonProcessingException {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<WorkOrder> list = workOrderRepository.findAllByCompanyId(user.getCompanyId());
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
        }
        DataTable<WorkOrder> workOrder = new DataTable<>();
        workOrder.setData(list);
        workOrder.setRecordsFiltered((long) list.size());
        workOrder.setRecordsTotal((long) list.size());
        workOrder.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(workOrder);
    }

    @RequestMapping(value = "/createworkorder", method = RequestMethod.GET)
    public String createWorkOrder() {
        return "/my/createworkorder";
    }

    @RequestMapping(value = "/createworkword", method = RequestMethod.POST)
    @ResponseBody
    public String doCreateWorkOrder(String type, String memo) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        WorkOrder workOrder = new WorkOrder();
        workOrder.setCompanyId(user.getCompanyId());
        workOrder.setType(type);
        workOrder.setMemo(memo);
        workOrderRepository.save(workOrder);
        return "{success: true}";
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public String info(Model model) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Company company = companyRepository.findOne(user.getCompanyId());
        model.addAttribute("user", user);
        model.addAttribute("company", company);
        return "/my/info";
    }

    @RequestMapping(value = "/info", method = RequestMethod.POST)
    @ResponseBody
    public String updateInfo(
            @RequestParam("address") String address,
            @RequestParam("numberOfTeacher") int numberOfTeacher,
            @RequestParam("numberOfFulltime") int numberOfFulltime,
            @RequestParam("numberOfParttime") int numberOfParttime,
            @RequestParam("numberOfOtherTeacher") int numberOfOtherTeacher,
            @RequestParam("numberOfOther") int numberOfOther,
            @RequestParam("contactor") String contactor,
            @RequestParam("contactPhone") String contactPhone
    ){
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Company company = companyRepository.findOne(user.getCompanyId());
        company.setAddress(address);
        company.setNumberOfTeacher(numberOfTeacher);
        company.setNumberOfFulltime(numberOfFulltime);
        company.setNumberOfParttime(numberOfParttime);
        company.setNumberOfOther(numberOfOther);
        company.setNumberOfOtherTeacher(numberOfOtherTeacher);
        company.setContactor(contactor);
        company.setContactPhone(contactPhone);
        companyRepository.save(company);
        return "{success: true}";
    }
}
