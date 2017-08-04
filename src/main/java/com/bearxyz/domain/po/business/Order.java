package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bearxyz on 2017/7/25.
 */
@Entity
@Table(name = "T_ORDER")
public class Order extends BaseDomain {

    @Column(length = 50)
    private String serial = "";

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "ORDER_ID")
    private List<OrderDetail> detail = new ArrayList<>();

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public List<OrderDetail> getDetail() {
        return detail;
    }

    public void setDetail(List<OrderDetail> detail) {
        this.detail = detail;
    }
}
