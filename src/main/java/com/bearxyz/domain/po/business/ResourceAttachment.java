package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.*;

/**
 * Created by bearxyz on 2017/8/30.
 */
@Entity
@Table(name = "T_RESOURCE_ATTACHMENT")
public class ResourceAttachment extends BaseDomain {

    private static final long serialVersionUID = 6563288308341982232L;
    @Column(length = 200)
    private String name = "";
    @Column
    private Long fileSize = (long)0;
    @Column(length = 50)
    private String fileType = "";
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "BLOB")
    private byte[] fileContent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

}
