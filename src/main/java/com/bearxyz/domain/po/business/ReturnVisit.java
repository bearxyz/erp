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
@Table(name = "T_RETURN_VISIT")
public class ReturnVisit extends BaseDomain {

    private static final long serialVersionUID = -6876465331292526443L;
    @Column(length = 36)
    private String companyId = "";
    @Column(length = 500)
    private String question = "";
    @Column(length = 500)
    private String solution = "";
    @Column(length = 500)
    private String memo = "";

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

}
