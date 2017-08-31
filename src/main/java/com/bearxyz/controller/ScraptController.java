package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.Scrapt;
import com.bearxyz.domain.po.business.ScraptItem;
import com.bearxyz.service.business.ScraptService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

/**
 * Created by bearxyz on 2017/8/28.
 */
@Controller
@RequestMapping("/scrapt")
@SessionAttributes("scrapt")
public class ScraptController {

    @Autowired
    private ScraptService service;

    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/scrapt/index";
    }

    @ResponseBody
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public String getList(@RequestBody PaginationCriteria req) throws JsonProcessingException {
        DataTable<ScraptItem> scrapt = service.getScraptItems(false, req);
        scrapt.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(scrapt);
    }

    @RequestMapping(value = "/apply", method = RequestMethod.GET)
    public String apply(Model model) {
        model.addAttribute("scrapt", new Scrapt());
        return "/scrapt/apply";
    }

    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    @ResponseBody
    public String doApply(@ModelAttribute("scrapt")Scrapt scrapt, SessionStatus status) {
        service.apply(scrapt);
        status.setComplete();
        return "{success: true}";
    }

}
