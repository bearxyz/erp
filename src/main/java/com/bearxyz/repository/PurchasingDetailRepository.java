package com.bearxyz.repository;

import com.bearxyz.domain.po.business.PurchasingDetail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by bearxyz on 2017/8/16.
 */
public interface PurchasingDetailRepository extends JpaRepository<PurchasingDetail, String> {
}
