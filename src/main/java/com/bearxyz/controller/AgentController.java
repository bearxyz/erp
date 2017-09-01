package com.bearxyz.controller;

import com.bearxyz.common.ActionResponse;
import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.common.exception.NameRepeatedException;
import com.bearxyz.domain.po.sys.Role;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.repository.RoleRepository;
import com.bearxyz.service.sys.SysService;
import com.bearxyz.utility.BCrypt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


/**
 * Created by bearxyz on 2017/9/1.
 */

@Controller
@SessionAttributes("user")
@RequestMapping("/agent")
public class AgentController {

    @Autowired
    private SysService sysService;
    @Autowired
    private RoleRepository roleRepository;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(){
        return "/agent/index";
    }

    @ResponseBody
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public String getList(@RequestBody PaginationCriteria req) throws JsonProcessingException {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        DataTable<User> users = sysService.getUsersByCompanyId(user.getCompanyId(),"USER_TYPE_AGENT");
        users.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(users);
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("user", new User());
        return "/agent/create";
    }

    @ResponseBody
    @RequestMapping(value = {"/create"}, method = RequestMethod.POST)
    public String save(User user) throws Exception {
        User u = (User) SecurityUtils.getSubject().getPrincipal();
        user.setType("USER_TYPE_AGENT");
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        user.setCompanyId(u.getCompanyId());
        ActionResponse ar = new ActionResponse();
        Role r = roleRepository.findByMask("ROLE_AGENT_SEC");
        String[] role = new String[]{r.getId()};
        try {
            sysService.saveUser(user);
            sysService.assignRolesToUser(user.getId(), role);
            ar.setSuccess(true);
        }
        catch (NameRepeatedException nre){
            ar.setSuccess(false);
            ar.setMsg(nre.getMessage());
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(ar);
    }

    @ResponseBody
    @RequestMapping(value = {"/delete"}, method = RequestMethod.POST)
    public String delete(@Param("ids") String[] ids){
        sysService.deleteUsers(ids);
        return "{success: true}";
    }

    @ResponseBody
    @RequestMapping(value = {"/setStatus"}, method = RequestMethod.POST)
    public String setStatus(@RequestParam("id") String id){
        sysService.setUserStatus(id);
        return "{success: true}";
    }

    @RequestMapping(value = "/resetPwd", method = RequestMethod.GET)
    public String resetPwd(@RequestParam("id") String id, Model model){
        User user = sysService.getUserById(id);
        model.addAttribute("user", user);
        return "/agent/resetPwd";
    }

}
