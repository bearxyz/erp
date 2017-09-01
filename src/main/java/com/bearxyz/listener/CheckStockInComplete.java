package com.bearxyz.listener;

import com.bearxyz.domain.po.business.*;
import com.bearxyz.repository.GoodsRepository;
import com.bearxyz.repository.PurchasingOrderItemRepository;
import com.bearxyz.repository.PurchasingOrderRepository;
import com.bearxyz.repository.StockRepository;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by bearxyz on 2017/8/25.
 */
@Transactional
@Component
public class CheckStockInComplete implements JavaDelegate {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private PurchasingOrderRepository purchasingOrderRepository;

    @Autowired
    private PurchasingOrderItemRepository purchasingOrderItemRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String id = execution.getProcessBusinessKey();
        Stock stock = stockRepository.findOne(id);
        for (StockItem item : stock.getItems()) {
            Goods goods = goodsRepository.findOne(item.getGoodsId());
            goods.setStock(goods.getStock() + item.getAmmount());
            goodsRepository.save(goods);
            PurchasingOrderItem orderItem = purchasingOrderItemRepository.findOne(item.getPurchasingOrderItemId());
            orderItem.setFinishedAmmount(orderItem.getFinishedAmmount() + item.getAmmount());
            orderItem.setFinishedCount(orderItem.getFinishedCount() + item.getCount());
            if(orderItem.getFinishedAmmount()>=orderItem.getAmmount())
                orderItem.setApproved(true);
            purchasingOrderItemRepository.save(orderItem);
            PurchasingOrder order = purchasingOrderRepository.findOne(orderItem.getOrder().getId());
            boolean approved = true;
            for (PurchasingOrderItem it : order.getItems()) {
                if (it.getFinishedAmmount() < it.getAmmount()) {
                    approved = false;
                    break;
                }
            }
            order.setApproved(approved);
            purchasingOrderRepository.save(order);
            execution.setVariable("complete", approved);
        }
        stock.setApproved(true);
        stockRepository.save(stock);
    }
}
