package com.bearxyz.listener;

import com.bearxyz.domain.po.business.Order;
import com.bearxyz.domain.po.business.SupportApply;
import com.bearxyz.repository.OrderRepository;
import com.bearxyz.repository.SupportApplyRepository;
import com.bearxyz.utility.DateUtility;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by bearxyz on 2017/9/4.
 */

@Transactional
@Component
public class SupportApplyAccept implements ExecutionListener {

    @Autowired
    private SupportApplyRepository repository;
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        SupportApply apply = repository.findOne(execution.getProcessBusinessKey());
        apply.setStatus(2);
        Order order = new Order();
        order.setType("GOODS_SUPPORT");
        order.setApproved(true);
        order.setCompanyId(apply.getCompanyId());
        order.setSaleId(apply.getSaleId());
        order.setPrice(apply.getPrice()* DateUtility.daysOfTwo(apply.getRealStartDate(),apply.getRealEndDate()));
        order.setOrderCount(apply.getRealManCount());
        order.setStatus(1);
        repository.save(apply);
        orderRepository.save(order);
    }
}
