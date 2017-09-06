package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.Company;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.mapper.CompanyMapper;
import com.bearxyz.repository.CompanyRepository;
import com.bearxyz.service.business.ClientService;
import com.bearxyz.service.sys.SysService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.List;

/**
 * Created by bearxyz on 2017/7/27.
 */
@Controller
@RequestMapping("/clientres")
@SessionAttributes("company")
@Transactional
public class ClientresController {

    @Autowired
    private SysService sysService;
    @Autowired
    private ClientService service;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CompanyMapper companyMapper;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/clientres/index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @ResponseBody
    public String list(@RequestBody PaginationCriteria req) throws JsonProcessingException {
        Subject u = SecurityUtils.getSubject();
        DataTable<Company> companys = new DataTable<>();
        if (u.hasRole("ROLE_LEASE_MANAGER")) {
            companys = service.getCompanyByConditions(null,null);
        } else if (u.hasRole("ROLE_LEASING_EXECUTIVE")) {
            List<Company> c= companyRepository.findCompaniesByUserProvinceAutoSign(((User) u.getPrincipal()).getId());
            List<Company> cc=companyRepository.findCompaniesByUserManSign(((User) u.getPrincipal()).getId());
            c.addAll(cc);
            companys.setData(c);
            companys.setRecordsFiltered((long)c.size());
            companys.setRecordsTotal((long)c.size());
        } else {
            companys = service.getCompanyByConditions(((User) u.getPrincipal()).getId(),null);
        }

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
    public String save(@ModelAttribute("company") Company company, SessionStatus status) {
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
    public String doEdit(@ModelAttribute("company") Company company, SessionStatus status) {
        service.saveCompany(company);
        status.setComplete();
        return "{success: true}";
    }

    @RequestMapping(value = "/auto", method = RequestMethod.POST)
    @ResponseBody
    public String auto(@RequestParam("id")String id) {
        Company company = service.getCompanyById(id);
        company.setAssigned(true);
        company.setAutoAssigned(true);
        service.saveCompany(company);
        return "{success: true}";
    }

    @RequestMapping(value = "/man/{id}", method = RequestMethod.GET)
    public String man(@PathVariable(value = "id") String id, Model model) {
        DataTable<User> users =sysService.getUserByRole("ROLE_LEASING_EXECUTIVE");
        model.addAttribute("users", users.getData());
        model.addAttribute("cid", id);
        return "/clientres/man";
    }

    @RequestMapping(value = "/man", method = RequestMethod.POST)
    @ResponseBody
    public String doMan(@RequestParam("uid")String uid,@RequestParam("cid")String cid) {
        Company company = service.getCompanyById(cid);
        company.setAssigned(true);
        service.saveCompany(company);
        companyMapper.assignClientToUser(cid, uid);
        return "{success: true}";
    }

    @RequestMapping(value = "/failed", method = RequestMethod.POST)
    @ResponseBody
    public String failed(@RequestParam("id")String id) {
        Company company = companyRepository.findOne(id);
        companyRepository.save(company);
        return "{success: true}";
    }

}
