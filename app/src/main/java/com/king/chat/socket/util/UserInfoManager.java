package com.king.chat.socket.util;


import com.king.chat.socket.bean.ContactBean;

/**
 * Created by kongwei on 2017/3/10.
 */

public class UserInfoManager {

    private static UserInfoManager userInfoManager;
    private ContactBean contactBean = null;
    public static int SysAccountId = -1;
    public static String PATH_ACCOUNT = "ACCOUNT-FILE.OBJ";

    public static UserInfoManager getInstance() {
        if (userInfoManager == null)
            userInfoManager = new UserInfoManager();
        return userInfoManager;
    }

    public ContactBean getContactBean() {
        return contactBean;
    }

    public void setContactBean(ContactBean contactBean) {
        this.contactBean = contactBean;
    }

    public String getAccount() {
        return contactBean != null ? contactBean.getAccount() : "";
    }

    public int getAccountId() {
        return contactBean != null ? contactBean.getId() : SysAccountId;
    }

    public void logout() {
        setContactBean(null);
    }

    public boolean isLogin() {
        if (getContactBean() != null && getAccountId() > 0)
            return true;
        return false;
    }

}
