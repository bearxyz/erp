package com.bearxyz.service.business;

import com.bearxyz.domain.po.business.Order;
import com.bearxyz.domain.po.business.OrderItem;
import com.bearxyz.repository.OrderItemRepository;
import com.bearxyz.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    public void apply(Order order){
        orderRepository.save(order);
    }

    public List<Order> getOrderByStateList(int status,String companyId){
        return orderRepository.getOrderByStateList(status,companyId);
    }

    public List<OrderItem> getItemByOrderId(String orderId) {
            Order order =orderRepository.findOne(orderId);
            return order.getItems();
    }
    public Order getOrderById(String id){
        Order order =orderRepository.findOne(id);
        return order;
    }

}
