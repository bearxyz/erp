package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.Warehouse;
import com.bearxyz.domain.po.sys.Dict;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.repository.UserRepository;
import com.bearxyz.repository.WarehouseRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.List;

/**
 * Created by bearxyz on 2017/9/14.
 */
@Controller
@RequestMapping("/warehouse")
@SessionAttributes("warehouse")
public class WarehouseController {

    @Autowired
    private WarehouseRepository warehouseRepository;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(){
        return "/warehouse/index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @ResponseBody
    public String getIndex(@RequestBody PaginationCriteria req) throws JsonProcessingException {
        DataTable<Warehouse> result = new DataTable<>();
        List<Warehouse> warehouses = warehouseRepository.findAll();
        for(Warehouse w: warehouses){
            User user = userRepository.findOne(w.getUserId());
            w.setUser(user.getFirstName()+user.getLastName());
        }
        result.setData(warehouses);
        result.setDraw(req.getDraw());
        result.setRecordsTotal((long)warehouses.size());
        result.setRecordsFiltered((long)warehouses.size());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(result);
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model){
        List<User> users = userRepository.findUsersByRolesMask("ROLE_DEPARTMENT_ADMINISTRATION");
        model.addAttribute("warehouse", new Warehouse());
        model.addAttribute("users", users);
        return "/warehouse/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public String doCreate(@ModelAttribute("warehouse")Warehouse warehouse, SessionStatus status){
        warehouseRepository.save(warehouse);
        return "{success: true}";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id")String id, Model model){
        Warehouse warehouse=warehouseRepository.findOne(id);
        List<User> users = userRepository.findUsersByRolesMask("ROLE_DEPARTMENT_ADMINISTRATION");
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("users", users);
        return "/warehouse/edit";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public String doEdit(@ModelAttribute("warehouse")Warehouse warehouse, SessionStatus status){
        warehouseRepository.save(warehouse);
        return "{success: true}";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(@Param("ids") String[] ids){
        for(String id: ids){
            warehouseRepository.delete(id);
        }
        return "{success: true}";
    }

}
