package com.bearxyz.controller;

import com.bearxyz.common.ActionResponse;
import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.common.exception.NameRepeatedException;
import com.bearxyz.domain.po.business.Company;
import com.bearxyz.domain.po.business.Contract;
import com.bearxyz.domain.po.sys.Dict;
import com.bearxyz.domain.po.sys.Role;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.repository.CompanyRepository;
import com.bearxyz.repository.ContractRepository;
import com.bearxyz.repository.RoleRepository;
import com.bearxyz.repository.UserRepository;
import com.bearxyz.service.business.ClientService;
import com.bearxyz.service.sys.SysService;
import com.bearxyz.utility.BCrypt;
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
@RequestMapping("/client")
@SessionAttributes("company")
@Transactional
public class ClientController {

    @Autowired
    private ClientService service;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SysService sysService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ContractRepository contractRepository;

    @RequestMapping(value = "/setAccount/{cid}", method = RequestMethod.GET)
    public String setAccount(@PathVariable("cid") String cid, Model model) {
        model.addAttribute("cid", cid);
        return "/client/setAccount";
    }

    @RequestMapping(value = "/setAccount", method = RequestMethod.POST)
    @ResponseBody
    public String doSetAccount(@RequestParam("cid") String cid, @RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName, Model model) throws JsonProcessingException {
        Company company = companyRepository.findOne(cid);
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setType("USER_TYPE_END");
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        user.setEnabled(true);
        user.setCompanyId(cid);
        ActionResponse ar = new ActionResponse();
        Role r = roleRepository.findByMask("ROLE_AGENT");
        String[] role = new String[]{r.getId()};
        try {
            sysService.saveUser(user);
            sysService.assignRolesToUser(user.getId(), role);
            ar.setSuccess(true);
        } catch (NameRepeatedException nre) {
            ar.setSuccess(false);
            ar.setMsg(nre.getMessage());
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(ar);
    }


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
        } else {
            List<Company> c = companyRepository.findCompaniesByUserProvince(((User) u.getPrincipal()).getId());
            companys.setData(c);
            companys.setRecordsFiltered((long) c.size());
            companys.setRecordsTotal((long) c.size());
        }
        for (Company c : companys.getData()) {
            Integer count = userRepository.countUsersByCompanyId(c.getId());
            if (count > 0) {
                c.setHasAccount(true);
                User user = userRepository.findUserByCompanyIdAndRole(c.getId(),"ROLE_AGENT");
                c.setUser(user);
            } else
                c.setHasAccount(false);
            Contract contract = contractRepository.findNewestContractByCompanyId(c.getId());
            if (contract != null) {
                c.setCurrentStartDate(contract.getStartDate());
                c.setCurrentEndDate(contract.getEndDate());
            }
            c.setFullAddress(c.getProvince() + c.getCity() + c.getDistrict() + c.getAddress());
            List<Contract> contracts = contractRepository.findAvaliableContractByCompanyId(c.getId());
            c.setContracts(contracts);
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

    @RequestMapping(value = "/benefit", method = RequestMethod.GET)
    public String benefit() {
        return "/client/benefit";
    }

    @RequestMapping(value = "/getclientlevel", method = RequestMethod.POST)
    @ResponseBody
    public String getClientLevel(@RequestBody PaginationCriteria req) throws JsonProcessingException {
        List<Dict> dicts = sysService.getAllDictByParentMask("CLIENT_LEVEL_TYPE");
        DataTable<Dict> result = new DataTable<>();
        result.setData(dicts);
        result.setRecordsFiltered((long) dicts.size());
        result.setRecordsTotal((long) dicts.size());
        result.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(result);
    }

    @RequestMapping(value = "/setBenefit/{id}", method = RequestMethod.GET)
    public String setBenefit(@PathVariable("id") String id, Model model) {
        Dict dict = sysService.getDictById(id);
        model.addAttribute("dict", dict);
        return "/client/setBenefit";
    }

    @RequestMapping(value = "/setBenefit", method = RequestMethod.POST)
    @ResponseBody
    public String doBenefit(String id, Float discount) {
        Dict dict = sysService.getDictById(id);
        dict.setDiscount(discount);
        sysService.saveDict(dict);
        return "{success: true}";
    }

    @RequestMapping(value = "/resetPwd/{id}", method = RequestMethod.GET)
    public String resetPwd(@PathVariable("id") String id, Model model) {
        model.addAttribute("id", id);
        return "/client/resetPwd";
    }

    @RequestMapping(value = "/resetPwd", method = RequestMethod.POST)
    @ResponseBody
    public String doResetPwd(String id, String password) {
        User user = userRepository.findUserByCompanyIdAndRole(id, "ROLE_AGENT");
        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        userRepository.save(user);
        return "{success: true}";
    }

    @RequestMapping(value = "/setStatus", method = RequestMethod.POST)
    @ResponseBody
    public String setStatus(String id){
        User u = userRepository.findUserByCompanyIdAndRole(id, "ROLE_AGENT");
        List<User> users = userRepository.findAllByCompanyIdOrParentCompanyId(id,id);
        for(User user: users){
            user.setEnabled(!u.getEnabled());
        }
        return "{success: true}";
    }

}
