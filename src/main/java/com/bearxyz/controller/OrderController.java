package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.Company;
import com.bearxyz.domain.po.business.Order;
import com.bearxyz.domain.po.sys.Dict;
import com.bearxyz.repository.CompanyRepository;
import com.bearxyz.repository.DictRepository;
import com.bearxyz.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by bearxyz on 2017/9/4.
 */

@Controller
@RequestMapping("/order")
@SessionAttributes("order")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private DictRepository dictRepository;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(){
        return "/order/index";
    }

    @RequestMapping(value = "/index", method =RequestMethod.POST)
    @ResponseBody
    public String getIndex(@RequestBody PaginationCriteria req) throws JsonProcessingException {
        List<Order> o = orderRepository.findAll();
        DataTable<Order> orders = new DataTable<>();
        for(Order ord: o){
            Company company = companyRepository.findOne(ord.getCompanyId());
            ord.setCompanyName(company.getName());
            Dict dict = dictRepository.findByMask(ord.getType());
            if(dict!=null)
                ord.setTypeName(dict.getName());
        }
        orders.setData(o);
        orders.setRecordsFiltered((long)o.size());
        orders.setRecordsTotal((long)o.size());
        orders.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(orders);
    }

}
