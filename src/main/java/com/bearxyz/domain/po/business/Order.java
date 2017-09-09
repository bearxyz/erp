package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bearxyz on 2017/8/30.
 */
@Entity
@Table(name = "T_ORDER")
public class Order extends BaseDomain {

    @Column(length = 50)
    private String code = "";
    @Column
    private Float price = (float) 0.0;
    @Column
    private Float deliverFee=(float)0.0;
    @Column(length = 36)
    private String companyId = "";
    @Column(length = 50)
    private String transport = "";
    @Column(length = 50)
    private String transportCompany = "";
    @Column(length = 50)
    private String transportNo = "";
    @Column(length = 50)
    private String province = "";
    @Column(length = 50)
    private String city = "";
    @Column(length = 50)
    private String district = "";
    @Column(length = 200)
    private String address = "";
    @Column(length = 50)
    private String processInstanceId = "";
    @Column
    private Boolean approved = false;
    //订单状态：1:待支付；2:待发货;3:待收货；4:完成
    @Column
    private Integer status = 0;
    @Column(length = 50)
    private String type = "";
    @Column(length = 36)
    private String saleId = "";
    @Column
    private String typeName = "";
    @Column
    private Integer orderCount=0;
    @Column
    private Float totalPrice = (float)0.0;


    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "ORDER_ID")
    private List<OrderItem> items = new ArrayList<>();

    @Transient
    private Sale sale;
    @Transient
    private String companyName = "";
    @Transient
    private String taskId;
    @Transient
    private String taskName;
    @Transient
    private String applyer;
    @Transient
    private Date finishedDate;

    public String getTransportCompany() {
        return transportCompany;
    }

    public void setTransportCompany(String transportCompany) {
        this.transportCompany = transportCompany;
    }

    public String getTransportNo() {
        return transportNo;
    }

    public void setTransportNo(String transportNo) {
        this.transportNo = transportNo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
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

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getApplyer() {
        return applyer;
    }

    public void setApplyer(String applyer) {
        this.applyer = applyer;
    }

    public Date getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(Date finishedDate) {
        this.finishedDate = finishedDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getSaleId() {
        return saleId;
    }

    public Float getDeliverFee() {
        return deliverFee;
    }

    public void setDeliverFee(Float deliverFee) {
        this.deliverFee = deliverFee;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }
}
