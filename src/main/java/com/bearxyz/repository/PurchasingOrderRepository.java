package com.bearxyz.repository;

import com.bearxyz.domain.po.business.PurchasingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by bearxyz on 2017/8/23.
 */
public interface PurchasingOrderRepository extends JpaRepository<PurchasingOrder,String>, JpaSpecificationExecutor<PurchasingOrder> {
}
