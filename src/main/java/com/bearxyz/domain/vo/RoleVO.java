package com.bearxyz.domain.vo;

import java.io.Serializable;

/**
 * Created by bearxyz on 2017/6/22.
 */
public class RoleVO implements Serializable {

    private static final long serialVersionUID = 8308310737782020949L;

    private String id;
    private String name;
    private String mask;
    private boolean hasRole;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public boolean isHasRole() {
        return hasRole;
    }

    public void setHasRole(boolean hasRole) {
        this.hasRole = hasRole;
    }
}
