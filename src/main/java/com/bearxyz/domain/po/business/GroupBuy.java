package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by bearxyz on 2017/9/7.
 */
@Entity
@Table(name = "T_GROUP_BUY")
public class GroupBuy extends BaseDomain {

    private static final long serialVersionUID = -3947635824583391018L;
    @Column(length = 36)
    private String saleId = "";
    @Column
    private Integer manCount = 0;
    @Column
    private Float disCount = (float)0.0;

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public Integer getManCount() {
        return manCount;
    }

    public void setManCount(Integer manCount) {
        this.manCount = manCount;
    }

    public Float getDisCount() {
        return disCount;
    }

    public void setDisCount(Float disCount) {
        this.disCount = disCount;
    }
}
