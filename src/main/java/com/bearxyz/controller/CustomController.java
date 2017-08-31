package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.domain.po.sys.Dict;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.mapper.UserMapper;
import com.bearxyz.service.sys.SysService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by bearxyz on 2017/8/30.
 */
@Controller
@RequestMapping("/custom")
public class CustomController {

    @Autowired
    private SysService sysService;

    @Autowired
    private UserMapper userMapper;

    @RequestMapping(value = "/chooseprovince/{id}")
    public String chooseProvince(@PathVariable("id")String id, Model model){
        List<Dict> provinces = sysService.getAllDictByParentMask("ADMINISTRATIVE_AREA");
        List<String> p = userMapper.findProvinceByUser(id);
        for(Dict province: provinces){
            for(String pl: p){
                if(pl.equals(province.getName()))
                    province.setHas(true);
            }
        }
        model.addAttribute("uid", id);
        model.addAttribute("provinces", provinces);
        return "/custom/chooseprovince";
    }

    @RequestMapping(value = "/assign", method = RequestMethod.POST)
    @ResponseBody
    public String assign(@RequestParam("uid") String uid, @RequestParam("province") String[] province) {
        userMapper.deleteProvinceByUser(uid);
        for(String p: province){
            userMapper.saveUserProvince(uid, p);
        }
        return "{success: true}";
    }


    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(){
        return "/custom/index";
    }

    @ResponseBody
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public String getList(@Param("draw")String draw) throws JsonProcessingException {
        DataTable<User> users =sysService.getUserByRole("ROLE_SERVICE_EXECUTIVE");
        users.setDraw(Integer.parseInt(draw));
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(users);
    }

}
