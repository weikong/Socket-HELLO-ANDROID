package com.king.chat.socket.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by xinzhendi-031 on 2017/12/13.
 */
public class FileItem implements Serializable ,Parcelable{
    public String imageId;
    public String fileName;
    public String filePath;
    public String fileType;
    public String fileCover;
    public long fileSize;
    public int fileDuration;
    public long fileDate;
    public boolean isCheck = false;
    public int isAlbumMedia = 1; //0:不是相册中的多媒体文件；1：相册中的多媒体文件
    int width;
    int height;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageId);
        dest.writeString(fileName);
        dest.writeString(filePath);
        dest.writeString(fileType);
        dest.writeString(fileCover);
        dest.writeLong(fileSize);
        dest.writeInt(fileDuration);
        dest.writeInt(isAlbumMedia);
        dest.writeLong(fileDate);
        dest.writeInt(width);
        dest.writeInt(height);
    }


    public static final Creator<FileItem> CREATOR = new Creator() {

        @Override
        public FileItem createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            FileItem p = new FileItem();
            p.setImageId(source.readString());
            p.setFileName(source.readString());
            p.setFilePath(source.readString());
            p.setFileType(source.readString());
            p.setFileCover(source.readString());
            p.setFileSize(source.readLong());
            p.setFileDuration(source.readInt());
            p.setFileDate(source.readLong());
            p.setIsAlbumMedia(source.readInt());
            p.setWidth(source.readInt());
            p.setHeight(source.readInt());
            return p;
        }

        @Override
        public FileItem[] newArray(int size) {
            // TODO Auto-generated method stub
            return new FileItem[size];
        }
    };

    public FileItem() {
    }

    public FileItem(String imageId, String filePath, String fileName) {
        this.imageId = imageId;
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public FileItem(String imageId, String filePath, String fileName, long fileDate, String type){
        this.imageId = imageId;
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileDate = fileDate;
        this.fileType = type;
    }

    public FileItem(String imageId, String filePath, String fileName, long fileDate, String type, int fileDuration){
        this.imageId = imageId;
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileDate = fileDate;
        this.fileType = type;
        this.fileDuration = fileDuration;
    }

    public String getImageId() {
        return imageId;
    }

    public FileItem setImageId(String imageId) {
        this.imageId = imageId;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public FileItem setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public FileItem setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public FileItem setCheck(boolean check) {
        isCheck = check;
        return this;
    }

    public String getFileType() {
        return fileType;
    }

    public FileItem setFileType(String fileType) {
        this.fileType = fileType;
        return this;
    }

    public String getFileCover() {
        return fileCover;
    }

    public FileItem setFileCover(String fileCover) {
        this.fileCover = fileCover;
        return this;
    }

    public long getFileSize() {
        return fileSize;
    }

    public FileItem setFileSize(long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public int getFileDuration() {
        return fileDuration;
    }

    public FileItem setFileDuration(int fileDuration) {
        this.fileDuration = fileDuration;
        return this;
    }

    public int getIsAlbumMedia() {
        return isAlbumMedia;
    }

    public void setIsAlbumMedia(int isAlbumMedia) {
        this.isAlbumMedia = isAlbumMedia;
    }

    public long getFileDate() {
        return fileDate;
    }

    public void setFileDate(long fileDate) {
        this.fileDate = fileDate;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
