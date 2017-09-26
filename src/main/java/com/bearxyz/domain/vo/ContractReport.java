package com.bearxyz.domain.vo;

import java.io.Serializable;

public class ContractReport implements Serializable {

    private String project;
    private String createdBy;
    private String projectName;
    private String createByName;
    private Float prestore;
    private Integer count;
    private Float fee;

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public Float getPrestore() {
        return prestore;
    }

    public void setPrestore(Float prestore) {
        this.prestore = prestore;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Float getFee() {
        return fee;
    }

    public void setFee(Float fee) {
        this.fee = fee;
    }
}
