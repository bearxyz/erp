package com.bearxyz.controller;

import com.bearxyz.domain.po.business.*;
import com.bearxyz.domain.po.sys.Dict;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.repository.CompanyRepository;
import com.bearxyz.repository.ConfigRepository;
import com.bearxyz.repository.ContractRepository;
import com.bearxyz.repository.DictRepository;
import com.bearxyz.service.business.OrderService;
import com.bearxyz.service.business.SaleService;
import com.bearxyz.service.sys.SysService;
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
import java.util.Iterator;
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
    @Autowired
    private SysService sysService;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private DictRepository dictRepository;

    @RequestMapping("/list")
    public String index(){
        return "/resource/index";
    }

    @ResponseBody
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public String getIndex(String category) throws JsonProcessingException {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<Sale> saleList =saleService.getSaleByTypeList(user.getCompanyId(),category);
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
        return mapper.writeValueAsString(saleIterator);
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
