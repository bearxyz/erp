package com.bearxyz.repository;

import com.bearxyz.domain.po.business.ForUseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by bearxyz on 2017/8/16.
 */
public interface ForUseItemRepository extends JpaRepository<ForUseItem, String>, JpaSpecificationExecutor<ForUseItem> {

    @Query(value = "SELECT i.* FROM T_FOR_USE_ITEM i INNER JOIN T_FOR_USE f on f.ID=i.FOR_USE_ID WHERE f.TYPE=?1 and f.APPROVED=?2", nativeQuery = true)
    List<ForUseItem> findAllByTypeAndApproved(String type, boolean approved);

}
