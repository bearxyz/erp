package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by bearxyz on 2017/7/27.
 */

@Entity
@Table(name = "T_CONTRACT")
public class Contract extends BaseDomain {

    private static final long serialVersionUID = -1352768149673334271L;

    @Column(length = 50)
    private String signPerson = "";
    @Column(length = 50)
    private String signPhone = "";
    @Column(length = 50)
    private String signId = "";
    @Column(length = 50)
    private String signEmail = "";
    @Column(length = 50)
    private String signProvince = "";
    @Column(length = 50)
    private String signCity = "";
    @Column(length = 50)
    private String signDistrict = "";
    @Column(length = 50)
    private String signAddress = "";
    @Column
    private Float fee = (float) 0.0;
    @Column
    private Float prestore = (float) 0.0;
    @Column(length = 50)
    private String agentProvince = "";
    @Column(length = 50)
    private String agentCity = "";
    @Column(length = 50)
    private String agentDistrict = "";
    @Column(length = 50)
    private String agentAddress="";
    @Column
    private Integer duration = 0;
    @Column(length = 50)
    private String project = "";
    @Column(length = 50)
    private String title = "";
    @Column(length = 50)
    private String agentType = "";
    @Column(length = 50)
    private String agentLevel = "";
    @Column
    private Date signDate;
    @Column
    private Date startDate;
    @Column
    private Date endDate;
    @Column(length = 36)
    private String companyId = "";
    @Column(length = 200)
    private String memo = "";
    @Column(length = 50)
    private String processInstanceId = "";
    @Column(length = 100)
    private String presentAddress = "";
    @Column
    private Boolean approved = false;
    @Column
    private Boolean expired = false;
    @Column
    private Boolean invalid = false;
    @Column
    private Float goal = (float) 0.0;

    @Transient
    private String projectName = "";
    @Transient
    private String agentTypeName = "";
    @Transient
    private String agentLevelName = "";
    @Transient
    private String taskId;
    @Transient
    private String taskName;
    @Transient
    private String applyer;
    @Transient
    private java.util.Date finishedDate;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "BUSINESS_ID")
    private Set<Attachment> attachments = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "CONTRACT_ID")
    private List<Present> items = new ArrayList<>();

    @Transient
    private String companyName = "";
    @Transient
    private String operator = "";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getSignDate() {
        return signDate;
    }

    public void setSignDate(Date signDate) {
        this.signDate = signDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }


    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
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

    public java.util.Date getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(java.util.Date finishedDate) {
        this.finishedDate = finishedDate;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public String getAgentType() {
        return agentType;
    }

    public void setAgentType(String agentType) {
        this.agentType = agentType;
    }

    public String getAgentLevel() {
        return agentLevel;
    }

    public void setAgentLevel(String agentLevel) {
        this.agentLevel = agentLevel;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getAgentTypeName() {
        return agentTypeName;
    }

    public void setAgentTypeName(String agentTypeName) {
        this.agentTypeName = agentTypeName;
    }

    public String getAgentLevelName() {
        return agentLevelName;
    }

    public void setAgentLevelName(String agentLevelName) {
        this.agentLevelName = agentLevelName;
    }

    public String getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        this.presentAddress = presentAddress;
    }

    public Set<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<Attachment> attachments) {
        this.attachments = attachments;
    }

    public List<Present> getItems() {
        return items;
    }

    public void setItems(List<Present> items) {
        this.items = items;
    }

    public Boolean getInvalid() {
        return invalid;
    }

    public void setInvalid(Boolean invalid) {
        this.invalid = invalid;
    }

    public Float getGoal() {
        return goal;
    }

    public void setGoal(Float goal) {
        this.goal = goal;
    }

    public String getSignPerson() {
        return signPerson;
    }

    public void setSignPerson(String signPerson) {
        this.signPerson = signPerson;
    }

    public String getSignPhone() {
        return signPhone;
    }

    public void setSignPhone(String signPhone) {
        this.signPhone = signPhone;
    }

    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }

    public String getSignEmail() {
        return signEmail;
    }

    public void setSignEmail(String signEmail) {
        this.signEmail = signEmail;
    }

    public String getSignProvince() {
        return signProvince;
    }

    public void setSignProvince(String signProvince) {
        this.signProvince = signProvince;
    }

    public String getSignCity() {
        return signCity;
    }

    public void setSignCity(String signCity) {
        this.signCity = signCity;
    }

    public String getSignDistrict() {
        return signDistrict;
    }

    public void setSignDistrict(String signDistrict) {
        this.signDistrict = signDistrict;
    }

    public String getSignAddress() {
        return signAddress;
    }

    public void setSignAddress(String signAddress) {
        this.signAddress = signAddress;
    }

    public Float getFee() {
        return fee;
    }

    public void setFee(Float fee) {
        this.fee = fee;
    }

    public Float getPrestore() {
        return prestore;
    }

    public void setPrestore(Float prestore) {
        this.prestore = prestore;
    }

    public String getAgentProvince() {
        return agentProvince;
    }

    public void setAgentProvince(String agentProvince) {
        this.agentProvince = agentProvince;
    }

    public String getAgentCity() {
        return agentCity;
    }

    public void setAgentCity(String agentCity) {
        this.agentCity = agentCity;
    }

    public String getAgentDistrict() {
        return agentDistrict;
    }

    public void setAgentDistrict(String agentDistrict) {
        this.agentDistrict = agentDistrict;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getAgentAddress() {
        return agentAddress;
    }

    public void setAgentAddress(String agentAddress) {
        this.agentAddress = agentAddress;
    }
}
