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
    private Float price = (float)0.0;
    //1 已受理 2 申请通过 3 不通过
    @Column
    private Integer status = 0;
    @Column(length = 50)
    private String processInstanceId = "";
    @Column(length = 1000)
    private String memo = "";

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


}
