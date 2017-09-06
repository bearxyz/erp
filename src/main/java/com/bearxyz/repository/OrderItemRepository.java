package com.bearxyz.repository;

import com.bearxyz.domain.po.business.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderItemRepository  extends JpaRepository<OrderItem, String>, JpaSpecificationExecutor<OrderItem> {
}
