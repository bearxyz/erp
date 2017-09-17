package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.Purchasing;
import com.bearxyz.domain.po.business.QA;
import com.bearxyz.domain.po.sys.Dict;
import com.bearxyz.domain.vo.QaVo;
import com.bearxyz.repository.DictRepository;
import com.bearxyz.repository.QARepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bearxyz on 2017/9/10.
 */
@Controller
@RequestMapping("/qa")
@SessionAttributes("qa")
public class QAController {

    @Autowired
    private QARepository qaRepository;
    @Autowired
    private DictRepository dictRepository;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/qa/index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @ResponseBody
    public String getIndex(@RequestBody PaginationCriteria req) throws JsonProcessingException {
        DataTable<QA> result = new DataTable<>();
        List<QA> qas = qaRepository.findAll();
        result.setData(qas);
        result.setRecordsFiltered((long) qas.size());
        result.setRecordsTotal((long) qas.size());
        result.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(result);
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("qa", new QA());
        return "/qa/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public String doCreate(@ModelAttribute("qa") QA qa, SessionStatus status) {
        qaRepository.save(qa);
        status.setComplete();
        return "{success:true}";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id")String id, Model model) {
        QA qa = qaRepository.getOne(id);
        model.addAttribute("qa", qa);
        return "/qa/edit";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public String doEdit(@ModelAttribute("qa") QA qa, SessionStatus status) {
        qaRepository.save(qa);
        status.setComplete();
        return "{success:true}";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(@Param("ids") String[] ids){
        for(String id:ids){
            qaRepository.delete(id);
        }
        return "{success:true}";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        List<QaVo> result = new ArrayList<>();
        List<Dict> dicts =dictRepository.findAllByParentMask("WORK_ORDER_TYPE");
        for(Dict dict:dicts){
            QaVo vo = new QaVo();
            vo.setType(dict.getName());
            List<QA> qas = qaRepository.findAllByType(dict.getMask());
            for(QA qa:qas){
                vo.getQas().add(qa);
            }
            result.add(vo);
        }
        model.addAttribute("list", result);
        return "/qa/list";
    }

}
