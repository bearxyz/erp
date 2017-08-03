package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by bearxyz on 2017/5/25.
 */
@Entity
public class Purchasing extends BaseDomain {

    private static final long serialVersionUID = -1209347414434373515L;
    @Column
    private String code = "";

    @Column
    private String purchaser = "";

}
