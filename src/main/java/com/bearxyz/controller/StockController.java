package com.bearxyz.controller;


import com.bearxyz.common.DataTable;
import com.bearxyz.domain.po.business.Goods;
import com.bearxyz.service.business.GoodsService;
import com.bearxyz.service.business.StockService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private StockService service;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(){
        return "/stock/index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @ResponseBody
    public String list(@RequestParam(value = "mask", required = false) String mask, @RequestParam("draw") String draw) throws JsonProcessingException {
        DataTable<Goods> goods = goodsService.getByNature(mask);
        goods.setDraw(Integer.parseInt(draw));
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(goods);
    }

    @RequestMapping(value = "/list/{type}", method = RequestMethod.GET)
    public String list(@PathVariable("type")String type, Model model){
        model.addAttribute("type", type);
        return "/stock/list";
    }


    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String detail(){
        return "/stock/detail";
    }

    @RequestMapping(value = "/applyIn", method = RequestMethod.GET)
    public String applyIn(){
        return "/stock/applyIn";
    }

    @RequestMapping(value = "/applyOut", method = RequestMethod.GET)
    public String applyOut(){
        return "/stock/applyOut";
    }

    @RequestMapping(value = "/scrapt", method = RequestMethod.GET)
    public String scrapt(){
        return "/stock/scrapt";
    }

}
