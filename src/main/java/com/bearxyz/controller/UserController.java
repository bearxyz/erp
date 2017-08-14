package com.bearxyz.controller;

import com.bearxyz.common.ActionResponse;
import com.bearxyz.common.DataTable;
import com.bearxyz.common.exception.NameRepeatedException;
import com.bearxyz.domain.po.sys.Role;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.domain.vo.RoleListVO;
import com.bearxyz.domain.vo.RoleVO;
import com.bearxyz.service.sys.SysService;
import com.bearxyz.utility.BCrypt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by bearxyz on 2017/6/4.
 */

@Controller
@SessionAttributes("user")
public class UserController {

    @Autowired
    private SysService sysService;

    @RequestMapping(value = "/user/index", method = RequestMethod.GET)
    public String index(){
        return "/user/index";
    }

    @ResponseBody
    @RequestMapping(value = "/user/index", method = RequestMethod.POST)
    public String getList(@RequestParam("draw") String draw,@RequestParam("start") int start, @RequestParam("length") int length) throws JsonProcessingException {
        DataTable<User> users = sysService.getUsersByType("USER_TYPE_SYSTEM",start,length);
        users.setDraw(Integer.parseInt(draw));
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(users);
    }

    @RequestMapping(value = "/user/create", method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("user", new User());
        return "/user/create";
    }

    @ResponseBody
    @RequestMapping(value = {"/user/create"}, method = RequestMethod.POST)
    public String save(User user) throws Exception {
        user.setType("USER_TYPE_SYSTEM");
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        ActionResponse ar = new ActionResponse();
        try {
            sysService.saveUser(user);
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
    @RequestMapping(value = {"/user/delete"}, method = RequestMethod.POST)
    public String delete(@Param("ids") String[] ids){
        sysService.deleteUsers(ids);
        return "{success: true}";
    }

    @ResponseBody
    @RequestMapping(value = {"/user/setStatus"}, method = RequestMethod.POST)
    public String setStatus(@RequestParam("id") String id){
        sysService.setUserStatus(id);
        return "{success: true}";
    }

    @RequestMapping(value = "/user/resetPwd", method = RequestMethod.GET)
    public String resetPwd(@RequestParam("id") String id, Model model){
        User user = sysService.getUserById(id);
        model.addAttribute("user", user);
        return "/user/resetPwd";
    }

    @RequestMapping(value = "/user/assign", method = RequestMethod.GET)
    public String assign(@RequestParam("id") String id, Model model){
        List<RoleListVO> vo = sysService.getRoles();
        User user = sysService.getUserById(id);
        for(RoleListVO list: vo){
            for(RoleVO rvo: list.getList()){
                for(Role r: user.getRoles()){
                    if(r.getId().equals(rvo.getId()))
                        rvo.setHasRole(true);
                }
            }
        }
        model.addAttribute("userId", id);
        model.addAttribute("roles", vo);
        return "/user/assign";
    }

    @RequestMapping(value = "/user/assign", method = RequestMethod.POST)
    @ResponseBody
    public String assignPermission(@RequestParam("userId") String userId, @RequestParam("role") String[] role) {
        sysService.assignRolesToUser(userId, role);
        return "{success: true}";
    }

}
