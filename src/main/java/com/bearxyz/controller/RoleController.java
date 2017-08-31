package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.TreeNode;
import com.bearxyz.domain.po.sys.Dict;
import com.bearxyz.domain.po.sys.Permission;
import com.bearxyz.domain.po.sys.Role;
import com.bearxyz.domain.vo.PermissionVO;
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
@SessionAttributes("role")
public class RoleController {

    @Autowired
    private SysService sysService;

    @RequestMapping(value = "/role/index", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("type", sysService.getAllDictByParentMask("ROLE_TYPE"));
        return "/role/index";
    }

    @ResponseBody
    @RequestMapping(value = "/role/index", method = RequestMethod.POST)
    public String getList(@RequestParam(value = "pid", required = false) String pid, @RequestParam("draw") String draw) throws JsonProcessingException {
        DataTable<Role> roles = sysService.getRolesByType(pid);
        roles.setDraw(Integer.parseInt(draw));
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(roles);
    }

    @ResponseBody
    @RequestMapping(value = "/role/tree", method = RequestMethod.GET)
    public List<TreeNode> getTree(@RequestParam("pid") String pid) {
        if (pid.equals("#")) {
            return sysService.getDictTreeByParentMask("ROLE_TYPE");
        } else
            return sysService.getDictTreeByParentMask(pid);
    }

    @RequestMapping(value = "/role/create", method = RequestMethod.GET)
    public String create(@RequestParam("id") String id, Model model) {
        Dict dict = sysService.getDictByMask(id);
        model.addAttribute("type", dict.getMask());
        model.addAttribute("role", new Role());
        return "/role/create";
    }

    @ResponseBody
    @RequestMapping(value = "/role/create", method = RequestMethod.POST)
    public String save(@ModelAttribute Role role, SessionStatus status) throws Exception {
        sysService.saveRole(role);
        status.setComplete();
        return "{success: true}";
    }

    @RequestMapping(value = "/role/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") String id, Model model) {
        Role role = sysService.getRoleById(id);
        model.addAttribute("role", role);
        return "/role/edit";
    }

    @ResponseBody
    @RequestMapping(value = "/role/edit", method = RequestMethod.POST)
    public String update(@ModelAttribute Role role, SessionStatus status) throws Exception {
        sysService.saveRole(role);
        status.setComplete();
        return "{success: true}";
    }

    @ResponseBody
    @RequestMapping(value = {"/role/delete"}, method = RequestMethod.POST)
    public String delete(@Param("ids") String[] ids) {
        sysService.deleteRoles(ids);
        return "{success: true}";
    }

    @RequestMapping(value = "/role/assign", method = RequestMethod.GET)
    public String assign(@RequestParam("id") String id, Model model) {
        List<PermissionVO> permissions = sysService.getAllPermission();
        Role role = sysService.getRoleById(id);
        for (PermissionVO permission : permissions) {
            for (Permission p : role.getPermissions()) {
                if (p.getId().equals(permission.getPermission().getId()))
                    permission.setHasPermission(true);
            }
            for (PermissionVO v : permission.getChildren()) {
                for (Permission p : role.getPermissions()) {
                    if (p.getId().equals(v.getPermission().getId()))
                        v.setHasPermission(true);
                }
            }
        }
        model.addAttribute("roleId", id);
        model.addAttribute("permissions", permissions);
        return "/role/assign";
    }

    @RequestMapping(value = "/role/assign", method = RequestMethod.POST)
    @ResponseBody
    public String assignPermission(@RequestParam("roleId") String roleId, @RequestParam("permission") String[] permission) {
        sysService.assignPermissionsToRole(roleId, permission);
        return "{success: true}";
    }

}
