package com.bearxyz.repository;

import com.bearxyz.domain.po.business.Replenishment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by bearxyz on 2017/9/2.
 */
public interface ReplenishmentRepository extends JpaRepository<Replenishment, String>, JpaSpecificationExecutor<Replenishment> {
}
