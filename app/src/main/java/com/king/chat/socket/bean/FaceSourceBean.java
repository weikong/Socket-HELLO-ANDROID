package com.king.chat.socket.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by maesinfo on 2019/5/15.
 */

public class FaceSourceBean implements Serializable {

    public String zip;
    public String thumb;
    public String name;
    public List<MediaSrc> list;

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MediaSrc> getList() {
        return list;
    }

    public void setList(List<MediaSrc> list) {
        this.list = list;
    }
}
