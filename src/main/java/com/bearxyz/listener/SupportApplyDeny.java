package com.bearxyz.listener;

import com.bearxyz.domain.po.business.SupportApply;
import com.bearxyz.repository.SupportApplyRepository;
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
public class SupportApplyDeny implements ExecutionListener {

    @Autowired
    private SupportApplyRepository repository;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        SupportApply apply = repository.findOne(execution.getProcessBusinessKey());
        apply.setStatus(3);
        repository.save(apply);
    }
}
