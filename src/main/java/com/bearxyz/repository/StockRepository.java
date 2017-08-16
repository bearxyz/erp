package com.bearxyz.repository;

import com.bearxyz.domain.po.business.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StockRepository extends JpaRepository<Stock, String>,JpaSpecificationExecutor<Stock> {
}
