package com.bearxyz.controller.rest;

import com.bearxyz.domain.po.sys.User;
import com.bearxyz.repository.*;
import org.activiti.engine.TaskService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by bearxyz on 2017/9/3.
 */
@RestController
@RequestMapping("/alert")
public class AlertController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private PurchasingOrderRepository purchasingOrderRepository;

    @Autowired
    private PurchasingOrderItemRepository purchasingOrderItemRepository;

    @Autowired
    private StockRepository stockRepository;

    @PostMapping("/task")
    public Long task(){
        Long task = (long)0;
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        task = taskService.createTaskQuery().taskCandidateOrAssigned(user.getId()).count();
        return task;
    }

    @PostMapping("/stock")
    public Integer stock(){
        Integer stock = 0;
        Integer alert = configRepository.findAll().get(0).getStockAlert();
        stock = goodsRepository.countGoodsByStockLessThan(alert);
        return stock;
    }

    @PostMapping("/po")
    public Integer po(){
        Integer stock = 0;
        stock = purchasingOrderRepository.countPurchasingOrdersByApprovedFalse();
        return stock;
    }

    @PostMapping("/stockin")
    public Integer stockin(){
        Integer stock = 0;
        stock = purchasingOrderItemRepository.countByNeedLoad();
        return stock;
    }

    @PostMapping("/stockout")
    public Integer stockout(){
        Integer stock = 0;
        stock = stockRepository.countStocksByTypeAndApproved("STOCK-OUT", false);
        return stock;
    }

}
