package com.bearxyz.repository;

import com.bearxyz.domain.po.business.GroupBuy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by bearxyz on 2017/9/7.
 */
public interface GroupBuyRepository extends JpaRepository<GroupBuy, String>, JpaSpecificationExecutor<GroupBuy> {

    List<GroupBuy> findAllBySaleId(String saleId);

    @Query(value = "select * from (select * from T_GROUP_BUY where SALE_ID=?1 AND MAN_COUNT<=?2 order by MAN_COUNT DESC) where ROWNUM<=1", nativeQuery = true)
    GroupBuy findBySaleIdAndCount(String saleId, Integer count);

}
