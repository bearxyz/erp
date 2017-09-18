package com.bearxyz.repository;

import com.bearxyz.domain.po.business.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by bearxyz on 2017/9/4.
 */
public interface OrderRepository extends JpaRepository<Order, String>, JpaSpecificationExecutor<Order> {

    @Query(value="SELECT o.*  FROM T_ORDER o   WHERE o.STATUS=?1 and o.COMPANY_ID=?2",nativeQuery = true)
    List<Order> getOrderByStateList(int status, String companyId);

    Integer countOrderByCompanyIdAndStatus(String companyId, Integer Status);

    Order findOrderByCode(String code);
}
