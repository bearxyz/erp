package com.bearxyz.domain.vo;

import java.io.Serializable;

/**
 * Created by bearxyz on 2017/6/24.
 */
public class NoticeVO extends BaseVO {

    private static final long serialVersionUID = -1518353419860478677L;

    private String title;
    private String type;

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
}
