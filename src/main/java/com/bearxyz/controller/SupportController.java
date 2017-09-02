package com.bearxyz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by bearxyz on 2017/9/2.
 */

@Controller
@RequestMapping("/support")
public class SupportController {

    @RequestMapping("/index")
    public String index(){
        return "/support/index";
    }

}
