package com.bearxyz.domain.po;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by bearxyz on 2017/5/25.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseDomain implements Serializable {

    private static final long serialVersionUID = -5127674223271590019L;

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 36)
    private String id;

    @CreatedDate
    @Column
    private Date dateCeated;

    @CreatedBy
    @Column
    private String createdBy;

    @LastModifiedDate
    @Column
    private Date lastUpdated;

    @LastModifiedBy
    @Column
    private String lastModifiedBy;


    @Version
    private Long version;

    public String getId() {
        return id;
    }

    public void setId(String id){ this.id = id;}

    public Date getDateCeated() {
        return dateCeated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

}
