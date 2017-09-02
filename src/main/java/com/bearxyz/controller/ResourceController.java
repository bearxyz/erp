package com.bearxyz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by bearxyz on 2017/9/2.
 */

@Controller
@RequestMapping("/resource")
public class ResourceController {

    @RequestMapping("/index")
    public String index(){
        return "/resource/index";
    }

}
