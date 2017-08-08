package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;

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
    @Column(length = 36)
    private String userId = "";

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<ForUseItem> getItems() {
        return items;
    }

    public void setItems(List<ForUseItem> items) {
        this.items = items;
    }
}
