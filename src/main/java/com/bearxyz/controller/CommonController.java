package com.bearxyz.controller;

import com.bearxyz.common.Select;
import com.bearxyz.domain.po.sys.Dict;
import com.bearxyz.service.sys.SysService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bearxyz on 2017/6/23.
 */
@Controller
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private SysService sysService;

    @RequestMapping(value = "/getDict")
    @ResponseBody
    public String getDict(@RequestParam("mask") String mask) throws JsonProcessingException {
        List<Dict> dicts = sysService.getAllDictByParentMask(mask);
        List<Select> selects = new ArrayList<>();
        for(Dict dict:dicts){
            Select select = new Select();
            select.setId(dict.getMask());
            select.setText(dict.getName());
            selects.add(select);
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(selects);
    }

}
