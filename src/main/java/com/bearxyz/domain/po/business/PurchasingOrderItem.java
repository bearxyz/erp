package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.*;

/**
 * Created by bearxyz on 2017/8/23.
 */
@Entity
@Table(name = "T_PURCHASING_ORDER_ITEM")
public class PurchasingOrderItem extends BaseDomain {

    private static final long serialVersionUID = -7095515756192221031L;
    @Column(length = 36)
    private String goodsId = "";
    @Column
    private Integer count = 0;
    @Column(length = 50)
    private String spec = "";
    @Column
    private Integer ammount = 0;
    @Column(length = 10)
    private String unit = "";
    @Column(length = 36)
    private String packageId = "";
    @Column(length = 50)
    private String supplier = "";
    @Column
    private Float price = (float) 0.0;
    @Column
    private boolean approved = false;
    @Column
    private Integer finishedAmmount = 0;
    @Column
    private Integer finishedCount = 0;

    @Transient
    private Goods goods;
    @Transient
    private String supplierName = "";

    @ManyToOne
    @JoinColumn(name = "PURCHASING_ID")
    private PurchasingOrder order;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public Integer getAmmount() {
        return ammount;
    }

    public void setAmmount(Integer ammount) {
        this.ammount = ammount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public void setFinishedAmmount(Integer finishedAmmount) {
        this.finishedAmmount = finishedAmmount;
    }

    public Integer getFinishedAmmount() {
        return finishedAmmount;
    }

    public PurchasingOrder getOrder() {
        return order;
    }

    public void setOrder(PurchasingOrder order) {
        this.order = order;
    }

    public Integer getFinishedCount() {
        return finishedCount;
    }

    public void setFinishedCount(Integer finishedCount) {
        this.finishedCount = finishedCount;
    }
}
