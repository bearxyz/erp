package com.bearxyz.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by bearxyz on 2017/6/24.
 */
public class BaseVO implements Serializable {

    private static final long serialVersionUID = -8855873688981330539L;

    private String id;

    private Date dateCeated;

    private String createdBy;

    private Date lastUpdated;

    private String lastModifiedBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDateCeated() {
        return dateCeated;
    }

    public void setDateCeated(Date dateCeated) {
        this.dateCeated = dateCeated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
}
