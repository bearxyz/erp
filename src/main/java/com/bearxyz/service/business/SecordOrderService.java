package com.bearxyz.service.business;

import com.bearxyz.domain.po.business.Order;
import com.bearxyz.domain.po.business.OrderItem;
import com.bearxyz.domain.po.business.SecordOrder;
import com.bearxyz.domain.po.business.SecordOrderItem;
import com.bearxyz.repository.OrderItemRepository;
import com.bearxyz.repository.OrderRepository;
import com.bearxyz.repository.SecordOrderItemRepository;
import com.bearxyz.repository.SecordOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SecordOrderService {

    @Autowired
    private SecordOrderRepository secordOrderRepository;
    @Autowired
    private SecordOrderItemRepository secordOrderItemRepository;

    public void apply(SecordOrder secordOrder){
        secordOrderRepository.save(secordOrder);
    }

    public List<SecordOrder> getSecordOrderByStateList(int status,String companyId){
        return secordOrderRepository.getSecordOrderByStateList(status,companyId);
    }

    public List<SecordOrderItem> getSecordItemByOrderId(String orderId) {
            SecordOrder secordOrder =secordOrderRepository.findOne(orderId);
            return secordOrder.getItems();
    }
    public SecordOrder getOrderById(String id){
        SecordOrder secordOrder =secordOrderRepository.findOne(id);
        return secordOrder;
    }

}
