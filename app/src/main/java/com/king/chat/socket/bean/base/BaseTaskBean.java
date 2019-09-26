package com.king.chat.socket.bean.base;

import java.io.Serializable;

/**
 * Created by maesinfo on 2019/5/15.
 */

public class BaseTaskBean implements Serializable {

    int code;
    String message;
    String data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
