package com.bearxyz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * Created by bearxyz on 2017/7/27.
 */

@Controller
@RequestMapping("/contract")
@SessionAttributes("contract")
public class ContractController {

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/contract/index";
    }

}
