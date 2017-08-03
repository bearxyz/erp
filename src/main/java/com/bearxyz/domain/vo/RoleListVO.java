package com.bearxyz.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bearxyz on 2017/6/22.
 */
public class RoleListVO implements Serializable {

    private static final long serialVersionUID = -2887190107848668956L;

    private String type;

    List<RoleVO> list;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<RoleVO> getList() {
        return list;
    }

    public void setList(List<RoleVO> list) {
        this.list = list;
    }
}
