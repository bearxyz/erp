package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by bearxyz on 2017/9/3.
 */
@Entity
@Table(name = "T_FIRST_ORDER_ITME")
public class FirstOrderItem extends BaseDomain {

    @Column(length = 36)
    private String firstSaleId = "";
    @Column
    private Integer count = 0;
    @Column(length = 50)
    private String spec = "";
    @Column
    private Float price = (float)0.0;
    @Column(length = 10)
    private String unit = "";
    @Column(length = 36)
    private String packageId = "";

    @Transient
    private FirstSale sale;

    public String getFirstSaleId() {
        return firstSaleId;
    }

    public void setFirstSaleId(String firstSaleId) {
        this.firstSaleId = firstSaleId;
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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
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

    public FirstSale getSale() {
        return sale;
    }

    public void setSale(FirstSale sale) {
        this.sale = sale;
    }
}
