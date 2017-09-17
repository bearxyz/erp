package com.bearxyz.repository;

import com.bearxyz.domain.po.business.Order;
import com.bearxyz.domain.po.business.SecordOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by bearxyz on 2017/9/4.
 */
public interface SecordOrderRepository extends JpaRepository<SecordOrder, String>, JpaSpecificationExecutor<SecordOrder> {

    @Query(value="SELECT o.*  FROM T_SECORD_ORDER o   WHERE o.STATUS=?1 and o.COMPANY_ID=?2",nativeQuery = true)
    List<SecordOrder> getSecordOrderByStateList(int status, String companyId);

}
