package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by bearxyz on 2017/8/28.
 */
@Entity
@Table(name = "T_COMMUNICATION_RECORD")
public class CommunicationRecord extends BaseDomain {

    private static final long serialVersionUID = 8758669697763642834L;
    @Column(length = 36)
    private String companyId = "";
    @Column(length = 500)
    private String question = "";
    @Column(length = 500)
    private String solution = "";
    @Column(length = 500)
    private String memo = "";
    @Column
    private Boolean successed = false;

    @Transient
    private String operator = "";

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
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

    public Boolean isSuccessed() {
        return successed;
    }

    public void setSuccessed(Boolean successed) {
        this.successed = successed;
    }
}
