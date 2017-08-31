package com.bearxyz.repository;

import com.bearxyz.domain.po.business.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by bearxyz on 2017/8/30.
 */
public interface SaleItemRepository extends JpaRepository<SaleItem, String>, JpaSpecificationExecutor<SaleItem> {
}
