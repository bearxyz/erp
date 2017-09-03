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
@Table(name = "T_FIRST_SALE")
public class FirstSale extends BaseDomain {

    @Column(length = 50)
    private String companyId = "";
    @Column(length = 36)
    private String saleId="";
    @Column
    private Float price = (float)0.0;

    @Transient
    private Sale sale;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }
}
