package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bearxyz on 2017/7/24.
 */

@Entity
@Table(name = "T_PACKAGE")
public class Package extends BaseDomain {

    private static final long serialVersionUID = 2238791613867000784L;

    @Column(length = 50)
    private String spec = "";
    @Column(length = 10)
    private String unit = "";
    @Column
    private Integer ammount = 0;
    @Column
    private Integer stock = 0;

    @Transient
    private String unitName = "";

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
