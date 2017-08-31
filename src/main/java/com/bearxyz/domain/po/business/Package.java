package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by bearxyz on 2017/7/24.
 */

@Entity
@Table(name = "T_PACKAGE")
public class Package extends BaseDomain {

    private static final long serialVersionUID = 2238791613867000784L;

    @Column(length = 50)
    private String packageSpec = "";
    @Column(length = 10)
    private String packageUnit = "";
    @Column
    private Integer ammount = 0;
    @Column
    private Integer stock = 0;

    public String getPackageSpec() {
        return packageSpec;
    }

    public void setPackageSpec(String packageSpec) {
        this.packageSpec = packageSpec;
    }

    public String getPackageUnit() {
        return packageUnit;
    }

    public void setPackageUnit(String packageUnit) {
        this.packageUnit = packageUnit;
    }

    public Integer getAmmount() {
        return ammount;
    }

    public void setAmmount(Integer ammount) {
        this.ammount = ammount;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

}
