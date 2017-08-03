package com.bearxyz.common;

import java.io.Serializable;

/**
 * Created by bearxyz on 2017/6/6.
 */
public class TreeNode implements Serializable {

    private String id;
    private String text;
    private boolean children = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isChildren() {
        return children;
    }

    public void setChildren(boolean children) {
        this.children = children;
    }
}
