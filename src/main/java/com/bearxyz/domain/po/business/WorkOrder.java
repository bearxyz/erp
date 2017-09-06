package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * Created by bearxyz on 2017/6/1.
 */
@Entity
@Table(name = "T_WORK_ORDER")
public class WorkOrder extends BaseDomain {


    private static final long serialVersionUID = 7663541029021485131L;

    @Column(length = 50)
    private String type="";
    @Column(length = 36)
    private String companyId = "";
    @Column(length = 1000)
    private String memo="";
    @Column(length = 1000)
    private String clientMemo="";
    @Column(length = 1000)
    private String depMemo="";
    @Column(length = 1000)
    private String cooMemo="";
    @Column(length = 1000)
    private String ceoMemo="";
    @Column
    private Boolean finished = false;
    @Column(length = 50)
    private String processInstanceId = "";


    @Transient
    private String companyName = "";
    @Transient
    private String result="";
    @Transient
    private String taskId;
    @Transient
    private String taskName;
    @Transient
    private Date finishedDate;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getClientMemo() {
        return clientMemo;
    }

    public void setClientMemo(String clientMemo) {
        this.clientMemo = clientMemo;
    }

    public String getDepMemo() {
        return depMemo;
    }

    public void setDepMemo(String depMemo) {
        this.depMemo = depMemo;
    }

    public String getCooMemo() {
        return cooMemo;
    }

    public void setCooMemo(String cooMemo) {
        this.cooMemo = cooMemo;
    }

    public String getCeoMemo() {
        return ceoMemo;
    }

    public void setCeoMemo(String ceoMemo) {
        this.ceoMemo = ceoMemo;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
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

    public Date getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(Date finishedDate) {
        this.finishedDate = finishedDate;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
}
