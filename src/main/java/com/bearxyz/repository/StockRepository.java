package com.bearxyz.repository;

import com.bearxyz.domain.po.business.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, String>,JpaSpecificationExecutor<Stock> {

    Integer countStocksByTypeAndApproved(String type, boolean approved);


    @Query(value = "SELECT s.* FROM T_STOCK s INNER JOIN T_WAREHOUSE w ON s.DELIVER_WAREHOUSE_ID = w.ID WHERE w.USER_ID=?1", nativeQuery = true)
    List<Stock> findStockOutByUserId(String userId);

    List<Stock> findAllByPurchasingOrderIdAndApproved(String pid, boolean approved);

}
