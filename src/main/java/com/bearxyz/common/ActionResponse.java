package com.bearxyz.common;

import java.io.Serializable;

/**
 * Created by bearxyz on 2017/6/20.
 */
public class ActionResponse implements Serializable {

    private boolean success;
    private String msg;

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
}
