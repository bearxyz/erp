package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by bearxyz on 2017/9/3.
 */
@Entity
@Table(name = "T_FIRST_ORDER")
public class FIrstOrder extends BaseDomain {

    @Column(length = 50)
    private String code = "";
    @Column(length = 50)
    private String companyId = "";
    @Column(length = 50)
    private String userId = "";
    @Column(length = 200)
    private String deliverAddress = "";
    @Column
    private Integer status = 0;

}
