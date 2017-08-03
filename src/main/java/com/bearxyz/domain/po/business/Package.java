package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bearxyz on 2017/7/24.
 */

@Entity
@Table(name = "T_PACKAGE")
public class Package extends BaseDomain {

    private static final long serialVersionUID = 2238791613867000784L;

    @Column(length = 50)
    private String spec = "";
    @Column(length = 50)
    private String type = "";
    @Column(length = 10)
    private String unit = "";
    @Column
    private Integer ammount = 0;
    @Column
    private Integer stock = 0;
    @Column(length = 36)
    private String companyId = "";
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "CLOB")
    private String brief = "";

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "T_GOODS_PACKAGE",
            joinColumns = {@JoinColumn(name = "GOODS_ID", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "PACKAGE_ID", referencedColumnName = "id")}
    )
    private List<Goods> goods = new ArrayList<>();

    @Transient
    private String unitName = "";


    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
