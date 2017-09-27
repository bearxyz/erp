package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.domain.po.business.*;
import com.bearxyz.domain.po.sys.Dict;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.domain.vo.ContractReport;
import com.bearxyz.domain.vo.SaleReport;
import com.bearxyz.mapper.*;
import com.bearxyz.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private PurchasingOrderItemRepository purchasingOrderItemRepository;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private DictRepository dictRepository;
    @Autowired
    private OfficialPartnerRepository officialPartnerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PurchasingOrderItemMapper purchasingOrderItemMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private StockItemRepository stockItemRepository;
    @Autowired
    private ContractMapper contractMapper;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;

    @RequestMapping(value = "/purchasing", method = RequestMethod.GET)
    public String purchasingReport() {
        return "/report/purchasing";
    }

    @RequestMapping(value = "/purchasing", method = RequestMethod.POST)
    @ResponseBody
    public String purchasingReportList(Integer draw, String startDate, String endDate, String project, String type, String name) throws JsonProcessingException {
        DataTable<PurchasingOrderItem> result = new DataTable<>();
        startDate = (startDate == null) ? "1970-1-1" : startDate;
        endDate = (endDate == null) ? "1970-1-1" : endDate;
        project = (project == null) ? "" : project;
        type = (type == null) ? "" : type;
        name = (name == null) ? "" : name;
        List<PurchasingOrderItem> list = purchasingOrderItemMapper.reportPurchasingOrderItem(startDate, endDate, project, type, name);
        Integer totalCount = 0;
        Float totalPrice = (float) 0.0;
        for (PurchasingOrderItem item : list) {
            Goods goods = goodsRepository.findOne(item.getGoodsId());
            if (goods.getProject() != null) {
                Dict proj = dictRepository.findByMask(goods.getProject());
                goods.setProjectName(proj.getName());
            }
            if (goods.getType() != null) {
                Dict tp = dictRepository.findByMask(goods.getType());
                goods.setTypeName(tp.getName());
            }
            item.setGoods(goods);
            if (item.getSupplier() != null) {
                OfficialPartner partner = officialPartnerRepository.findOne(item.getSupplier());
                item.setSupplierName(partner.getName());
            }
            PurchasingOrderItem i = purchasingOrderItemRepository.findOne(item.getId());
            item.setOrder(i.getOrder());
            if (item.getOrder().getOperator() != null) {
                User user = userRepository.findOne(item.getOrder().getOperator());
                item.getOrder().setApplyer(user.getFirstName() + user.getLastName());
            }
            totalCount += item.getCount();
            totalPrice += item.getPrice();
        }
        PurchasingOrderItem total = new PurchasingOrderItem();
        total.setCount(totalCount);
        total.setPrice(totalPrice);
        PurchasingOrder o = new PurchasingOrder();
        o.setCode("合计：");
        Goods goods = new Goods();
        total.setGoods(goods);
        total.setOrder(o);
        list.add(total);
        result.setDraw(draw);
        result.setRecordsFiltered((long) list.size());
        result.setRecordsTotal((long) list.size());
        result.setData(list);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(result);
    }

    @RequestMapping(value = "/stockin", method = RequestMethod.GET)
    public String stockin() {
        return "/report/stockin";
    }

    @RequestMapping(value = "/stockin", method = RequestMethod.POST)
    @ResponseBody
    public String stockinReport(Integer draw, String startDate, String endDate, String project, String type, String name) throws JsonProcessingException {
        DataTable<StockItem> result = new DataTable<>();
        startDate = (startDate == null) ? "1970-1-1" : startDate;
        endDate = (endDate == null) ? "1970-1-1" : endDate;
        project = (project == null) ? "" : project;
        type = (type == null) ? "" : type;
        name = (name == null) ? "" : name;
        List<StockItem> list = stockMapper.reportStockIn(startDate, endDate, project, type, name);
        Integer totalCount = 0;
        Float totalPrice = (float) 0.0;
        Integer orderCount = 0;
        for (StockItem item : list) {
            Goods goods = goodsRepository.findOne(item.getGoodsId());
            if (goods.getProject() != null) {
                Dict proj = dictRepository.findByMask(goods.getProject());
                goods.setProjectName(proj.getName());
            }
            if (goods.getType() != null) {
                Dict tp = dictRepository.findByMask(goods.getType());
                goods.setTypeName(tp.getName());
            }
            item.setGoods(goods);
            if (item.getSupplier() != null) {
                OfficialPartner partner = officialPartnerRepository.findOne(item.getSupplier());
                if (partner != null)
                    item.setSupplierName(partner.getName());
            }
            if (item.getPurchasingOrderItemId() != null) {
                PurchasingOrderItem orderItem = purchasingOrderItemRepository.findOne(item.getPurchasingOrderItemId());
                item.setOrderItem(orderItem);
                orderCount += orderItem.getCount();
            } else {
                item.setOrderItem(new PurchasingOrderItem());
            }
            StockItem i = stockItemRepository.findOne(item.getId());
            item.setStock(i.getStock());
            User user = userRepository.findOne(item.getStock().getCreatedBy());
            item.getStock().setOperator(user.getFirstName() + user.getLastName());
            totalCount += item.getCount();
            totalPrice += item.getPrice();
        }
        StockItem total = new StockItem();
        total.setCount(totalCount);
        total.setPrice(totalPrice);
        Stock o = new Stock();
        o.setCode("合计：");
        Goods goods = new Goods();
        total.setGoods(goods);
        PurchasingOrderItem orderItem = new PurchasingOrderItem();
        orderItem.setCount(orderCount);
        total.setOrderItem(orderItem);
        total.setStock(o);
        list.add(total);
        result.setDraw(draw);
        result.setRecordsFiltered((long) list.size());
        result.setRecordsTotal((long) list.size());
        result.setData(list);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(result);

    }

    @RequestMapping(value = "/project", method = RequestMethod.GET)
    public String project() {
        return "/report/project";
    }

    @RequestMapping(value = "/project", method = RequestMethod.POST)
    @ResponseBody
    public String projectList(Integer draw, String startDate, String endDate, String project, String province) throws JsonProcessingException {
        DataTable<ContractReport> result = new DataTable<>();
        startDate = (startDate == null) ? "1970-1-1" : startDate;
        endDate = (endDate == null) ? "1970-1-1" : endDate;
        project = (project == null) ? "" : project;
        province = (province == null) ? "" : province;
        List<ContractReport> list = contractMapper.getContractReportByProject(startDate, endDate, project, province);
        for (ContractReport report : list) {
            Dict dict = dictRepository.findByMask(report.getProject());
            if (dict != null)
                report.setProjectName(dict.getName());
        }
        result.setData(list);
        result.setDraw(draw);
        result.setRecordsTotal((long) list.size());
        result.setRecordsFiltered((long) list.size());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(result);
    }

    @RequestMapping(value = "/saleman", method = RequestMethod.GET)
    public String saleman() {
        return "/report/saleman";
    }

    @RequestMapping(value = "/saleman", method = RequestMethod.POST)
    @ResponseBody
    public String salemantList(Integer draw, String startDate, String endDate, String project, String province) throws JsonProcessingException {
        DataTable<ContractReport> result = new DataTable<>();
        startDate = (startDate == null) ? "1970-1-1" : startDate;
        endDate = (endDate == null) ? "1970-1-1" : endDate;
        project = (project == null) ? "" : project;
        province = (province == null) ? "" : province;
        List<ContractReport> list = contractMapper.getContractReportBySaleman(startDate, endDate, project, province);
        for (ContractReport report : list) {
            Dict dict = dictRepository.findByMask(report.getProject());
            if (dict != null)
                report.setProjectName(dict.getName());
            if (report.getCreatedBy() != null) {
                User user = userRepository.findOne(report.getCreatedBy());
                report.setCreateByName(user.getFirstName() + user.getLastName());
            }
        }
        result.setData(list);
        result.setDraw(draw);
        result.setRecordsTotal((long) list.size());
        result.setRecordsFiltered((long) list.size());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(result);
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public String status() {
        return "/report/status";
    }

    @RequestMapping(value = "/status", method = RequestMethod.POST)
    @ResponseBody
    public String statusList(Integer draw) throws JsonProcessingException {
        DataTable<Company> result = new DataTable<>();
        List<Company> companies = companyRepository.findAll();
        for (Company company : companies) {
            Contract contracts = contractRepository.findNewestContractByCompanyId(company.getId());
            if (contracts != null) {
                Dict dict = dictRepository.findByMask(contracts.getProject());
                if (dict != null)
                    contracts.setProjectName(dict.getName());
            }
            company.setContract(contracts);
            if (company.getAutoAssigned()) {
                User user = userRepository.findUserByProvince(company.getProvince());
                if (user != null)
                    company.setUser(user);
            } else {
                User user = userMapper.findUserByClientId(company.getId());
                if (user != null)
                    company.setUser(user);
            }
        }
        result.setData(companies);
        result.setDraw(draw);
        result.setRecordsTotal((long) companies.size());
        result.setRecordsFiltered((long) companies.size());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(result);
    }

    @RequestMapping(value = "/comefrom", method = RequestMethod.GET)
    public String comefrom() {
        return "/report/comefrom";
    }

    @RequestMapping(value = "/comefrom", method = RequestMethod.POST)
    @ResponseBody
    public String comfromList(Integer draw) throws JsonProcessingException {
        DataTable<Company> result = new DataTable<>();
        List<Company> companies = companyRepository.findAll();
        for (Company company : companies) {
            Contract contracts = contractRepository.findNewestContractByCompanyId(company.getId());
            if (contracts != null) {
                Dict dict = dictRepository.findByMask(contracts.getProject());
                if (dict != null)
                    contracts.setProjectName(dict.getName());
            }
            company.setContract(contracts);
            if (company.getAutoAssigned()) {
                User user = userRepository.findUserByProvince(company.getProvince());
                if (user != null)
                    company.setUser(user);
            } else {
                User user = userMapper.findUserByClientId(company.getId());
                if (user != null)
                    company.setUser(user);
            }
            User creator = userRepository.findOne(company.getCreatedBy());
            if (creator != null)
                company.setCreator(creator);
        }
        result.setData(companies);
        result.setDraw(draw);
        result.setRecordsTotal((long) companies.size());
        result.setRecordsFiltered((long) companies.size());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(result);
    }

    @RequestMapping(value = "/projectsale", method = RequestMethod.GET)
    public String productSale(){
        return "/report/projectsale";
    }

    @RequestMapping(value = "/projectsale", method = RequestMethod.POST)
    @ResponseBody
    public String productSaleList(Integer draw, String startDate, String endDate) throws JsonProcessingException{
        DataTable<SaleReport> result = new DataTable<>();
        startDate = (startDate == null) ? "1970-1-1" : startDate;
        endDate = (endDate == null) ? "1970-1-1" : endDate;
        List<SaleReport> list = orderItemMapper.getSaleReportByProject(startDate, endDate);
        Integer totalCount = 0;
        Float totalPrice = (float)0.0;
        for(SaleReport sr: list){
            Dict dict = dictRepository.findByMask(sr.getProject());
            if(dict!=null)
                sr.setProject(dict.getName());
            totalCount+=sr.getCount();
            totalPrice+=sr.getTotalPrice();
        }
        SaleReport total = new SaleReport();
        total.setProject("合计：");
        total.setCount(totalCount);
        total.setTotalPrice(totalPrice);
        list.add(total);
        result.setData(list);
        result.setDraw(draw);
        result.setRecordsTotal((long) list.size());
        result.setRecordsFiltered((long) list.size());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(result);
    }

    @RequestMapping(value = "/productsale", method = RequestMethod.GET)
    public String productsale(){
        return "/report/productsale";
    }

    @RequestMapping(value = "/productsale", method = RequestMethod.POST)
    @ResponseBody
    public String productSaleList(Integer draw, String startDate, String endDate, String project) throws JsonProcessingException{
        DataTable<SaleReport> result = new DataTable<>();
        startDate = (startDate == null) ? "1970-1-1" : startDate;
        endDate = (endDate == null) ? "1970-1-1" : endDate;
        project = (project == null) ? "" : project;
        List<SaleReport> list = orderItemMapper.getSaleReportByProduct(startDate, endDate, project);
        Integer totalCount = 0;
        Float totalPrice = (float)0.0;
        for(SaleReport sr: list){
            Dict dict = dictRepository.findByMask(sr.getProject());
            if(dict!=null)
                sr.setProject(dict.getName());
            dict = dictRepository.findByMask(sr.getType());
            if(dict!=null)
                sr.setType(dict.getName());
            Goods goods = goodsRepository.findOne(sr.getGoods());
            if(goods!=null)
                sr.setGoods(goods.getName());
            totalCount+=sr.getCount();
            totalPrice+=sr.getTotalPrice();
        }
        SaleReport total = new SaleReport();
        total.setProject("合计：");
        total.setCount(totalCount);
        total.setTotalPrice(totalPrice);
        list.add(total);
        result.setData(list);
        result.setDraw(draw);
        result.setRecordsTotal((long) list.size());
        result.setRecordsFiltered((long) list.size());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(result);
    }

    @RequestMapping(value = "/productareasale", method = RequestMethod.GET)
    public String productAreaSale(){
        return "/report/productareasale";
    }

    @RequestMapping(value = "/productareasale", method = RequestMethod.POST)
    @ResponseBody
    public String productAreaSaleList(Integer draw, String startDate, String endDate, String project, String province, String city, String district) throws JsonProcessingException{
        DataTable<SaleReport> result = new DataTable<>();
        startDate = (startDate == null) ? "1970-1-1" : startDate;
        endDate = (endDate == null) ? "1970-1-1" : endDate;
        project = (project == null) ? "" : project;
        province = (province == null) ? "" : province;
        city = (city == null) ? "" : city;
        district = (district == null) ? "" : district;
        List<SaleReport> list = orderItemMapper.getSaleReportByProductAndProvince(startDate, endDate, project, province, city, district);
        Integer totalCount = 0;
        Float totalPrice = (float)0.0;
        for(SaleReport sr: list){
            Dict dict = dictRepository.findByMask(sr.getProject());
            if(dict!=null)
                sr.setProject(dict.getName());
            dict = dictRepository.findByMask(sr.getType());
            if(dict!=null)
                sr.setType(dict.getName());
            Goods goods = goodsRepository.findOne(sr.getGoods());
            if(goods!=null)
                sr.setGoods(goods.getName());
            totalCount+=sr.getCount();
            totalPrice+=sr.getTotalPrice();
        }
        SaleReport total = new SaleReport();
        total.setProject("合计：");
        total.setCount(totalCount);
        total.setTotalPrice(totalPrice);
        list.add(total);
        result.setData(list);
        result.setDraw(draw);
        result.setRecordsTotal((long) list.size());
        result.setRecordsFiltered((long) list.size());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(result);
    }

    @RequestMapping(value = "/customersale", method = RequestMethod.GET)
    public String customerSale(){
        return "/report/customersale";
    }

    @RequestMapping(value = "/customersale", method = RequestMethod.POST)
    @ResponseBody
    public String customerSaleList(Integer draw, String startDate, String endDate, String project, String province) throws JsonProcessingException{
        DataTable<SaleReport> result = new DataTable<>();
        startDate = (startDate == null) ? "1970-1-1" : startDate;
        endDate = (endDate == null) ? "1970-1-1" : endDate;
        project = (project == null) ? "" : project;
        province = (province == null) ? "" : province;
        List<SaleReport> list = orderItemMapper.getSaleReportByCustomerAndProvince(startDate, endDate, project, province);
        Integer totalCount = 0;
        Float totalPrice = (float)0.0;
        for(SaleReport sr: list){
            Dict dict = dictRepository.findByMask(sr.getProject());
            if(dict!=null)
                sr.setProject(dict.getName());
            User user  = userRepository.findOne(sr.getCustomer());
            if(user!=null)
                sr.setCustomer(user.getFirstName()+user.getLastName());
            totalCount+=sr.getCount();
            totalPrice+=sr.getTotalPrice();
        }
        SaleReport total = new SaleReport();
        total.setProject("合计：");
        total.setCount(totalCount);
        total.setTotalPrice(totalPrice);
        list.add(total);
        result.setData(list);
        result.setDraw(draw);
        result.setRecordsTotal((long) list.size());
        result.setRecordsFiltered((long) list.size());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(result);
    }

    @RequestMapping(value = "/areasale", method = RequestMethod.GET)
    public String areaSale(){
        return "/report/areasale";
    }

    @RequestMapping(value = "/areasale", method = RequestMethod.POST)
    @ResponseBody
    public String areaSaleList(Integer draw, String startDate, String endDate, String project, String province, String city, String district, String signPerson) throws JsonProcessingException{
        DataTable<SaleReport> result = new DataTable<>();
        startDate = (startDate == null) ? "1970-1-1" : startDate;
        endDate = (endDate == null) ? "1970-1-1" : endDate;
        project = (project == null) ? "" : project;
        province = (province == null) ? "" : province;
        city = (city == null) ? "" : city;
        district = (district == null) ? "" : district;
        signPerson = (signPerson == null) ? "" : signPerson;
        List<SaleReport> list = orderItemMapper.getSaleReportByClientAndProvince(startDate, endDate, project, province, city, district, signPerson);
        Integer totalCount = 0;
        Float totalPrice = (float)0.0;
        for(SaleReport sr: list){
            Dict dict = dictRepository.findByMask(sr.getProject());
            if(dict!=null)
                sr.setProject(dict.getName());
            totalCount+=sr.getCount();
            totalPrice+=sr.getTotalPrice();
        }
        SaleReport total = new SaleReport();
        total.setProject("合计：");
        total.setCount(totalCount);
        total.setTotalPrice(totalPrice);
        list.add(total);
        result.setData(list);
        result.setDraw(draw);
        result.setRecordsTotal((long) list.size());
        result.setRecordsFiltered((long) list.size());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(result);
    }

}
