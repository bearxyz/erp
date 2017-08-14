package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;
import org.activiti.engine.task.Task;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bearxyz on 2017/8/2.
 */
@Entity
@Table(name = "T_FOR_USE")
public class ForUse extends BaseDomain {

    private static final long serialVersionUID = 3533042830401089597L;
    @Column(length = 200)
    private String purpose = "";
    @Column(length = 50)
    private String processInstanceId = "";
    @Column
    private Boolean approved = false;

    @Transient
    private String goods = "";
    @Transient
    private String taskId;
    @Transient
    private String taskName;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "FOR_USE_ID")
    private List<ForUseItem> items = new ArrayList<>();

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

    public List<ForUseItem> getItems() {
        return items;
    }

    public void setItems(List<ForUseItem> items) {
        this.items = items;
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
}
