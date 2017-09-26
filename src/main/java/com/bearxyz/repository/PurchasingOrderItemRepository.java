package com.bearxyz.repository;

import com.bearxyz.domain.po.business.PurchasingOrderItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by bearxyz on 2017/8/24.
 */
public interface PurchasingOrderItemRepository extends JpaRepository<PurchasingOrderItem, String>, JpaSpecificationExecutor<PurchasingOrderItem> {

    @Query(value = "SELECT COUNT (ID) FROM T_PURCHASING_ORDER_ITEM WHERE APPROVED = 1 AND FINISHED_AMMOUNT < AMMOUNT", nativeQuery = true)
    Integer countByNeedLoad();

    @Query(value = "SELECT i.* FROM T_PURCHASING_ORDER_ITEM i INNER JOIN T_GOODS g on g.ID=i.GOODS_ID WHERE i.DATE_CEATED<=to_date(?2, 'yyyy-mm-dd') AND i.DATE_CEATED>=to_date(?1, 'yyyy-mm-dd') AND i.APPROVED=1 AND g.PROJECT like CONCAT('%',?3,'%') AND g.TYPE like CONCAT('%',?4,'%') AND g.NAME like CONCAT('%',?5,'%')", nativeQuery = true)
    List<PurchasingOrderItem> reportPurchasingOrderItem(String startDate,String endDate, String project, String type, String name);

}
