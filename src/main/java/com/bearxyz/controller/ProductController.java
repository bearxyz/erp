package com.bearxyz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by bearxyz on 2017/9/2.
 */
@Controller
@RequestMapping("/product")
public class ProductController {

    @RequestMapping("/index")
    public String index(){
        return "/product/index";
    }

}
