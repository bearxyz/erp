package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by bearxyz on 2017/9/14.
 */
@Entity
@Table(name = "T_WAREHOUSE")
public class Warehouse extends BaseDomain {

    @Column(length = 50)
    private String name = "";
    @Column(length = 50)
    private String userId = "";

    @Transient
    private String user = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
