package com.king.chat.socket.bean;

import java.io.Serializable;

/**
 * Created by maesinfo on 2019/5/15.
 */

public class BiaoQingBean implements Serializable {

    private int id;
    private String url;
    private String localPath;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
