package com.bearxyz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * Created by bearxyz on 2017/7/26.
 */
@Controller
@RequestMapping("/package")
@SessionAttributes("package")
public class PackageController {

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/package/index";
    }

}
