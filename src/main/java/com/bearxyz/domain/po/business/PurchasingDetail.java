package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by bearxyz on 2017/6/1.
 */

@Entity
@Table(name = "T_PURCHASING_DETAIL")
public class PurchasingDetail extends BaseDomain {

    private static final long serialVersionUID = 2185885842446796700L;

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

    @Transient
    private Goods goods;

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
}
