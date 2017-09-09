package com.bearxyz.repository;

import com.bearxyz.domain.po.business.CouponActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by bearxyz on 2017/9/7.
 */
public interface CouponActivityRepository extends JpaRepository<CouponActivity, String>, JpaSpecificationExecutor<CouponActivity> {
}
