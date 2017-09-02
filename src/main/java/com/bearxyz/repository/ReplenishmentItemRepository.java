package com.bearxyz.repository;

import com.bearxyz.domain.po.business.ReplenishmentItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by bearxyz on 2017/9/2.
 */
public interface ReplenishmentItemRepository extends JpaRepository<ReplenishmentItem,String>, JpaSpecificationExecutor<ReplenishmentItem> {
}
