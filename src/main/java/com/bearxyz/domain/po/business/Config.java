package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by bearxyz on 2017/9/3.
 */
@Entity
@Table(name = "T_CONFIG")
public class Config extends BaseDomain {

    private static final long serialVersionUID = 8879113092554028116L;
    @Column
    private Integer stockAlert = 0;
    @Column
    private Float deliverFee = (float)0.0;

    public Integer getStockAlert() {
        return stockAlert;
    }

    public void setStockAlert(Integer stockAlert) {
        this.stockAlert = stockAlert;
    }

    public Float getDeliverFee() {
        return deliverFee;
    }

    public void setDeliverFee(Float deliverFee) {
        this.deliverFee = deliverFee;
    }
}
