package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by bearxyz on 2017/8/30.
 */
@Entity
@Table(name = "T_ORDER_ITEM")
public class OrderItem extends BaseDomain {

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

    @Transient
    private Sale sale;

}
