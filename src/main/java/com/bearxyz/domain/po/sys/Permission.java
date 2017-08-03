package com.bearxyz.domain.po.sys;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by bearxyz on 2017/6/1.
 */
@Entity
@Table(name = "SYS_PERMISSION")
public class Permission extends BaseDomain {

    private static final long serialVersionUID = -6499429737376344070L;

    @Column(length = 50)
    @NotNull(message = "权限名称不能为空")
    private String name;

    @Column(unique = true)
    @NotNull(message = "权限标识不能为空")
    private String mask;

    @Column(length = 50)
    private String css;

    @Column
    @NotNull(message = "权限地址不能为空")
    private String url;

    @Column(length = 36)
    private String parentId;

    @Column(length = 50)
    private String type;

    @Column
    private Integer seq = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }
}
