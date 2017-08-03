package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.common.TreeNode;
import com.bearxyz.domain.po.business.Notice;
import com.bearxyz.domain.po.business.OfficialPartner;
import com.bearxyz.domain.po.sys.Dict;
import com.bearxyz.domain.vo.NoticeVO;
import com.bearxyz.service.business.OfficialPartnerService;
import com.bearxyz.service.sys.SysService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.List;

/**
 * Created by bearxyz on 2017/7/24.
 */
@Controller
@RequestMapping("/partner")
@SessionAttributes("partner")
public class OfficialPartnerController {

    @Autowired
    private OfficialPartnerService service;

    @Autowired
    private SysService sysService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/partner/index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @ResponseBody
    public String list(@RequestParam(value = "pid", required = false) String pid, @RequestParam("draw") String draw) throws JsonProcessingException {
        String mask = "";
        Dict dict = sysService.getDictById(pid);
        if(dict!=null) mask = dict.getMask();
        DataTable<OfficialPartner> partners = service.getByType(mask);
        partners.setDraw(Integer.parseInt(draw));
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(partners);
    }

    @ResponseBody
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public List<TreeNode> getTree(@RequestParam("pid") String pid) {
        if (pid.equals("#")) {
            return sysService.getDictTreeByParentMask("OFFICIAL_PARTNER_TYPE");
        } else
            return sysService.getDictTreeByParentId(pid);
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(@RequestParam("id") String id, Model model) {
        Dict dict = sysService.getDictById(id);
        model.addAttribute("type", dict.getMask());
        model.addAttribute("partner", new OfficialPartner());
        return "/partner/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public String doCreate(@ModelAttribute("partner")OfficialPartner partner, SessionStatus status) {
        service.save(partner);
        status.setComplete();
        return "{success: true}";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable(value = "id") String id, Model model) {
        OfficialPartner partner = service.getById(id);
        model.addAttribute("partner", partner);
        return "/partner/edit";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public String doEdit(@ModelAttribute("partner")OfficialPartner partner, SessionStatus status) {
        service.save(partner);
        status.setComplete();
        return "{success: true}";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(String[] ids) {
        service.delete(ids);
        return "{success: true}";
    }

}
