package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by bearxyz on 2017/6/1.
 */
@Entity
public class GodownEntryDetail extends BaseDomain {

    private static final long serialVersionUID = 7142364714041465575L;

    @Column
    private Long entryId;

    public Long getEntryId() {
        return entryId;
    }

    public void setEntryId(Long entryId) {
        this.entryId = entryId;
    }
}
