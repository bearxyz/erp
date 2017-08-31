package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.TreeNode;
import com.bearxyz.domain.po.sys.Permission;
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
 * Created by bearxyz on 2017/6/4.
 */

@Controller
@RequestMapping("/permission")
@SessionAttributes("permission")
public class PermissionController {

    @Autowired
    private SysService sysService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {

        return "/permission/index";
    }

    @ResponseBody
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public String getList(@RequestParam(value = "pid", required = false) String pid, @RequestParam("draw") String draw) throws JsonProcessingException {
        DataTable<Permission> permissions = sysService.getPermissionActions(pid);
        permissions.setDraw(Integer.parseInt(draw));
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(permissions);
    }

    @ResponseBody
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public List<TreeNode> getTree() {
        return sysService.getPermissionTree();
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(@RequestParam(value = "pid", required = false) String pid, @RequestParam("type") String type, Model model) {
        model.addAttribute("pid", pid);
        model.addAttribute("type", type);
        model.addAttribute("permission", new Permission());
        return "/permission/create";
    }

    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String save(@ModelAttribute Permission permission, SessionStatus status) throws Exception {
        sysService.savePermission(permission);
        status.setComplete();
        return "{success: true}";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") String id, Model model) {
        Permission permission = sysService.getPermissionById(id);
        model.addAttribute("type", sysService.getAllDictByParentMask("PERMISSION_TYPE"));
        model.addAttribute("permission", permission);
        return "/permission/edit";
    }

    @ResponseBody
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String update(@ModelAttribute Permission permission, SessionStatus status) throws Exception {
        sysService.savePermission(permission);
        status.setComplete();
        return "{success: true}";
    }

    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(@Param("ids") String[] ids) {
        sysService.deletePermissions(ids);
        return "{success: true}";
    }

    @ResponseBody
    @RequestMapping(value = "/move", method = RequestMethod.POST)
    public String move(@Param("id") String id, @Param("position") Integer position, @Param("old_position")Integer old_position) {
        sysService.movePermission(id, position, old_position);
        return "{success: true}";
    }

}
