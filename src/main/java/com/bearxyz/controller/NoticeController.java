package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.Notice;
import com.bearxyz.domain.vo.NoticeVO;
import com.bearxyz.service.business.NoticeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by bearxyz on 2017/6/22.
 */
@Controller
@RequestMapping("/notice")
@SessionAttributes("notice")
public class NoticeController {

    @Autowired
    private NoticeService service;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/notice/index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @ResponseBody
    public String list(@RequestBody PaginationCriteria req) throws JsonProcessingException {
        DataTable<NoticeVO> notices = service.getByConditions(req);
        notices.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(notices);
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("notice", new Notice());
        return "/notice/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public String doCreate(@ModelAttribute Notice notice, SessionStatus status) {
        service.save(notice);
        status.setComplete();
        return "{success: true}";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable(value = "id") String id, Model model) {
        Notice notice = service.getById(id);
        model.addAttribute("notice", notice);
        return "/notice/edit";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public String doEdit(@ModelAttribute Notice notice, SessionStatus status) {
        service.save(notice);
        status.setComplete();
        return "{success: true}";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(String[] ids) {
        service.delete(ids);
        return "{success: true}";
    }

    @RequestMapping(value = "/read/{id}", method = RequestMethod.GET)
    public String read(@PathVariable(value = "id") String id, Model model) {
        Notice notice = service.getById(id);
        model.addAttribute("notice", notice);
        return "/notice/read";
    }

}
