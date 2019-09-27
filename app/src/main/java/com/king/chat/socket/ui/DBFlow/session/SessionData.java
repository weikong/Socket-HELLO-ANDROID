package com.king.chat.socket.ui.DBFlow.session;

import com.king.chat.socket.ui.DBFlow.ChatDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

/**
 * Created by maesinfo on 2018/11/5.
 * 备注：DBFlow会根据你的类名自动生成一个表明，以此为例：
 * 这个类对应的表名为：UserData_Table，这是作者在实践中得出来的
 */

@Table(database = ChatDatabase.class)
public class SessionData extends BaseModel implements Serializable {
    /**
     * 消息发送者
     */
    @PrimaryKey
    public String messagefromid;

    /**
     * 消息发送者
     */
    @Column
    public String messagefromname;

    /**
     * 消息发送者头像
     */
    @Column
    public String messagefromavatar;


    /**
     * 消息唯一ID
     */
    @Column
    public String messageid;

    /**
     * 消息类型
     * 9：聊天内容； 1：Ping； 2：Connect；3：Disconnect； 4：Login； 5：ACK回执
     */
    @Column
    public int messagetype;//
    /**
     * 消息类型
     * {com.king.chat.socket.ui.DBFlow.chatRecord.MessageChatType}
     * 1、文本；2、语音；3；视频；4、文件；5、链接；6、分享；7、红包
     */
    @Column
    public int messagechattype;

    /**
     * 消息状态
     * 0：未接收；1：接收
     */
    @Column
    public int messagestate;//

    /**
     * 消息发送时间
     */
    @Column
    public long messagetime;//

    /**
     * 消息内容
     */
    @Column
    public String messagecontent;//

    /**
     * 消息未读数
     */
    @Column
    public int message_unread_count;//

    public String getMessagefromid() {
        return messagefromid;
    }

    public void setMessagefromid(String messagefromid) {
        this.messagefromid = messagefromid;
    }

    public String getMessagefromname() {
        return messagefromname;
    }

    public void setMessagefromname(String messagefromname) {
        this.messagefromname = messagefromname;
    }

    public String getMessagefromavatar() {
        return messagefromavatar;
    }

    public void setMessagefromavatar(String messagefromavatar) {
        this.messagefromavatar = messagefromavatar;
    }

    public String getMessageid() {
        return messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }

    public int getMessagetype() {
        return messagetype;
    }

    public void setMessagetype(int messagetype) {
        this.messagetype = messagetype;
    }

    public int getMessagestate() {
        return messagestate;
    }

    public void setMessagestate(int messagestate) {
        this.messagestate = messagestate;
    }

    public long getMessagetime() {
        return messagetime;
    }

    public void setMessagetime(long messagetime) {
        this.messagetime = messagetime;
    }

    public String getMessagecontent() {
        return messagecontent;
    }

    public void setMessagecontent(String messagecontent) {
        this.messagecontent = messagecontent;
    }

    public int getMessage_unread_count() {
        return message_unread_count;
    }

    public void setMessage_unread_count(int message_unread_count) {
        this.message_unread_count = message_unread_count;
    }

    public int getMessagechattype() {
        return messagechattype;
    }

    public void setMessagechattype(int messagechattype) {
        this.messagechattype = messagechattype;
    }
}
