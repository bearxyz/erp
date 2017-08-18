package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bearxyz on 2017/5/25.
 */
@Entity
@Table(name = "T_PURCHASING")
public class Purchasing extends BaseDomain {

    private static final long serialVersionUID = -1209347414434373515L;
    @Column
    private String code = "";

    @Column
    private String purchaser = "";
    @Column(length = 200)
    private String purpose = "";
    @Column(length = 50)
    private String processInstanceId = "";
    @Column
    private Boolean approved = false;
    @Column(length = 50)
    private String operater = "";
    @Column
    private Date operationDate;

    @Transient
    private String goods = "";
    @Transient
    private String taskId;
    @Transient
    private String taskName;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "PURCHASING_ID")
    private List<PurchasingDetail> items = new ArrayList<>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPurchaser() {
        return purchaser;
    }

    public void setPurchaser(String purchaser) {
        this.purchaser = purchaser;
    }

    public List<PurchasingDetail> getItems() {
        return items;
    }

    public void setItems(List<PurchasingDetail> items) {
        this.items = items;
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

    public String getOperater() {
        return operater;
    }

    public void setOperater(String operater) {
        this.operater = operater;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }
}
