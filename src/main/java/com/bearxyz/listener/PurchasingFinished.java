package com.bearxyz.listener;

import com.bearxyz.domain.po.business.Purchasing;
import com.bearxyz.domain.po.business.Stock;
import com.bearxyz.repository.PurchasingRepository;
import com.bearxyz.repository.StockRepository;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by bearxyz on 2017/8/21.
 */

@Transactional
@Component
public class PurchasingFinished implements ExecutionListener {

    @Autowired
    private PurchasingRepository repository;
    @Autowired
    private StockRepository stockRepository;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        Purchasing purchasing = repository.findOne(execution.getProcessBusinessKey());
        purchasing.setApproved(true);
        Stock stock = new Stock();
        BeanUtils.copyProperties(purchasing, stock);
        repository.save(purchasing);
    }
}
