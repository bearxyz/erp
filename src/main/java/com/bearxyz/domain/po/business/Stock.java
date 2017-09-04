package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "T_STOCK")
public class Stock extends BaseDomain {

    private static final long serialVersionUID = 7135568704122702991L;
    @Column(length = 50)
    private String code = "";
    @Column(length = 50)
    private String mask = "";
    @Column(length = 50)
    private String type = "";
    @Column(length = 200)
    private String purpose = "";
    @Column(length = 50)
    private String processInstanceId = "";
    @Column
    private Date operationDate;
    @Column
    private Boolean approved = false;
    @Column(length = 50)
    private String deliverProvince = "";
    @Column(length = 50)
    private String deliverCity = "";
    @Column(length = 50)
    private String deliverDistrict = "";
    @Column(length = 200)
    private String deliverAddress = "";
    @Column(length = 36)
    private String deliverCompany = "";
    @Column(length = 50)
    private String transportFee = "";
    @Column(length = 50)
    private String transportNo = "";

    @Transient
    private String deliverCompanyName = "";
    @Transient
    private String operator = "";
    @Transient
    private String goods = "";
    @Transient
    private String taskId;
    @Transient
    private String taskName;
    @Transient
    private String typeName = "";
    @Transient
    private Date finishedDate;
    @Transient
    private Float totalPrice = (float) 0.0;

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "STOCK_ID")
    private List<StockItem> items = new ArrayList<>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
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

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
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

    public List<StockItem> getItems() {
        return items;
    }

    public void setItems(List<StockItem> items) {
        this.items = items;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Date getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(Date finishedDate) {
        this.finishedDate = finishedDate;
    }

    public String getDeliverAddress() {
        return deliverAddress;
    }

    public void setDeliverAddress(String deliverAddress) {
        this.deliverAddress = deliverAddress;
    }

    public String getDeliverCompany() {
        return deliverCompany;
    }

    public void setDeliverCompany(String deliverCompany) {
        this.deliverCompany = deliverCompany;
    }

    public String getDeliverCompanyName() {
        return deliverCompanyName;
    }

    public void setDeliverCompanyName(String deliverCompanyName) {
        this.deliverCompanyName = deliverCompanyName;
    }

    public String getTransportFee() {
        return transportFee;
    }

    public void setTransportFee(String transportFee) {
        this.transportFee = transportFee;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDeliverProvince() {
        return deliverProvince;
    }

    public void setDeliverProvince(String deliverProvince) {
        this.deliverProvince = deliverProvince;
    }

    public String getDeliverCity() {
        return deliverCity;
    }

    public void setDeliverCity(String deliverCity) {
        this.deliverCity = deliverCity;
    }

    public String getDeliverDistrict() {
        return deliverDistrict;
    }

    public void setDeliverDistrict(String deliverDistrict) {
        this.deliverDistrict = deliverDistrict;
    }

    public String getTransportNo() {
        return transportNo;
    }

    public void setTransportNo(String transportNo) {
        this.transportNo = transportNo;
    }
}
