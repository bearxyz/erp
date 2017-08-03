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

    @Column(length = 200)
    private String purpose = "";

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "FOR_USE_ID")
    private List<ForUseItem> items = new ArrayList<>();

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public List<ForUseItem> getItems() {
        return items;
    }

    public void setItems(List<ForUseItem> items) {
        this.items = items;
    }
}
