package com.bearxyz.repository;

import com.bearxyz.domain.po.business.PurchasingOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by bearxyz on 2017/8/24.
 */
public interface PurchasingOrderItemRepository extends JpaRepository<PurchasingOrderItem, String>, JpaSpecificationExecutor<PurchasingOrderItem> {

    @Query(value = "SELECT COUNT (ID) FROM T_PURCHASING_ORDER_ITEM WHERE APPROVED = 1 AND FINISHED_AMMOUNT < AMMOUNT", nativeQuery = true)
    Integer countByNeedLoad();

}
