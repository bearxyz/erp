package com.bearxyz.repository;

import com.bearxyz.domain.po.business.Purchasing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by bearxyz on 2017/8/16.
 */
public interface PurchasingRepository extends JpaRepository<Purchasing, String>, JpaSpecificationExecutor<Purchasing> {
}
