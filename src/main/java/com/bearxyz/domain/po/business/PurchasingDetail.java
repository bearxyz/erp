package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by bearxyz on 2017/6/1.
 */

@Entity
public class PurchasingDetail extends BaseDomain {

    private static final long serialVersionUID = 2185885842446796700L;

    @Column
    private Long purchasingRequisitionId = (long)0;

    @Column
    private Long goodId = (long)0;

    @Column
    private Long supplierId = (long)0;

    @Column
    private Float unitPrice = (float)0.0;

    @Column
    private Integer quantity = 0;

    public Long getPurchasingRequisitionId() {
        return purchasingRequisitionId;
    }

    public void setPurchasingRequisitionId(Long purchasingRequisitionId) {
        this.purchasingRequisitionId = purchasingRequisitionId;
    }

    public Long getGoodId() {
        return goodId;
    }

    public void setGoodId(Long goodId) {
        this.goodId = goodId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
