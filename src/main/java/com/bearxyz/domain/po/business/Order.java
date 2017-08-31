package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * Created by bearxyz on 2017/8/30.
 */
@Entity
@Table(name = "T_ORDER")
public class Order extends BaseDomain {

    @Column(length = 50)
    private String code = "";
    @Column
    private Float price = (float)0.0;
    @Column(length = 36)
    private String companyId = "";
    @Column(length = 50)
    private String transport = "";
    @Column(length = 200)
    private String deliverAddress = "";
    @Column(length = 50)
    private String processInstanceId = "";
    @Column
    private Boolean approved = false;

    @Transient
    private String taskId;
    @Transient
    private String taskName;
    @Transient
    private String applyer;
    @Transient
    private Date finishedDate;

}
