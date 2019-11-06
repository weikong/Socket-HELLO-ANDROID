package com.king.chat.socket.bean;

import java.io.Serializable;

/**
 * Created by maesinfo on 2019/5/15.
 */

public class MineBean implements Serializable {

    private int drawable;
    private String title;
    private boolean isMore = true;
    /**
     * 0:正常item
     * 1:头部（头像）
     * 2:尾部（退出）
     */
    private int type;

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
