package com.bearxyz.controller;

import com.bearxyz.domain.po.business.ForUse;
import com.bearxyz.service.business.ForUseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

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

}
