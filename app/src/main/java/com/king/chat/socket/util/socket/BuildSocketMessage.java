package com.king.chat.socket.util.socket;

import android.text.TextUtils;

import com.king.chat.socket.ui.DBFlow.chatRecord.ChatRecordData;
import com.king.chat.socket.config.Config;
import com.king.chat.socket.ui.DBFlow.chatRecord.MessageChatType;

import java.util.UUID;

/**
 * Created by maesinfo on 2019/9/20.
 */

public class BuildSocketMessage {

    private static class BuildSocketMessageHolder{
        private static final BuildSocketMessage INSTANCE = new BuildSocketMessage();
    }

    /**
     * 单一实例
     */
    public static final BuildSocketMessage getInstance(){
        return BuildSocketMessageHolder.INSTANCE;
    }

    /**
     * 发送聊天内容
     *
     * 聊天消息类型
     * 1、文本；2、语音；3；视频；4、文件；5、链接；6、分享；7、红包
     * */
    /*public ChatRecordData buildContent(String sendMsg) {
        ChatRecordData chatRecordData = new ChatRecordData();
        chatRecordData.messageid = UUID.randomUUID().toString();
        chatRecordData.sourcesenderid = Config.userId;
        chatRecordData.messagefromid = Config.userId;
        chatRecordData.messagefromname = Config.userName;
        chatRecordData.messagetoid = Config.toUserId;
        chatRecordData.messagetoname = Config.toUserName;
        chatRecordData.messagetype = 9;
        chatRecordData.messagestate = 0;
        chatRecordData.messagetime = System.currentTimeMillis();
        chatRecordData.messagecontent = sendMsg;
        chatRecordData.messagechattype = MessageChatType.TYPE_TEXT;
        return chatRecordData;
    }*/

    /**
     * 发送聊天内容
     *
     * 聊天消息类型
     * 1、文本；2、语音；3；视频；4、文件；5、链接；6、分享；7、红包
     * */
    public ChatRecordData buildContent(String sendMsg,int chatType,String groupAccount) {
        ChatRecordData chatRecordData = new ChatRecordData();
        chatRecordData.messageid = UUID.randomUUID().toString();
        chatRecordData.sourcesenderid = Config.userId;
        chatRecordData.sourcesendername = Config.userName;
//        chatRecordData.messagefromid = groupAccount;
//        chatRecordData.messagefromname = Config.userName;
        chatRecordData.messagetoid = Config.toUserId;
        chatRecordData.messagetoname = Config.toUserName;
        chatRecordData.messagetype = 9;
        chatRecordData.messagestate = 0;
        chatRecordData.messagetime = System.currentTimeMillis();
        chatRecordData.messagecontent = sendMsg;
        chatRecordData.messagechattype = chatType;
        if (!TextUtils.isEmpty(groupAccount) && groupAccount.startsWith("G_")){
            chatRecordData.messagefromid = Config.toUserId;
            chatRecordData.messagefromname = Config.toUserName;
            chatRecordData.groupdata = 1;
        } else {
            chatRecordData.messagefromid = Config.userId;
            chatRecordData.messagefromname = Config.userName;
            chatRecordData.groupdata = 0;
        }
        return chatRecordData;
    }

    /**
     * 发送Ping包
     * */
    public ChatRecordData buildPing() {
        ChatRecordData chatRecordData = new ChatRecordData();
        chatRecordData.messageid = UUID.randomUUID().toString();
        chatRecordData.sourcesenderid = Config.userId;
        chatRecordData.messagefromid = Config.userId;
        chatRecordData.messagetoid = Config.SystemID;
        chatRecordData.messagetype = 1;
        chatRecordData.messagecontent = "PING";
        return chatRecordData;
    }

    /**
     * 连接成功后登陆
     * */
    public ChatRecordData buildLogin() {
        ChatRecordData chatRecordData = new ChatRecordData();
        chatRecordData.messageid = UUID.randomUUID().toString();
        chatRecordData.sourcesenderid = Config.userId;
        chatRecordData.messagefromid = Config.userId;
        chatRecordData.messagetoid = Config.SystemID;
        chatRecordData.messagetype = 4;
        chatRecordData.messagecontent = "Login";
        return chatRecordData;
    }

    /**
     * 取消连接Socket服务器
     * */
    public ChatRecordData buildDisconnect() {
        ChatRecordData chatRecordData = new ChatRecordData();
        chatRecordData.messageid = UUID.randomUUID().toString();
        chatRecordData.sourcesenderid = Config.userId;
        chatRecordData.messagefromid = Config.userId;
        chatRecordData.messagetoid = Config.SystemID;
        chatRecordData.messagetype = 3;
        chatRecordData.messagecontent = "断开连接";
        return chatRecordData;
    }

    /**
     * 接收到消息后回执服务器
     * */
    public ChatRecordData buildAckServer(String messageId) {
        ChatRecordData chatRecordData = new ChatRecordData();
        chatRecordData.messageid = messageId;
        chatRecordData.sourcesenderid = Config.userId;
        chatRecordData.messagefromid = Config.userId;
        chatRecordData.messagetoid = Config.SystemID;
        chatRecordData.messagetype = 5;
        chatRecordData.messagecontent = "回执";
        return chatRecordData;
    }
}
