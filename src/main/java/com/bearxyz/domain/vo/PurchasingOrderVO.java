package com.bearxyz.domain.vo;

import com.bearxyz.domain.po.business.Goods;

/**
 * Created by bearxyz on 2017/8/24.
 */
public class PurchasingOrderVO extends BaseVO {

    private static final long serialVersionUID = 7928777460008888732L;
    private String code;
    private String supplier;
    private Integer count;
    private String spec;
    private Integer ammount;
    private Integer finishedAmmount = 0;
    private Goods goods;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
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

    public Integer getFinishedAmmount() {
        return finishedAmmount;
    }

    public void setFinishedAmmount(Integer finishedAmmount) {
        this.finishedAmmount = finishedAmmount;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }
}
