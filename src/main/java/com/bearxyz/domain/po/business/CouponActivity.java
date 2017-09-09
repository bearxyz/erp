package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by bearxyz on 2017/9/7.
 */
@Entity
@Table(name = "T_COUPON_ACTIVITY")
public class CouponActivity extends BaseDomain {

    private static final long serialVersionUID = -2126072571122614571L;
    @Column(length = 50)
    private String name;
    @Column
    private Integer count;

    @Transient
    private Integer used;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getUsed() {
        return used;
    }

    public void setUsed(Integer used) {
        this.used = used;
    }
}
