package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.*;
import com.bearxyz.domain.po.sys.Dict;
import com.bearxyz.repository.*;
import com.bearxyz.utility.OrderUtils;
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
    private SaleRepository saleRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private DictRepository dictRepository;
    @Autowired
    private StockRepository stockRepository;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/order/index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @ResponseBody
    public String getIndex(@RequestBody PaginationCriteria req) throws JsonProcessingException {
        List<Order> o = orderRepository.findAll();
        DataTable<Order> orders = new DataTable<>();
        for (Order ord : o) {
            Company company = companyRepository.findOne(ord.getCompanyId());
            ord.setCompanyName(company.getName());
            Dict dict = dictRepository.findByMask(ord.getType());
            if (dict != null)
                ord.setTypeName(dict.getName());
        }
        orders.setData(o);
        orders.setRecordsFiltered((long) o.size());
        orders.setRecordsTotal((long) o.size());
        orders.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(orders);
    }

    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    @ResponseBody
    public String pay(@RequestParam("id") String id) {
        Order order = orderRepository.findOne(id);
        Sale sale = null;
        if (order.getSaleId() != null) sale = saleRepository.findOne(order.getSaleId());
        if (sale != null && !sale.getCategory().equals("GOODS_NORMAL")) {
            sale.setBuyer(sale.getBuyer()+1);
            saleRepository.save(sale);
            saleRepository.insertCompanyUser(order.getCompanyId(), sale.getId(), sale.getCategory());
        } else {
            Stock stock = new Stock();
            stock.setType("STOCK-OUT");
            stock.setMask("STOCK_OUT_ORDER");
            stock.setCode(OrderUtils.genSerialnumber("CK"));
            stock.setPurpose("销售出库");
            stock.setDeliverAddress(order.getProvince() + order.getCity() + order.getDistrict() + order.getAddress());
            for (OrderItem item : order.getItems()) {
                Sale s = saleRepository.findOne(item.getSaleId());
                s.setBuyer(s.getBuyer()+item.getCount());
                saleRepository.save(s);
            }
            stock.setOrderId(order.getId());
            stockRepository.save(stock);
        }
        order.setStatus(2);
        orderRepository.save(order);
        return "{success: true}";
    }

}
