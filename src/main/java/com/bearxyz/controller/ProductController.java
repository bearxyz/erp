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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
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
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private DictRepository dictRepository;


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
        List<Contract> contracts = contractRepository.findAvaliableContractByCompanyId(user.getCompanyId());
        Iterator<Sale> saleIterator = saleList.iterator();

            while (saleIterator.hasNext()){
                Sale sale = saleIterator.next();
                boolean has = false;
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
                Dict dict = dictRepository.findByMask(sale.getProject());
                for(Contract contract: contracts){
                    if(contract.getProject().equals(dict.getName()))
                        has = true;
                }
                if(!has)
                    saleIterator.remove();
            }
        ObjectMapper mapper = new ObjectMapper();
        //System.out.print("saleList==="+mapper.writeValueAsString(saleList));
        return mapper.writeValueAsString(saleIterator);
    }
    @RequestMapping(value = "/cart", method = RequestMethod.GET)
    public String cart(Model model) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Company company= companyRepository.findOne(user.getCompanyId());
        List<Config> configList = configRepository.findAll();
        float deliverFee= 0.00f;
        if(configList!=null && configList.size()>0){
            if(configList.get(0).getDeliverFee()!=null) {
                deliverFee = configList.get(0).getDeliverFee();
            }
        }
        Float discount=clientService.getClientBenefit(user.getId());
        model.addAttribute("company", company);
        model.addAttribute("deliverFee",deliverFee);
        return "/product/cart";
    }
    @RequestMapping(value = "/order/{id}", method = RequestMethod.GET)
        public String order(Model model,@PathVariable("id")String id) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Company company= companyRepository.findOne(user.getCompanyId());
        Order order =orderService.getOrderById(id);
        float balance=(float)0.00;
        if(company.getBalance() !=null){
            balance=company.getBalance();
        }

        Float discount=clientService.getClientBenefit(user.getId());
        Float discountTotalPrice=(float)0.00f;
        //Float discount=(float)0.8;
        if(discount!=0.1){
            discountTotalPrice=order.getPrice()-order.getPrice()*discount;
        }
        DecimalFormat decimalFormat=new DecimalFormat(".00");
        Float totalPrice=order.getPrice()*discount;
        model.addAttribute("company", company);
        model.addAttribute("discount",discount);
        model.addAttribute("balance",balance);
        model.addAttribute("order",order);
        model.addAttribute("discountTotalPrice",decimalFormat.format(discountTotalPrice));
        model.addAttribute("totalPrice",decimalFormat.format(totalPrice));
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
    @RequestMapping(value = "/upateApply", method = RequestMethod.POST)
    @ResponseBody
    public String upateApply(Order order,String saleId,String discountCode,String discountPrice,String prices ,SessionStatus status, HttpServletRequest request) {
        Order order1=orderService.getOrderById(order.getId());
        order1.setPrice(order.getPrice());
        order1.setDiscountTotalPrice(order.getDiscountTotalPrice());
        if(saleId !=null &&  discountCode ==null &&  discountPrice ==null && prices !=null ){
            String [] saleIds =saleId.split(",");
            String [] discountCodes =discountCode.split(",");
            String [] discountPrices =discountPrice.split(",");
            String [] pricess =prices.split(",");
            for(int i=0;i<saleIds.length;i++){
                List<OrderItem> orderItems =order1.getItems();
                for(int k=0;k<orderItems.size();k++){
                    OrderItem orderItem =orderItems.get(k);
                    if(orderItem.getSaleId().equals(saleIds[i])){
                        if(discountPrices[i].equals("")){
                            orderItem.setDiscountPrice((float)0.00);
                        }else{
                            orderItem.setDiscountPrice(Float.parseFloat(discountPrices[i]));
                        }
                        orderItem.setDiscountCode(discountCodes[i]);
                        orderItem.setTotalPrice(Float.parseFloat(pricess[i]));
                    };
                }
            }
        }
        orderService.apply(order1);
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
