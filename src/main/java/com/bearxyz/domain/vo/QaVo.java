package com.bearxyz.domain.vo;

import com.bearxyz.domain.po.business.QA;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bearxyz on 2017/9/10.
 */
public class QaVo implements Serializable {

    private static final long serialVersionUID = -775009899940539696L;
    private String type = "";
    private List<QA> qas = new ArrayList<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<QA> getQas() {
        return qas;
    }

    public void setQas(List<QA> qas) {
        this.qas = qas;
    }
}
