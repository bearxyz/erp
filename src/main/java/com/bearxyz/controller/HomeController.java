package com.bearxyz.controller;

import com.bearxyz.domain.po.sys.Permission;
import com.bearxyz.domain.po.sys.Role;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.service.sys.SysService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.*;


/**
 * Created by bearxyz on 2017/5/25.
 */

@Controller
public class HomeController {

    @Autowired
    private SysService sysService;

    @RequestMapping(value = "/login.html", method = RequestMethod.GET)
    public String login() {
        return "/login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String loginAction(String username, String password, HttpSession session) {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            return "redirect:/home.html?#dashboard.html";
        } catch (Exception e) {
            return "redirect:/login.html?error";
        }
    }

    @RequestMapping(value = {"/home.html","/"})
    public String home(Model model) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<Permission> permissions = sysService.getUserPermissions(user.getId());
        model.addAttribute("permissions", permissions);
        return "/home";
    }

    @RequestMapping(value = "/dashboard.html", method = RequestMethod.GET)
    public String dashboard() {
        return "/dashboard";
    }

    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String notPermmited() {
        return "/403";
    }

    @RequestMapping(value = "/404", method = RequestMethod.GET)
    public String notFound() {
        return "/404";
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String error() {
        return "/500";
    }

}
