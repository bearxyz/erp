package com.bearxyz.domain.vo;

import com.bearxyz.domain.po.sys.Permission;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bearxyz on 2017/6/19.
 */
public class PermissionVO implements Serializable {

    private static final long serialVersionUID = 8237836820271488165L;

    Permission permission;

    List<PermissionVO> children;

    boolean hasPermission;

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public List<PermissionVO> getChildren() {
        return children;
    }

    public void setChildren(List<PermissionVO> children) {
        this.children = children;
    }

    public boolean isHasPermission() {
        return hasPermission;
    }

    public void setHasPermission(boolean hasPermission) {
        this.hasPermission = hasPermission;
    }
}
