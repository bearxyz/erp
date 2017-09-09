package com.bearxyz.repository;

import com.bearxyz.domain.po.business.GroupBuy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by bearxyz on 2017/9/7.
 */
public interface GroupBuyRepository extends JpaRepository<GroupBuy, String>, JpaSpecificationExecutor<GroupBuy> {
}
