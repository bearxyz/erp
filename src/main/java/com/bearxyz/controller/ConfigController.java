package com.bearxyz.controller;

import com.bearxyz.domain.po.business.Config;
import com.bearxyz.repository.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.List;

/**
 * Created by bearxyz on 2017/9/3.
 */
@Controller
@RequestMapping("/config")
@SessionAttributes("config")
public class ConfigController {

    @Autowired
    private ConfigRepository repository;

    @RequestMapping(value="/index", method = RequestMethod.GET)
    public String index(Model model){
        Config config = new Config();
        List<Config> tmp = repository.findAll();
        if(tmp.size()>0) config = tmp.get(0);
        model.addAttribute("config", config);
        return "/config/index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @ResponseBody
    public String update(@ModelAttribute("config")Config config, SessionStatus status){
        repository.save(config);
        status.setComplete();
        return "{success: true}";
    }

}
