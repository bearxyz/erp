package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.*;
import com.bearxyz.domain.po.sys.Dict;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.repository.*;
import com.bearxyz.service.business.*;
import com.bearxyz.service.sys.SysService;
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
    @Autowired
    private SysService sysService;
    @Autowired
    private SupportApplyService supportApplyService;
    @Autowired
    private SupportApplyRepository supportApplyRepository;
    @Autowired
    private CouponRepository couponRepository;


    @RequestMapping("/index")
    public String index(){
        return "/product/index";
    }


    @ResponseBody
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public String getIndex(String category) throws JsonProcessingException {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<String> categorys = new ArrayList<String>();
        if(category.equals("")){
            categorys.add("GOODS_NORMAL");
            categorys.add("GOODS_SUPPORT");
        }else if(category.equals("GOODS_NORMAL")){
            categorys.add("GOODS_NORMAL");
        }else if(category.equals("GOODS_SUPPORT")){
            categorys.add("GOODS_SUPPORT");
        }
        List<Sale> saleList =saleService.getSaleByTypeList(categorys);
        if(saleList !=null && saleList.size()>0){
            for(Sale sale:saleList){
                if(sale.getProject() !=null && (!sale.getProject().equals(""))){
                    Dict dict =sysService.getDictByMask(sale.getProject());
                    sale.setProjectName(dict.getName());
                }
                if(sale.getType() !=null && (!sale.getType().equals(""))){
                    Dict dict =sysService.getDictByMask(sale.getType());
                    sale.setTypeName(dict.getName());
                }
                if(sale.getSubtype() !=null &&  (!sale.getSubtype().equals(""))){
                    Dict dict =sysService.getDictByMask(sale.getSubtype());
                    sale.setSubtypeName(dict.getName());
                }
            }
        }
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
        List<Config> configList = configRepository.findAll();
        float deliverFee= 0.00f;
        if(configList!=null && configList.size()>0){
            if(configList.get(0).getDeliverFee()!=null) {
                deliverFee = configList.get(0).getDeliverFee();
            }
        }
        float balance=(float)0.00;
        if(company.getBalance() !=null){
            balance=company.getBalance();
        }
        Float discount=clientService.getClientBenefit(user.getId());
        model.addAttribute("company", company);
        model.addAttribute("deliverFee",deliverFee);
        model.addAttribute("discount",discount);
        model.addAttribute("balance",balance);
        return "/product/order";
    }

    @RequestMapping(value = "/getCouponDiscount", method = RequestMethod.POST)
    @ResponseBody
    public String getCouponDiscount(String code) throws JsonProcessingException {
        Coupon coupon= couponRepository.findCouponByCodeAndUsed(code,false);
        List<Coupon> couponList = new ArrayList<Coupon>();
        if(coupon !=null){
            couponList.add(coupon);
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(couponList);
    }

    @RequestMapping(value = "/getOrderSaleId", method = RequestMethod.POST)
    @ResponseBody
    public String getOrderSaleId(String saleId, HttpServletRequest request)throws JsonProcessingException{
        Sale sale =saleService.getById(saleId);
        if(sale.getProject() !=null && (!sale.getProject().equals(""))){
            Dict dict =sysService.getDictByMask(sale.getProject());
            sale.setProjectName(dict.getName());
        }
        if(sale.getType() !=null && (!sale.getType().equals(""))){
            Dict dict =sysService.getDictByMask(sale.getType());
            sale.setTypeName(dict.getName());
        }
        if(sale.getSubtype() !=null &&  (!sale.getSubtype().equals(""))){
            Dict dict =sysService.getDictByMask(sale.getSubtype());
            sale.setSubtypeName(dict.getName());
        }
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
        if(order.getItems() !=null && order.getItems().size()>0){
            for(OrderItem orderItem:order.getItems()){
                if(orderItem.getDiscountCode()!=null && (!orderItem.getDiscountCode().equals(""))){
                    Coupon coupon=  couponRepository.findCouponByCodeAndUsed(orderItem.getDiscountCode(),false);
                    if(coupon !=null){
                        coupon.setUsed(true);
                        couponRepository.save(coupon);
                    }
                }
            }
        }
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
