package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.Company;
import com.bearxyz.domain.po.business.Notice;
import com.bearxyz.service.business.ClientService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

/**
 * Created by bearxyz on 2017/7/27.
 */
@Controller
@RequestMapping("/clientres")
@SessionAttributes("company")
public class ClientresController {

    @Autowired
    private ClientService service;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/clientres/index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @ResponseBody
    public String list(@RequestBody PaginationCriteria req) throws JsonProcessingException {
        DataTable<Company> companys = service.getCompanyByConditions(req);
        companys.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(companys);
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("company", new Company());
        return "/clientres/create";
    }

    @ResponseBody
    @RequestMapping(value = {"/create"}, method = RequestMethod.POST)
    public String save(@ModelAttribute("company")Company company, SessionStatus status) {
        service.saveCompany(company);
        status.setComplete();
        return "{success: true}";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable(value = "id") String id, Model model) {
        Company company = service.getCompanyById(id);
        model.addAttribute("company", company);
        return "/clientres/edit";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public String doEdit(@ModelAttribute("company")Company company, SessionStatus status) {
        service.saveCompany(company);
        status.setComplete();
        return "{success: true}";
    }

}
