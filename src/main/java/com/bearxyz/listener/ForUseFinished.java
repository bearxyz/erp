package com.bearxyz.listener;

import com.bearxyz.domain.po.business.ForUse;
import com.bearxyz.domain.po.business.ForUseItem;
import com.bearxyz.domain.po.business.Stock;
import com.bearxyz.domain.po.business.StockItem;
import com.bearxyz.repository.ForUseRepository;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class ForUseFinished implements ExecutionListener {

    @Autowired
    private ForUseRepository repository;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        ForUse forUse = repository.findOne(execution.getProcessBusinessKey());
        forUse.setApproved(true);
        repository.save(forUse);
    }
}
