package com.bearxyz.domain.vo;

import java.io.Serializable;

/**
 * Created by bearxyz on 2017/9/5.
 */
public class ClientResource implements Serializable {


    private static final long serialVersionUID = -3003421061607922729L;
    private String id;
    private String name;
    private String imageUrl;

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
}
