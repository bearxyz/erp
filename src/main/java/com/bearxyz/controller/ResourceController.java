package com.bearxyz.controller;

import com.bearxyz.domain.po.business.Company;
import com.bearxyz.domain.po.business.Config;
import com.bearxyz.domain.po.business.Order;
import com.bearxyz.domain.po.business.Sale;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.repository.CompanyRepository;
import com.bearxyz.repository.ConfigRepository;
import com.bearxyz.service.business.OrderService;
import com.bearxyz.service.business.SaleService;
import com.bearxyz.utility.OrderUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by bearxyz on 2017/9/2.
 */

@Controller
@RequestMapping("/resource")
public class ResourceController {

    @Autowired
    private SaleService saleService;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ConfigRepository configRepository;

    @RequestMapping("/list")
    public String index(){
        return "/resource/index";
    }

    @ResponseBody
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public String getIndex(String category) throws JsonProcessingException {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<Sale> saleList =saleService.getSaleByTypeList(user.getCompanyId(),category);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(saleList);
    }

    @RequestMapping(value = "/order/{saleId}", method = RequestMethod.GET)
    public String order(Model model,@PathVariable(value = "saleId")String saleId) throws JsonProcessingException {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Company company= companyRepository.findOne(user.getCompanyId());
        Sale sale =saleService.getById(saleId);
        List<Config> configList = configRepository.findAll();
        float deliverFee= 0.00f;
        if(configList!=null && configList.size()>0){
            if(configList.get(0).getDeliverFee()!=null) {
                deliverFee = configList.get(0).getDeliverFee();
            }
        }
        model.addAttribute("company", company);
        model.addAttribute("deliverFee",deliverFee);
        model.addAttribute("sale",sale);
        float totalPrice=deliverFee+sale.getPrice();
        model.addAttribute("totalPrice",totalPrice);
       return "/resource/order";
    }

    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    @ResponseBody
    public String apply(Order order, SessionStatus status, HttpServletRequest request) {
        order.setCode(OrderUtils.genSerialnumber("0"));
        order.setType("GOODS_VIRTUAL");
        order.setTypeName("桔宝库");
        orderService.apply(order);
        return "{success: true}";
    }



}
