package com.bearxyz.domain.po.sys;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by bearxyz on 2017/6/3.
 */

@Entity
@Table(name = "SYS_DICT")
public class Dict extends BaseDomain {

    private static final long serialVersionUID = 7734796189423776410L;

    @Column(length = 36)
    private String parentId = " ";

    @Column(length = 50, unique = true)
    private String mask;

    @Column(length = 50)
    private String parentMask;

    @Column(length = 50)
    private String name;

    @Column(length = 50)
    private String value;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getParentMask() {
        return parentMask;
    }

    public void setParentMask(String parentMask) {
        this.parentMask = parentMask;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
