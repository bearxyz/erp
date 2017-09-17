package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by bearxyz on 2017/9/7.
 */
@Entity
@Table(name = "T_COUPON")
public class Coupon extends BaseDomain {

    private static final long serialVersionUID = 4606548235642275671L;
    @Column(length = 36)
    private String activityId = "";
    @Column(length = 100)
    private String code = "";
    @Column
    private Float price = (float) 0.0;
    @Column
    private Boolean used = false;
    @Column
    private Integer needCount = 0;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public Integer getNeedCount() {
        return needCount;
    }

    public void setNeedCount(Integer needCount) {
        this.needCount = needCount;
    }

}
