package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.*;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.repository.CompanyRepository;
import com.bearxyz.repository.SaleRepository;
import com.bearxyz.service.business.ClientService;
import com.bearxyz.service.business.OrderService;
import com.bearxyz.service.business.SaleService;
import com.bearxyz.service.business.SecordOrderService;
import com.bearxyz.utility.OrderUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xerces.internal.util.SAXLocatorWrapper;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import com.bearxyz.repository.ConfigRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bearxyz on 2017/9/2.
 */
@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private SaleService saleService;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ConfigRepository configRepository;
    @Autowired
    private SecordOrderService secordOrderService;
    @Autowired
    private ClientService clientService;


    @RequestMapping("/index")
    public String index(){
        return "/product/index";
    }


    @ResponseBody
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public String getIndex(String category) throws JsonProcessingException {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<Sale> saleList =saleService.getSaleByTypeList(user.getCompanyId(),category);
        ObjectMapper mapper = new ObjectMapper();
        //System.out.print("saleList==="+mapper.writeValueAsString(saleList));
        return mapper.writeValueAsString(saleList);
    }
    @RequestMapping(value = "/cart", method = RequestMethod.GET)
    public String cart(Model model) {
       // model.addAttribute("user", new User());
        return "/product/cart";
    }
    @RequestMapping(value = "/order", method = RequestMethod.GET)
        public String order(Model model) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Company company= companyRepository.findOne(user.getCompanyId());
        //Sale sale =saleService.getById(id);
        List<Config> configList = configRepository.findAll();
        float deliverFee= 0.00f;
        if(configList!=null && configList.size()>0){
            if(configList.get(0).getDeliverFee()!=null) {
                deliverFee = configList.get(0).getDeliverFee();
            }
        }
        float discount=clientService.getClientBenefit(user.getId());
        model.addAttribute("company", company);
        model.addAttribute("deliverFee",deliverFee);
        model.addAttribute("discount",discount);
       //model.addAttribute("totalPrice",sale.getPrice()+deliverFee);
       //model.addAttribute("sale",sale);
        return "/product/order";
    }
    @RequestMapping(value = "/getOrderSaleId", method = RequestMethod.POST)
    @ResponseBody
    public String getOrderSaleId(String saleId, HttpServletRequest request)throws JsonProcessingException{
        Sale sale =saleService.getById(saleId);
        List<Sale> saleList = new ArrayList<Sale>();
        saleList.add(sale);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(saleList);
    }


    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    @ResponseBody
    public String apply(Order order, SessionStatus status, HttpServletRequest request) {
        order.setCode(OrderUtils.genSerialnumber("0"));
        order.setType("GOODS_NORMAL");
        order.setTaskName("普通商品");
        orderService.apply(order);
        return "{success: true}";
    }

    @RequestMapping(value = "/secindex", method = RequestMethod.GET)
    public String secindex(){
        return "/product/secindex";
    }

    @RequestMapping(value = "/secSaleList", method = RequestMethod.POST)
    @ResponseBody
    public String secSaleList(@RequestBody PaginationCriteria req) throws JsonProcessingException {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        DataTable<Sale> sales = saleService.secSaleList(user.getCompanyId());
        sales.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(sales);
    }

    @RequestMapping(value = "/secordOrder", method = RequestMethod.GET)
    public String secordOrder(Model model){
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Company company= companyRepository.findOne(user.getCompanyId());
        //Sale sale =saleService.getById(id);
        List<Config> configList = configRepository.findAll();
        float deliverFee= 0.00f;
        if(configList!=null && configList.size()>0){
            if(configList.get(0).getDeliverFee()!=null) {
                deliverFee = configList.get(0).getDeliverFee();
            }
        }
        model.addAttribute("company", company);
        model.addAttribute("deliverFee",deliverFee);
        return "/product/secordOrder";
    }

    @RequestMapping(value = "/secordApply", method = RequestMethod.POST)
    @ResponseBody
    public String secordApply(SecordOrder secordOrder, SessionStatus status, HttpServletRequest request) {
        secordOrder.setCode(OrderUtils.genSerialnumber("0"));
        secordOrder.setType("GOODS_NORMAL");
        secordOrder.setTaskName("普通商品");
        secordOrderService.apply(secordOrder);
        return "{success: true}";
    }
}
