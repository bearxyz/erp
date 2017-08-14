package com.bearxyz.service.workflow;

import com.bearxyz.domain.po.business.ForUse;
import com.bearxyz.repository.ForUseRepository;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Created by bearxyz on 2017/8/7.
 */

@Service
@Transactional
public class ForUseWorkflowService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private ForUseRepository repository;

    public ProcessInstance startWorkflow(ForUse forUse, Map<String, Object> variables){
        repository.saveAndFlush(forUse);
        String bussinessKey = forUse.getId();
        ProcessInstance processInstance = null;
        try{
            identityService.setAuthenticatedUserId(forUse.getUserId());
            processInstance = runtimeService.startProcessInstanceByKey("for-use", bussinessKey, variables);
            String processInstanceId = processInstance.getId();
            forUse.setProcessInstanceId(processInstanceId);
        }
        finally {
            identityService.setAuthenticatedUserId(null);
        }
        return processInstance;
    }


}
