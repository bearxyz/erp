package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.Company;
import com.bearxyz.domain.po.business.Contract;
import com.bearxyz.domain.po.business.Goods;
import com.bearxyz.domain.po.business.Present;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.repository.CompanyRepository;
import com.bearxyz.repository.ContractRepository;
import com.bearxyz.repository.GoodsRepository;
import com.bearxyz.repository.UserRepository;
import com.bearxyz.service.business.ContractService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

/**
 * Created by bearxyz on 2017/7/27.
 */

@Controller
@RequestMapping("/contract")
@SessionAttributes("contract")
public class ContractController {

    @Autowired
    private ContractService service;
    @Autowired
    private TaskService taskService;
    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/complete")
    public String complete(@RequestParam("bid") String bid, @RequestParam("tid") String tid, @RequestParam("applyer") String applyer, Model model) {
        Contract purchasing = service.getById(bid);
        for (Present present : purchasing.getItems()) {
            Goods goods = goodsRepository.findOne(present.getGoodsId());
            present.setGoods(goods);
        }
        String memo = "";
        model.addAttribute("contract", purchasing);
        model.addAttribute("applyer", applyer);
        Task task = taskService.createTaskQuery().taskId(tid).singleResult();
        if (!task.getTaskDefinitionKey().equals("deptLeader") && taskService.getVariable(task.getId(), "deptLeaderMemo") != null)
            memo = taskService.getVariable(task.getId(), "deptLeaderMemo").toString();
        model.addAttribute("taskId", tid);
        model.addAttribute("taskKey", task.getTaskDefinitionKey());
        model.addAttribute("memo", memo);
        return "/contract/complete";
    }

    @RequestMapping(value = "/index/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String index(@RequestBody PaginationCriteria req, @PathVariable("id") String id) throws JsonProcessingException {
        DataTable<Contract> records = service.getContacts(id, req);
        records.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(records);
    }

    @RequestMapping(value = "/create/{id}", method = RequestMethod.GET)
    public String createRecord(@PathVariable("id") String id, Model model) {
        Company company = companyRepository.findOne(id);
        model.addAttribute("company", company);
        model.addAttribute("companyId", id);
        model.addAttribute("contract", new Contract());
        return "/contract/create";
    }

    @ResponseBody
    @RequestMapping(value = {"/create"}, method = RequestMethod.POST)
    public String save(@ModelAttribute("contract") Contract record, SessionStatus status, HttpServletRequest request) throws IOException {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("attachment");
        service.apply(record, files);
        status.setComplete();
        return "{success: true}";
    }

    @RequestMapping(value = "/reApply/{id}", method = RequestMethod.GET)
    public String reApply(@PathVariable("id") String id, Model model) {
        Task task = taskService.createTaskQuery().processInstanceBusinessKey(id).singleResult();
        Contract contract = service.getById(id);
        for (Present present : contract.getItems()) {
            Goods goods = goodsRepository.findOne(present.getGoodsId());
            present.setGoods(goods);
        }
        model.addAttribute("companyId", id);
        model.addAttribute("contract", contract);
        model.addAttribute("taskId", task.getId());
        return "/contract/reApply";
    }

    @RequestMapping(value = "/reApply", method = RequestMethod.POST)
    @ResponseBody
    public String doReApply(@ModelAttribute("contract") Contract contract, SessionStatus status, HttpServletRequest request) throws IOException {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("attachment");
        service.save(contract, files);
        status.setComplete();
        return "{success: true}";
    }

    @RequestMapping(value = "/list/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String list(@RequestBody PaginationCriteria req, @PathVariable("id")String id) throws JsonProcessingException {
        DataTable<Contract> contractDataTable = new DataTable<>();
        List<Contract> contracts = contractRepository.findAvaliableContractByCompanyId(id);
        contractDataTable.setData(contracts);
        contractDataTable.setRecordsTotal((long)contracts.size());
        contractDataTable.setRecordsFiltered((long)contracts.size());
        contractDataTable.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(contractDataTable);
    }

    @RequestMapping(value = "/editproject/{id}", method = RequestMethod.GET)
    public String editProject(@PathVariable("id")String id, Model model){
        Contract contract = contractRepository.findOne(id);
        model.addAttribute("contract",contract);
        return "/contract/editproject";
    }

    @RequestMapping(value = "/editproject", method = RequestMethod.POST)
    @ResponseBody
    public String doEditProject(@ModelAttribute("contract") Contract contract, SessionStatus status) throws IOException {
        contractRepository.save(contract);
        status.setComplete();
        return "{success: true}";
    }

    @RequestMapping(value = "/seccontract/{id}", method = RequestMethod.GET)
    public String seccontract(@PathVariable("id")String id, Model model){
        model.addAttribute("companyId", id);
        model.addAttribute("contract", new Contract());
        return "/agent/contract";
    }

    @RequestMapping(value = "/seccontract", method = RequestMethod.POST)
    public String doseccontract(@ModelAttribute("contract") Contract contract, SessionStatus status, HttpServletRequest request) throws IOException {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("attachment");
        contract.setSecond(true);
        service.save(contract, files);
        return "/agent/contract";
    }

    @RequestMapping(value = "/sec", method = RequestMethod.GET)
    public String sec(){
        return "/contract/sec";
    }

    @RequestMapping(value = "/sec", method = RequestMethod.POST)
    @ResponseBody
    public String dosec(@RequestBody PaginationCriteria req) throws JsonProcessingException {
        List<Contract> contracts = contractRepository.findAllBySecond(true);
        DataTable<Contract> result = new DataTable<>();
        result.setData(contracts);
        result.setRecordsTotal((long)contracts.size());
        result.setRecordsFiltered((long)contracts.size());
        result.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(result);
    }

    @RequestMapping(value = "/accept", method = RequestMethod.POST)
    @ResponseBody
    public String accept(String id){
        Contract contract = contractRepository.findOne(id);
        contract.setApproved(true);
        contractRepository.save(contract);
        User user = userRepository.findOne(contract.getCompanyId());
        user.setEnabled(true);
        user.setPassed(true);
        userRepository.save(user);
        return "{success: true}";
    }

    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    @ResponseBody
    public String pay(String id, Date recieveMoneyDate, String recieveMoneyBank, Float recieveMoney, String contractCode){
        Contract contract = contractRepository.findOne(id);
        contract.setRecieveMoneyDate(recieveMoneyDate);
        contract.setRecieveMoney(recieveMoney);
        contract.setRecieveMoneyBank(recieveMoneyBank);
        contract.setContractCode(contractCode);
        contractRepository.save(contract);
        return "{success: true}";
    }

}
