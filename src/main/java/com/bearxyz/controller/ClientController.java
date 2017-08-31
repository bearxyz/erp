package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.Company;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.repository.CompanyRepository;
import com.bearxyz.service.business.ClientService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.List;

/**
 * Created by bearxyz on 2017/7/27.
 */

@Controller
@RequestMapping("/client")
@SessionAttributes("company")
public class ClientController {

    @Autowired
    private ClientService service;

    @Autowired
    private CompanyRepository companyRepository;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/client/index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @ResponseBody
    public String list(@RequestBody PaginationCriteria req) throws JsonProcessingException {
        Subject u = SecurityUtils.getSubject();
        DataTable<Company> companys = new DataTable<>();
        if (u.hasRole("ROLE_SERVICE_MANAGER")) {
            companys = service.getCompanyByConditions(null, true);
        }
        else {
            List<Company> c= companyRepository.findCompaniesByUserProvince(((User) u.getPrincipal()).getId());
            companys.setData(c);
            companys.setRecordsFiltered((long)c.size());
            companys.setRecordsTotal((long)c.size());
        }
        companys.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(companys);
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable(value = "id") String id, Model model) {
        Company company = service.getCompanyById(id);
        model.addAttribute("company", company);
        return "/client/edit";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public String doEdit(@ModelAttribute("company") Company company, SessionStatus status) {
        service.saveCompany(company);
        status.setComplete();
        return "{success: true}";
    }

}
