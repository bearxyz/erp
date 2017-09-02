package com.bearxyz.listener;

import com.bearxyz.domain.po.business.PurchasingOrder;
import com.bearxyz.domain.po.business.PurchasingOrderItem;
import com.bearxyz.domain.po.business.Replenishment;
import com.bearxyz.domain.po.business.ReplenishmentItem;
import com.bearxyz.repository.PurchasingOrderRepository;
import com.bearxyz.repository.ReplenishmentRepository;
import com.bearxyz.utility.OrderUtils;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by bearxyz on 2017/9/2.
 */

@Transactional
@Component
public class ReplenishmentFinished implements ExecutionListener {

    @Autowired
    private ReplenishmentRepository repository;
    @Autowired
    private PurchasingOrderRepository purchasingOrderRepository;

    @Override
    public void notify(DelegateExecution execution) throws Exception {

        Replenishment purchasing = repository.findOne(execution.getProcessBusinessKey());
        purchasing.setApproved(true);
        PurchasingOrder order = new PurchasingOrder();
        order.setCode(OrderUtils.genSerialnumber("CG"));
        order.setApproved(false);
        for(ReplenishmentItem item : purchasing.getItems()){
            PurchasingOrderItem si = new PurchasingOrderItem();
            si.setGoodsId(item.getGoodsId());
            si.setCount(item.getCount());
            si.setAmmount(item.getAmmount());
            si.setPackageId(item.getPackageId());
            si.setSpec(item.getSpec());
            si.setUnit(item.getUnit());
            order.getItems().add(si);
        }
        purchasingOrderRepository.save(order);
        repository.save(purchasing);

    }
}
