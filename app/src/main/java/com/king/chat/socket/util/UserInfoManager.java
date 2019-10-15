package com.king.chat.socket.util;


import com.king.chat.socket.bean.ContactBean;
import com.king.chat.socket.config.Config;

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
        if (contactBean != null){
            Config.userId = contactBean.getAccount();
            Config.userName = contactBean.getName();
        } else {
            Config.userId = "";
            Config.userName = "";
        }
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
