package com.bearxyz.domain.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bearxyz on 2017/9/5.
 */
public class ClientResource implements Serializable {


    private static final long serialVersionUID = -3003421061607922729L;
    private String id;
    private String name;
    private String imageUrl;
    private List<ClientResourceItem> attachments=new ArrayList<>();

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<ClientResourceItem> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<ClientResourceItem> attachments) {
        this.attachments = attachments;
    }
}
