package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.Select;
import com.bearxyz.common.TreeNode;
import com.bearxyz.domain.po.business.Goods;
import com.bearxyz.domain.po.sys.Dict;
import com.bearxyz.service.business.GoodsService;
import com.bearxyz.service.sys.SysService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @Autowired
    private GoodsService goodsService;

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

    @ResponseBody
    @RequestMapping(value = "/getDictTree", method = RequestMethod.GET)
    public List<TreeNode> getTree(@RequestParam("mask") String mask) {
        List<TreeNode> treeNodeList = sysService.getDictTreeByParentMask(mask);
        return treeNodeList;
    }

    @RequestMapping(value = "/selectProduct", method = RequestMethod.GET)
    public String selectProduct(){
        return "/common/selectProduct";
    }

    @RequestMapping(value = "/selectProduct", method = RequestMethod.POST)
    @ResponseBody
    public String selectProductList(@RequestParam(value = "mask", required = false) String mask, @RequestParam("draw") String draw) throws JsonProcessingException {
        DataTable<Goods> goods = goodsService.getByNature(mask);
        goods.setDraw(Integer.parseInt(draw));
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(goods);
    }

    @RequestMapping(value = "/listProduct", method = RequestMethod.POST)
    @ResponseBody
    public String getProduct(String[] ids) throws JsonProcessingException {
        List<Goods> goods = goodsService.getByIds(ids);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(goods);
    }

}
