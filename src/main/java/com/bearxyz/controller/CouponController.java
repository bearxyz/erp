package com.bearxyz.controller;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.Coupon;
import com.bearxyz.domain.po.business.CouponActivity;
import com.bearxyz.repository.CouponActivityRepository;
import com.bearxyz.repository.CouponRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


/**
 * Created by bearxyz on 2017/9/7.
 */
@Controller
@RequestMapping("/coupon")
@Transactional
public class CouponController {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponActivityRepository couponActivityRepository;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(){
        return "/coupon/index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @ResponseBody
    public String doIndex(@RequestBody PaginationCriteria req) throws JsonProcessingException {
        DataTable<CouponActivity> list = new DataTable<>();
        List<CouponActivity> couponActivities = couponActivityRepository.findAll();
        for(CouponActivity activity: couponActivities){
            activity.setUsed(couponRepository.countAllByActivityIdAndUsed(activity.getId(),true));
        }
        list.setRecordsTotal((long)couponActivities.size());
        list.setRecordsFiltered((long)couponActivities.size());
        list.setData(couponActivities);
        list.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(list);
    }

    @RequestMapping(value = "/list/{aid}", method = RequestMethod.GET)
    public String list(@PathVariable("aid")String aid, Model model){
        model.addAttribute("aid", aid);
        return "/coupon/list";
    }

    @RequestMapping(value = "/list/{aid}", method = RequestMethod.POST)
    @ResponseBody
    public String doList(@PathVariable("aid")String aid,@RequestBody PaginationCriteria req) throws JsonProcessingException {
        DataTable<Coupon> list = new DataTable<>();
        List<Coupon> couponActivities = couponRepository.findAllByActivityId(aid);
        list.setRecordsTotal((long)couponActivities.size());
        list.setRecordsFiltered((long)couponActivities.size());
        list.setData(couponActivities);
        list.setDraw(req.getDraw());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(list);
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(){
        return "/coupon/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public String doCreate(@RequestParam("name")String name,@RequestParam("count")Integer count,@RequestParam("price")Float price){
        CouponActivity couponActivity = new CouponActivity();
        couponActivity.setName(name);
        couponActivity.setCount(count);
        couponActivityRepository.save(couponActivity);
        for(int i=0;i<count;i++){
            Coupon coupon = new Coupon();
            coupon.setActivityId(couponActivity.getId());
            coupon.setPrice(price);
            coupon.setCode(getOrderIdByUUId());
            couponRepository.save(coupon);
        }
        return "{success: true}";
    }

    private String getOrderIdByUUId() {
        int machineId = 1;
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if(hashCodeV < 0) {
            hashCodeV = - hashCodeV;
        }
        return machineId + String.format("%015d", hashCodeV);
    }

}
