package com.king.chat.socket.bean;

import java.io.Serializable;

/**
 * Created by kongwei on 2017/2/15.
 */

public class UploadFileBean implements Serializable {

    private String fileName;
    private String suffix;
    private String filePathMD5;
    private String filePathMD5Thumb;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getFilePathMD5() {
        return filePathMD5;
    }

    public void setFilePathMD5(String filePathMD5) {
        this.filePathMD5 = filePathMD5;
    }

    public String getFilePathMD5Thumb() {
        return filePathMD5Thumb;
    }

    public void setFilePathMD5Thumb(String filePathMD5Thumb) {
        this.filePathMD5Thumb = filePathMD5Thumb;
    }
}
