package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;
import sun.security.util.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by bearxyz on 2017/7/27.
 */
@Entity
@Table(name = "T_TRACK")
public class Track extends BaseDomain {

    private static final long serialVersionUID = -6389478285807754095L;
    @Column(length = 36)
    private String companyId = "";
    @Column(length = 1000)
    private String analysis = "";
    @Column(length = 1000)
    private String solution = "";
    @Column(length = 1000)
    private String progress = "";
    @Column(length = 1000)
    private String returnVisit = "";

    @Transient
    private String tracker = "";

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getReturnVisit() {
        return returnVisit;
    }

    public void setReturnVisit(String returnVisit) {
        this.returnVisit = returnVisit;
    }

    public String getTracker() {
        return tracker;
    }

    public void setTracker(String tracker) {
        this.tracker = tracker;
    }
}
