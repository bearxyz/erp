package com.bearxyz.listener;

import com.bearxyz.domain.po.business.WorkOrder;
import com.bearxyz.repository.WorkOrderRepository;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by bearxyz on 2017/9/6.
 */
@Transactional
@Component
public class WorkOrderFinished implements ExecutionListener {

    @Autowired
    private WorkOrderRepository repository;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        WorkOrder workOrder = repository.findOne(execution.getProcessBusinessKey());
        workOrder.setFinished(true);
        repository.save(workOrder);
    }
}
