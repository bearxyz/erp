package com.bearxyz.repository;

import com.bearxyz.domain.po.business.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by bearxyz on 2017/9/7.
 */
public interface CouponRepository extends JpaRepository<Coupon, String>, JpaSpecificationExecutor<Coupon> {

    List<Coupon> findAllByActivityId(String aid);

    Integer countAllByActivityIdAndUsed(String aid, boolean used);
}
