package com.bearxyz.common;

import java.io.Serializable;

/**
 * Created by bearxyz on 2017/6/20.
 */
public class ActionResponse implements Serializable {

    private static final long serialVersionUID = 8804930602351547942L;
    private boolean success;
    private String msg;
    private String id;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
