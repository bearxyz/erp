package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.CommunicationRecord;
import com.bearxyz.domain.po.business.ReturnVisit;
import com.bearxyz.repository.ReturnVisitRepository;
import com.bearxyz.service.business.ReturnVisitService;
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
@RequestMapping("/returnvisit")
@SessionAttributes("visit")
public class ReturnVisitController {

    @Autowired
    private ReturnVisitService service;
    @Autowired
    private ReturnVisitRepository repository;

    @RequestMapping(value = "/index/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String index(@RequestBody PaginationCriteria req, @PathVariable("id") String id) throws JsonProcessingException {
        DataTable<ReturnVisit> records = service.getReturnVisits(id, null, req);
        records.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(records);
    }

    @RequestMapping(value = "/create/{id}", method = RequestMethod.GET)
    public String createRecord(@PathVariable("id") String id, Model model) {
        model.addAttribute("companyId", id);
        model.addAttribute("visit", new ReturnVisit());
        return "/returnvisit/create";
    }

    @ResponseBody
    @RequestMapping(value = {"/create"}, method = RequestMethod.POST)
    public String save(@ModelAttribute("visit") ReturnVisit record, SessionStatus status) {
        service.save(record);
        status.setComplete();
        return "{success: true}";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String editRecord(@PathVariable("id") String id, Model model) {
        ReturnVisit returnVisit = repository.findOne(id);
        model.addAttribute("companyId", id);
        model.addAttribute("visit", returnVisit);
        return "/returnvisit/edit";
    }

    @ResponseBody
    @RequestMapping(value = {"/edit"}, method = RequestMethod.POST)
    public String update(@ModelAttribute("visit") ReturnVisit record, SessionStatus status) {
        service.save(record);
        status.setComplete();
        return "{success: true}";
    }

}
