package com.bearxyz.listener;

import com.bearxyz.domain.po.business.Stock;
import com.bearxyz.domain.po.business.StockItem;
import com.bearxyz.repository.StockRepository;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by bearxyz on 2017/8/28.
 */
@Transactional
@Component
public class StockInFinished implements ExecutionListener {
    private static final long serialVersionUID = -6762266131509018390L;

    @Autowired
    private StockRepository repository;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        Stock stock = repository.findOne(execution.getProcessBusinessKey());
        stock.setApproved(true);
        for(StockItem item: stock.getItems()){
            item.setApproved(true);
        }
        repository.save(stock);
    }
}
