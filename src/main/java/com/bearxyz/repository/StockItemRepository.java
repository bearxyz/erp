package com.bearxyz.repository;

import com.bearxyz.domain.po.business.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StockItemRepository extends JpaRepository<StockItem, String>, JpaSpecificationExecutor<StockItem> {
}
