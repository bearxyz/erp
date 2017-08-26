package com.bearxyz.listener;

import com.bearxyz.domain.po.business.*;
import com.bearxyz.repository.GoodsRepository;
import com.bearxyz.repository.PurchasingOrderRepository;
import com.bearxyz.repository.StockRepository;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by bearxyz on 2017/8/24.
 */
@Transactional
@Component
public class PurchasingOrderFinished implements ExecutionListener {

    @Autowired
    private StockRepository stockRepository;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        Stock stock = stockRepository.findOne(execution.getProcessBusinessKey());
        stock.setApproved(true);
        stockRepository.save(stock);
    }
}
