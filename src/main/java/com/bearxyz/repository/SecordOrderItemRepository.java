package com.bearxyz.repository;

import com.bearxyz.domain.po.business.OrderItem;
import com.bearxyz.domain.po.business.SecordOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SecordOrderItemRepository extends JpaRepository<SecordOrderItem, String>, JpaSpecificationExecutor<SecordOrderItem> {
}
