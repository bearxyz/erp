package com.bearxyz.service.business;

import com.bearxyz.domain.po.business.Order;
import com.bearxyz.domain.po.business.OrderItem;
import com.bearxyz.domain.po.business.Sale;
import com.bearxyz.domain.po.sys.Dict;
import com.bearxyz.repository.OrderItemRepository;
import com.bearxyz.repository.OrderRepository;
import com.bearxyz.service.sys.SysService;
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
    @Autowired
    private SysService sysService;
    @Autowired
    private  SaleService saleService;

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
        if(order !=null && order.getItems() !=null && order.getItems().size()>0){
            for (OrderItem orderItem :order.getItems()){
                Sale sale=null;
                if(orderItem.getSale() ==null) {
                    sale =saleService.getById(orderItem.getSaleId());
                }else{
                    sale =orderItem.getSale();
                }
                    if(sale.getProject() !=null && (!sale.getProject().equals(""))){
                        Dict dict =sysService.getDictByMask(sale.getProject());
                        sale.setProjectName(dict.getName());
                    }
                    if(sale.getType() !=null && (!sale.getType().equals(""))){
                        Dict dict =sysService.getDictByMask(sale.getType());
                        sale.setTypeName(dict.getName());
                    }
                    if(sale.getSubtype() !=null &&  (!sale.getSubtype().equals(""))){
                        Dict dict =sysService.getDictByMask(sale.getSubtype());
                        sale.setSubtypeName(dict.getName());
                    }
                    orderItem.setSale(sale);
                }

        }
        return order;
    }

}
