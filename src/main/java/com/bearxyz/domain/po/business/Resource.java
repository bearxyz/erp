package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bearxyz on 2017/8/30.
 */
@Entity
@Table(name = "T_RESOURCE")
public class Resource extends BaseDomain {

    private static final long serialVersionUID = 6631216198653954738L;
    @Column(length = 50)
    private String code = "";
    @Column(length = 50)
    private String name = "";
    @Column(length = 50)
    private String project = "";
    @Column
    private Float price = (float)0.0;
    @Column(length = 50)
    private String type = "";
    @Column(length = 50)
    private String processInstanceId = "";
    @Column
    private Boolean approved = false;
    @Column
    private Boolean onSale = false;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "CLOB")
    private String content;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "BUSINESS_ID")
    private List<ResourceAttachment> attachments = new ArrayList<>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Boolean getOnSale() {
        return onSale;
    }

    public void setOnSale(Boolean onSale) {
        this.onSale = onSale;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<ResourceAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<ResourceAttachment> attachments) {
        this.attachments = attachments;
    }
}
