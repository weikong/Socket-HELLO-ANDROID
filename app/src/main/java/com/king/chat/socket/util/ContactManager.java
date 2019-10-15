package com.king.chat.socket.util;

import android.app.Activity;

import com.king.chat.socket.bean.ContactBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Activity栈管理
 *
 * @author luomin
 */
public class ContactManager {

    List<ContactBean> contactList = new ArrayList<>();

    private static class ContactManagerHolder{
        private static final ContactManager INSTANCE = new ContactManager();
    }

    /**
     * 单一实例
     */
    public static final ContactManager getInstance(){
        return ContactManagerHolder.INSTANCE;
    }

    public List<ContactBean> getContactList() {
        return contactList;
    }

    public void setContactList(List<ContactBean> contactList) {
        if (contactList != null){
            this.contactList.clear();
            this.contactList.addAll(contactList);
        }
    }
}
