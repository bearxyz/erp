package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.*;

@Entity
@Table(name = "T_STOCK_ITEM")
public class StockItem extends BaseDomain{

    private static final long serialVersionUID = 4275561680444080302L;
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
    @Column(length = 50)
    private String warehouse = "";

    @ManyToOne
    @JoinColumn(name = "STOCK_ID")
    private Stock stock;

    public String getPurchasingOrderItemId() {
        return purchasingOrderItemId;
    }

    public void setPurchasingOrderItemId(String purchasingOrderId) {
        this.purchasingOrderItemId = purchasingOrderId;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
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

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }
}
