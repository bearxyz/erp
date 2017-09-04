package com.bearxyz.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by bearxyz on 2017/9/4.
 */

@Component
@Transactional
public class GiveJobTo implements TaskListener {
    private static final long serialVersionUID = -6672027627025178580L;

    @Override
    public void notify(DelegateTask delegateTask) {
        String userId = delegateTask.getVariable("userId").toString();
        delegateTask.setVariable("assignedUser",userId);
    }
}
