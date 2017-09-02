package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.Goods;
import com.bearxyz.repository.GoodsRepository;
import com.bearxyz.service.business.GoodsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;


/**
 * Created by bearxyz on 2017/7/25.
 */
@Controller
@RequestMapping("/goods")
@SessionAttributes("goods")
public class GoodsController {

    @Autowired
    private GoodsService service;

    @Autowired
    private GoodsRepository repository;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/goods/index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @ResponseBody
    public String list(@RequestBody PaginationCriteria req) throws JsonProcessingException {
        DataTable<Goods> goods = service.getGoods();
        goods.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(goods);
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("goods", new Goods());
        return "/goods/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public String doCreate(@ModelAttribute("goods")Goods goods, SessionStatus status) {
        service.save(goods);
        status.setComplete();
        return "{success: true}";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable(value = "id") String id, Model model) {
        Goods goods = service.getById(id);
        model.addAttribute("goods", goods);
        return "/goods/edit";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public String doEdit(@ModelAttribute("goods")Goods goods, SessionStatus status) {
        service.save(goods);
        status.setComplete();
        return "{success: true}";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(String[] ids) {
        service.delete(ids);
        return "{success: true}";
    }

    @RequestMapping(value = "/delPak", method = RequestMethod.POST)
    @ResponseBody
    public String delete(String id) {
        service.deletePackageById(id);
        return "{success: true}";
    }

}
