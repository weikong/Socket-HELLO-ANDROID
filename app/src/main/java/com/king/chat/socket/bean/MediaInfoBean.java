package com.king.chat.socket.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by xinzhendi-031 on 2017/12/13.
 */
public class MediaInfoBean implements Serializable ,Parcelable{
    public String duration;
    public String width;
    public String height;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(duration);
        dest.writeString(width);
        dest.writeString(height);
    }


    public static final Creator<MediaInfoBean> CREATOR = new Creator() {

        @Override
        public MediaInfoBean createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            MediaInfoBean p = new MediaInfoBean();
            p.setDuration(source.readString());
            p.setWidth(source.readString());
            p.setHeight(source.readString());
            return p;
        }

        @Override
        public MediaInfoBean[] newArray(int size) {
            // TODO Auto-generated method stub
            return new MediaInfoBean[size];
        }
    };

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getWidth() {
        return width;
    }

    public int getWidthInt() {
        if (TextUtils.isEmpty(width))
            return 0;
        try {
            return Integer.parseInt(width);
        } catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public int getHeightInt() {
        if (TextUtils.isEmpty(height))
            return 0;
        try {
            return Integer.parseInt(height);
        } catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
