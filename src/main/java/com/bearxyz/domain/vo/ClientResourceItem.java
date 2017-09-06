package com.bearxyz.domain.vo;

import java.io.Serializable;

/**
 * Created by bearxyz on 2017/9/6.
 */
public class ClientResourceItem implements Serializable {

    private static final long serialVersionUID = -5388523422105826780L;
    private String id;
    private String fileName;
    private String fileType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
