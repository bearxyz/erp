package com.bearxyz.listener;

import com.bearxyz.domain.po.business.Sale;
import com.bearxyz.repository.SaleRepository;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by bearxyz on 2017/8/30.
 */
@Transactional
@Component
public class ProductSaleEnd implements ExecutionListener {
    @Autowired
    private SaleRepository repository;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        Sale sale = repository.findOne(execution.getProcessBusinessKey());
        sale.setApproved(true);
        repository.save(sale);
    }
}
