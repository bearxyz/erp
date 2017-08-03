package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;

/**
 * Created by bearxyz on 2017/7/27.
 */

@Entity
@Table(name = "T_CONTRACT")
public class Contract extends BaseDomain {

    private static final long serialVersionUID = -1352768149673334271L;

    @Column(length = 50)
    private String title = "";
    @Column(length = 50)
    private String subTitle = "";
    @Column
    private Date signDate;
    @Column
    private Date startDate;
    @Column
    private Date endDate;
    @Column(length = 36)
    private String companyId = "";

}
