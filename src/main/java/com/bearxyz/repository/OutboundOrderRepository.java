package com.bearxyz.repository;

import com.bearxyz.domain.po.business.Outbound;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by bearxyz on 2017/6/1.
 */
public interface OutboundOrderRepository extends JpaRepository<Outbound, Long> {
}
