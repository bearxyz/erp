package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.CommunicationRecord;
import com.bearxyz.domain.po.business.Company;
import com.bearxyz.repository.CommunicationRecordRepository;
import com.bearxyz.repository.CompanyRepository;
import com.bearxyz.service.business.CommunicationRecordService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

/**
 * Created by bearxyz on 2017/8/28.
 */
@Controller
@RequestMapping("/communication")
@SessionAttributes("record")
public class CommunicationController {

    @Autowired
    private CommunicationRecordService communicationRecordService;
    @Autowired
    private CommunicationRecordRepository communicationRecordRepository;
    @Autowired
    private CompanyRepository companyRepository;


    @RequestMapping(value = "/index/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String index(@RequestBody PaginationCriteria req, @PathVariable("id")String id) throws JsonProcessingException {
        DataTable<CommunicationRecord> records = communicationRecordService.getCommunicationRecords(id,null, req);
        records.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(records);
    }

    @RequestMapping(value = "/create/{id}", method = RequestMethod.GET)
    public String createRecord(@PathVariable("id")String id, Model model) {
        model.addAttribute("companyId", id);
        model.addAttribute("record", new CommunicationRecord());
        return "/communication/create";
    }

    @ResponseBody
    @RequestMapping(value = {"/create"}, method = RequestMethod.POST)
    public String save(@ModelAttribute("record")CommunicationRecord record, SessionStatus status) {
        communicationRecordRepository.saveAndFlush(record);
        Integer count = communicationRecordRepository.countAllByCompanyIdAndSuccessed(record.getCompanyId(), false);
        if(count>=3){
            Company company = companyRepository.findOne(record.getCompanyId());
            company.setFailed(true);
            companyRepository.save(company);
        }
        status.setComplete();
        return "{success: true}";
    }

}
