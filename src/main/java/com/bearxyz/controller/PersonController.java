package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.Contract;
import com.bearxyz.domain.po.business.Person;
import com.bearxyz.repository.PersonRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.List;

/**
 * Created by bearxyz on 2017/9/16.
 */
@Controller
@RequestMapping("/person")
@SessionAttributes("person")
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @RequestMapping(value = "/list/{id}/{type}", method = RequestMethod.POST)
    @ResponseBody
    public String list(@RequestBody PaginationCriteria req, @PathVariable("id")String id, @PathVariable("type")String type) throws JsonProcessingException {
        List<Person> personList = personRepository.findAllByCompanyIdAndType(id,type);
        DataTable<Person> result = new DataTable<>();
        result.setData(personList);
        result.setRecordsFiltered((long)personList.size());
        result.setRecordsTotal((long)personList.size());
        result.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(result);
    }

    @RequestMapping(value = "/create/{id}/{type}", method = RequestMethod.GET)
    public String create(@PathVariable("id")String id, @PathVariable("type")String type, Model model){
        model.addAttribute("id", id);
        model.addAttribute("type",type);
        model.addAttribute("person", new Person());
        return "/person/create";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id")String id ,Model model){
        Person person = personRepository.findOne(id);
        model.addAttribute("person",person);
        return "/person/edit";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public String save(@ModelAttribute("person") Person person, SessionStatus status){
        personRepository.save(person);
        status.setComplete();
        return "{success: true}";
    }

}
