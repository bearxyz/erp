package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by bearxyz on 2017/6/1.
 */
@Entity
@Table(name = "T_NOTICE")
public class Notice extends BaseDomain {

    private static final long serialVersionUID = 5557819301111371811L;

    @Column
    @NotNull(message = "标题不能为空")
    private String title;
    @Column
    @NotNull(message = "类型不能为空")
    private String type;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "CLOB")
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
