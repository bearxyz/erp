package com.bearxyz.listener;

import com.bearxyz.domain.po.business.PurchasingOrder;
import com.bearxyz.domain.po.business.PurchasingOrderItem;
import com.bearxyz.repository.PurchasingOrderRepository;
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

    private static final long serialVersionUID = -9223123530007826384L;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
    }
}
