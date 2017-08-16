package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.ForUse;
import com.bearxyz.domain.po.business.Purchasing;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.service.business.PurchasingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

/**
 * Created by bearxyz on 2017/8/16.
 */
@Controller
@RequestMapping("/purchasing")
@SessionAttributes("purchasing")
public class PurchasingController {

    @Autowired
    private PurchasingService service;

    @RequestMapping(value = "/project", method = RequestMethod.GET)
    public String project(){
        return "/purchasing/project";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(){
        return "/purchasing/index";
    }

    @ResponseBody
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public String getList(@RequestBody PaginationCriteria req) throws JsonProcessingException {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        DataTable<Purchasing> purchasing = service.getForUse(user.getId(), req);
        purchasing.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(purchasing);
    }

    @RequestMapping(value = "/apply", method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("purchasing", new Purchasing());
        return "/purchasing/apply";
    }

    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    @ResponseBody
    public String doCreate(@ModelAttribute("purchasing")Purchasing forUse, SessionStatus status) {
        service.apply(forUse);
        status.setComplete();
        return "{success: true}";
    }

}
