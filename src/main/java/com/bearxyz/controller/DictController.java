package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.TreeNode;
import com.bearxyz.domain.po.sys.Dict;
import com.bearxyz.service.sys.SysService;
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
 * Created by bearxyz on 2017/6/6.
 */

@Controller
@SessionAttributes("dict")
public class DictController {

    @Autowired
    private SysService sysService;

    @RequestMapping(value = "/dict/index", method = RequestMethod.GET)
    public String index() {
        return "/dict/index";
    }

    @ResponseBody
    @RequestMapping(value = "/dict/index", method = RequestMethod.POST)
    public String getList(@RequestParam("pid") String pid, @RequestParam("draw") String draw, @RequestParam("start") int start, @RequestParam("length") int length) throws JsonProcessingException {
        DataTable<Dict> dict = sysService.getDictsByParentId(pid, start, length);
        dict.setDraw(Integer.parseInt(draw));
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(dict);
    }

    @ResponseBody
    @RequestMapping(value = "/dict/tree", method = RequestMethod.GET)
    public List<TreeNode> getTree(@RequestParam("pid") String pid) {
        if (pid.equals("#")) pid = " ";
        List<TreeNode> treeNodeList = sysService.getDictTreeByParentId(pid);
        return treeNodeList;
    }

    @RequestMapping(value = "/dict/create", method = RequestMethod.GET)
    public String create(@RequestParam(value = "pid", required = false) String pid, Model model) {
        model.addAttribute("pid", pid);
        model.addAttribute("dict", new Dict());
        return "/dict/create";
    }

    @ResponseBody
    @RequestMapping(value = {"/dict/create"}, method = RequestMethod.POST)
    public String save(@ModelAttribute("dict")Dict dict, SessionStatus status) {
        sysService.saveDict(dict);
        status.setComplete();
        return "{success: true}";
    }

    @RequestMapping(value = "/dict/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") String id, Model model) {
        Dict dict = sysService.getDictById(id);
        model.addAttribute("dict", dict);
        return "/dict/edit";
    }

    @ResponseBody
    @RequestMapping(value = "/dict/edit", method = RequestMethod.POST)
    public String update(@ModelAttribute("dict")Dict dict, SessionStatus status) throws Exception {
        sysService.saveDict(dict);
        status.setComplete();
        return "{success: true}";
    }

    @ResponseBody
    @RequestMapping(value = {"/dict/delete"}, method = RequestMethod.POST)
    public String delete(@Param("ids") String[] ids) {
        sysService.deleteDicts(ids);
        return "{success: true}";
    }

}
