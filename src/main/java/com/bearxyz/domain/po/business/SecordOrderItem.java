package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created by bearxyz on 2017/8/30.
 */
@Entity
@Table(name = "T_SECORD_ORDER_ITEM")
public class SecordOrderItem extends BaseDomain {

    @Column(length = 36)
    private String saleId = "";
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


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private SecordOrder secordOrder;

    @Column(length = 50)
    private String project = "";
    @Column(length = 50)
    private String type = "";
    @Column(length = 50)
    private String subtype = "";


    @Transient
    private Sale sale;

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
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

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public SecordOrder getSecordOrder() {
        return secordOrder;
    }

    public void setSecordOrder(SecordOrder secordOrder) {
        this.secordOrder = secordOrder;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }
}
