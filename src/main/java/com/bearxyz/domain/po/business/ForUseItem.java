package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by bearxyz on 2017/8/2.
 */

@Entity
@Table(name = "T_FOR_USE_ITEM")
public class ForUseItem extends BaseDomain {

    @Column(length = 36)
    private String goodsId = "";
    @Column
    private Integer count = 0;

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
}
