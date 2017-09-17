package com.bearxyz.repository;

import com.bearxyz.domain.po.business.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by bearxyz on 2017/9/14.
 */
public interface WarehouseRepository extends JpaRepository<Warehouse, String> {
}
