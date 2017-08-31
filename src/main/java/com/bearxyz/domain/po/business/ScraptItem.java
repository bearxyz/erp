package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.*;

/**
 * Created by bearxyz on 2017/8/27.
 */
@Entity
@Table(name = "T_SCRAPT_ITEM")
public class ScraptItem extends BaseDomain {

    private static final long serialVersionUID = 5849427331875292192L;
    @Column(length = 36)
    private String purchasingOrderItemId = "";
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
    @Column
    private Float price = (float)0.0;
    @Column(length = 36)
    private String packageId = "";
    @Column
    private boolean approved = false;

    @Transient
    private Goods goods;

    @ManyToOne
    @JoinColumn(name = "SCRAPT_ID")
    private Scrapt scrapt;

    public String getPurchasingOrderItemId() {
        return purchasingOrderItemId;
    }

    public void setPurchasingOrderItemId(String purchasingOrderItemId) {
        this.purchasingOrderItemId = purchasingOrderItemId;
    }

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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public Scrapt getScrapt() {
        return scrapt;
    }

    public void setScrapt(Scrapt scrapt) {
        this.scrapt = scrapt;
    }
}
