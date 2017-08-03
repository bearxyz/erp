package com.bearxyz.domain.po.sys;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by bearxyz on 2017/6/1.
 */
@Entity
@Table(name = "SYS_ROLE")
public class Role extends BaseDomain {

    private static final long serialVersionUID = -2957798252388251456L;

    @Column(length = 50)
    @NotNull(message = "角色名称不能为空")
    private String name;
    @Column(length = 50, unique = true)
    @NotNull(message = "角色标识不能为空")
    private String mask;
    @Column(length = 50)
    private String type;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "SYS_PERMISSION_ROLE",
            joinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "PERMISSION_ID", referencedColumnName = "id")}
    )
    private Set<Permission> permissions = new HashSet<>();

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

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
