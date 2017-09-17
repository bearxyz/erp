package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by bearxyz on 2017/9/10.
 */
@Entity
@Table(name = "T_QA")
public class QA extends BaseDomain {

    private static final long serialVersionUID = 2795003665025130232L;
    @Column(length = 50)
    private String type="";
    @Column(length = 100)
    private String question="";
    @Column(length = 1000)
    private String answer="";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
