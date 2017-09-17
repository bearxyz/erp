package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * Created by bearxyz on 2017/9/4.
 */
@Entity
@Table(name = "T_SUPPORT_APPLY")
public class SupportApply extends BaseDomain {

    private static final long serialVersionUID = 112414046651731542L;
    @Column(length = 36)
    private String saleId = "";
    @Column(length = 36)
    private String companyId = "";
    @Column
    private Float price = (float) 0.0;
    //1 已受理 2 申请通过 3 不通过
    @Column
    private Integer status = 0;
    @Column(length = 50)
    private String processInstanceId = "";
    @Column(length = 1000)
    private String memo = "";
    @Column
    private java.sql.Date startDate;
    @Column
    private java.sql.Date endDate;
    @Column
    private Integer manCount = 0;
    @Column
    private java.sql.Date realStartDate;
    @Column
    private java.sql.Date realEndDate;
    @Column
    private Integer realManCount = 0;
    @Column
    private Float realPrice = (float) 0.0;

    @Transient
    private Sale sale;
    @Transient
    private String taskId;
    @Transient
    private String taskName;
    @Transient
    private String applyer;
    @Transient
    private Date finishedDate;

    public String getSaleId() {
        return saleId;
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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public java.sql.Date getStartDate() {
        return startDate;
    }

    public void setStartDate(java.sql.Date startDate) {
        this.startDate = startDate;
    }

    public java.sql.Date getEndDate() {
        return endDate;
    }

    public void setEndDate(java.sql.Date endDate) {
        this.endDate = endDate;
    }

    public Integer getManCount() {
        return manCount;
    }

    public void setManCount(Integer manCount) {
        this.manCount = manCount;
    }

    public java.sql.Date getRealStartDate() {
        return realStartDate;
    }

    public void setRealStartDate(java.sql.Date realStartDate) {
        this.realStartDate = realStartDate;
    }

    public java.sql.Date getRealEndDate() {
        return realEndDate;
    }

    public void setRealEndDate(java.sql.Date realEndDate) {
        this.realEndDate = realEndDate;
    }

    public Integer getRealManCount() {
        return realManCount;
    }

    public void setRealManCount(Integer realManCount) {
        this.realManCount = realManCount;
    }

    public Float getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(Float realPrice) {
        this.realPrice = realPrice;
    }
}
