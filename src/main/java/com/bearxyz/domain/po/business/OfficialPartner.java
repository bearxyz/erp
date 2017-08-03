package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by bearxyz on 2017/7/24.
 */
@Entity
@Table(name = "T_OFFICIAL_PARTNER")
public class OfficialPartner extends BaseDomain {

    private static final long serialVersionUID = -4154068955687305040L;

    @Column(length = 100)
    private String name = "";
    @Column(length = 50)
    private String province = "";
    @Column(length = 50)
    private String city = "";
    @Column(length = 50)
    private String district = "";
    @Column(length = 100)
    private String address = "";
    @Column(length = 50)
    private String contact = "";
    @Column(length = 50)
    private String phone = "";
    @Column(length = 50)
    private String type = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
