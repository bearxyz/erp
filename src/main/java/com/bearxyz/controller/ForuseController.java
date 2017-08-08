package com.bearxyz.controller;

import com.bearxyz.domain.po.business.ForUse;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.service.business.ForUseService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

/**
 * Created by bearxyz on 2017/8/2.
 */

@Controller
@RequestMapping("/foruse")
@SessionAttributes("foruse")
public class ForuseController {

    @Autowired
    private ForUseService service;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(){
        return "/foruse/index";
    }

    @RequestMapping(value = "/apply", method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("foruse", new ForUse());
        return "/foruse/apply";
    }

    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    @ResponseBody
    public String doCreate(@ModelAttribute("foruse")ForUse forUse, SessionStatus status) {
        forUse.setUserId(((User) SecurityUtils.getSubject().getPrincipal()).getId());
        service.apply(forUse);
        status.setComplete();
        return "{success: true}";
    }

}
