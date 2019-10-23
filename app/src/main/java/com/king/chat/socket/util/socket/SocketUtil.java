package com.king.chat.socket.util.socket;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.king.chat.socket.App;
import com.king.chat.socket.bean.ContactBean;
import com.king.chat.socket.ui.DBFlow.chatRecord.ChatRecordData;
import com.king.chat.socket.ui.DBFlow.chatRecord.DBChatRecordImpl;
import com.king.chat.socket.config.Config;
import com.king.chat.socket.ui.DBFlow.session.DBSessionImpl;
import com.king.chat.socket.ui.DBFlow.session.SessionData;
import com.king.chat.socket.util.BroadCastUtil;
import com.king.chat.socket.util.FilterTimeOutManager;
import com.king.chat.socket.util.Logger;
import com.king.chat.socket.util.NotificationUtils;
import com.king.chat.socket.util.ToastUtil;
import com.king.chat.socket.util.UserInfoManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by maesinfo on 2019/9/20.
 */

public class SocketUtil {

    private static final String TAG = "SocketUtil";

    /**
     * connect:
     * 0、连接成功
     * 1、连接中
     * 2、未连接
     * */
    public static final int IM_CONNECTED = 0;
    public static final int IM_CONNECTING = 1;
    public static final int IM_DISCONNECT = 2;
    public static int IM_CONNECT_STATE = IM_DISCONNECT;

    private Socket socket;
    private PrintWriter printWriter;
    private BufferedReader in;
    private ExecutorService mExecutorService = null;
    private ExecutorService mPingExecutorService = null;
    private String receiveMsg;
    private boolean isPing = true;
    private Handler mHandler;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 101){
                Logger.e(TAG,"101 IM_CONNECT_STATE = "+IM_CONNECT_STATE);
                if (IM_CONNECT_STATE == IM_CONNECTING){
                    BroadCastUtil.sendActionBroadCast(App.getInstance(),BroadCastUtil.ACTION_DISCONNECT);
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            connect();
                        }
                    },2000);
                }
            }
        }
    };

    public SocketUtil() {
        mPingExecutorService = Executors.newSingleThreadExecutor();
        mExecutorService = Executors.newCachedThreadPool();
    }

    private static class SocketUtilHolder {
        private static final SocketUtil INSTANCE = new SocketUtil();
    }

    /**
     * 单一实例
     */
    public static final SocketUtil getInstance() {
        return SocketUtilHolder.INSTANCE;
    }

    public void setmHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }

    /**
     * 连接服务器
     */
    public void connect() {
        isPing = true;
        mExecutorService.execute(new connectService());  //在一个新的线程中请求 Socket 连接
        mPingExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                if (!isPing) {
                    return;
                }
                while (isPing) {
                    try {
                        Thread.sleep(30 * 1000);
                        sendPing();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 发送聊天内容
     */
    public ChatRecordData sendContentFilePre(String sendMsg, int chatType, SessionData sessionDataAvatar,int groupData) {
        // TODO: 2019/9/20 保存数据库
        ChatRecordData chatRecordData = BuildSocketMessage.getInstance().buildContent(sendMsg,chatType,sessionDataAvatar.getMessagefromid());
        long insert = DBChatRecordImpl.getInstance().insertChatRecord(chatRecordData);
        if (insert > 0) {
            SessionData sessionData = new SessionData();
            sessionData.setMessagecontent(chatRecordData.getMessagecontent());
            sessionData.setMessagefromid(chatRecordData.getMessagetoid());
            sessionData.setMessagefromname(chatRecordData.getMessagetoname());
            String headerAvatar = "";
            if (sessionDataAvatar != null)
                headerAvatar = sessionDataAvatar.getMessagefromavatar();
            if (!TextUtils.isEmpty(headerAvatar))
                sessionData.setMessagefromavatar(headerAvatar);
            sessionData.setMessageid(chatRecordData.getMessageid());
            sessionData.setMessagestate(chatRecordData.getMessagestate());
            sessionData.setMessagetime(chatRecordData.getMessagetime());
            sessionData.setMessagetype(chatRecordData.getMessagetype());
            sessionData.setMessagechattype(chatRecordData.getMessagechattype());
            sessionData.setGroupdata(groupData);
            sessionData.setSourcesenderid(chatRecordData.getSourcesenderid());
            sessionData.setSourcesendername(chatRecordData.getSourcesendername());
            boolean update = DBSessionImpl.getInstance().updateSession(sessionData);
            BroadCastUtil.sendActionBroadCast(App.getInstance(), BroadCastUtil.ACTION_RECIEVE_MESSAGE);
            if (mHandler != null) {
                Message message = Message.obtain();
                message.what = 1;
                message.obj = chatRecordData;
                mHandler.sendMessage(message);
            }
            return chatRecordData;
        }
        ToastUtil.show("数据存储失败");
        Logger.e(TAG, "数据存储失败");
        return null;
    }

    /**
     * 发送聊天内容
     */
    public void sendContentFilePost(ChatRecordData chatRecordData) {
        mExecutorService.execute(new sendService(chatRecordData));
    }

    /**
     * 发送聊天内容
     */
    public void sendContent(String sendMsg, int chatType, SessionData sessionDataAvatar,int groupData) {
        // TODO: 2019/9/20 保存数据库
        ChatRecordData chatRecordData = BuildSocketMessage.getInstance().buildContent(sendMsg,chatType,sessionDataAvatar.getMessagefromid());
        long insert = DBChatRecordImpl.getInstance().insertChatRecord(chatRecordData);
        if (insert > 0) {
            SessionData sessionData = new SessionData();
            sessionData.setMessagecontent(chatRecordData.getMessagecontent());
            sessionData.setMessagefromid(chatRecordData.getMessagetoid());
            sessionData.setMessagefromname(chatRecordData.getMessagetoname());
            String headerAvatar = "";
            if (sessionDataAvatar != null)
                headerAvatar = sessionDataAvatar.getMessagefromavatar();
            if (!TextUtils.isEmpty(headerAvatar))
                sessionData.setMessagefromavatar(headerAvatar);
            sessionData.setMessageid(chatRecordData.getMessageid());
            sessionData.setMessagestate(chatRecordData.getMessagestate());
            sessionData.setMessagetime(chatRecordData.getMessagetime());
            sessionData.setMessagetype(chatRecordData.getMessagetype());
            sessionData.setMessagechattype(chatRecordData.getMessagechattype());
            sessionData.setGroupdata(groupData);
            sessionData.setSourcesenderid(chatRecordData.getSourcesenderid());
            sessionData.setSourcesendername(chatRecordData.getSourcesendername());
            boolean update = DBSessionImpl.getInstance().updateSession(sessionData);
            BroadCastUtil.sendActionBroadCast(App.getInstance(), BroadCastUtil.ACTION_RECIEVE_MESSAGE);
            if (mHandler != null) {
                Message message = Message.obtain();
                message.what = 1;
                message.obj = chatRecordData;
                mHandler.sendMessage(message);
                mExecutorService.execute(new sendService(chatRecordData));
            }
            return;
        }
        ToastUtil.show("数据存储失败");
        Logger.e(TAG, "数据存储失败");
    }

    /**
     * 发送聊天内容
     */
    public void reSendContent(ChatRecordData chatRecordData) {
        // TODO: 2019/9/20 保存数据库
        chatRecordData.setMessagestate(0);
        chatRecordData.setMessagetype(9);
        boolean update = DBChatRecordImpl.getInstance().updateChatRecord(chatRecordData);
        if (update) {
            BroadCastUtil.sendActionBroadCast(App.getInstance(), BroadCastUtil.ACTION_RECIEVE_MESSAGE);
            if (mHandler != null) {
                Message message = Message.obtain();
                message.what = 2;
                message.obj = chatRecordData;
                mHandler.sendMessage(message);
                mExecutorService.execute(new sendService(chatRecordData));
            }
            return;
        }
        ToastUtil.show("数据存储失败");
        Logger.e(TAG, "数据存储失败");
    }

    /**
     * 发送Ping包
     */
    public void sendPing() {
        mExecutorService.execute(new sendService(BuildSocketMessage.getInstance().buildPing()));
    }

    /**
     * 连接成功后登陆
     */
    public void sendLogin() {
        if (printWriter != null){
            ChatRecordData loginBean = BuildSocketMessage.getInstance().buildLogin();
            printWriter.println(JSON.toJSONString(loginBean)); //走网络，向服务器发消息
//            FilterTimeOutManager.getInstance().scheduledFuture(loginBean);
            handler.sendEmptyMessageDelayed(101,5*1000);
        }
    }

    /**
     * 取消连接Socket服务器
     */
    public void disconnect() {
        mExecutorService.execute(new sendService(BuildSocketMessage.getInstance().buildDisconnect()));
        isPing = false;
    }

    /**
     * 接收到消息后回执服务器
     */
    public void ackServer(String messageId) {
        mExecutorService.execute(new sendService(BuildSocketMessage.getInstance().buildAckServer(messageId)));
    }

    /**
     * 发送服务接口
     */
    private class sendService implements Runnable {
        private ChatRecordData baseBean;

        sendService(ChatRecordData baseBean) {
            this.baseBean = baseBean;
        }

        @Override
        public void run() {
            if (printWriter != null)
                printWriter.println(JSON.toJSONString(baseBean));
            if (baseBean.getMessagetype() == 9){
                FilterTimeOutManager.getInstance().scheduledFuture(baseBean);
            }
        }
    }

    public boolean isConnect(){
        if (socket != null){
            return socket.isConnected();
        }
        return false;
    }

    public void closeSocket(){
        IM_CONNECT_STATE = IM_DISCONNECT;
        try {
            if (in != null){
                in.close();
                in = null;
            }
            if (printWriter != null){
                printWriter.close();
                printWriter = null;
            }
            if (socket != null){
                socket.close();
                socket = null;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 连接服务器接口
     */
    private class connectService implements Runnable {
        @Override
        public void run() {//可以考虑在此处添加一个while循环，结合下面的catch语句，实现Socket对象获取失败后的超时重连，直到成功建立Socket连接
            try {
                IM_CONNECT_STATE = IM_CONNECTING;
                BroadCastUtil.sendActionBroadCast(App.getInstance(),BroadCastUtil.ACTION_CONNECTING);
//                Socket socket = new Socket(Config.HOST, Config.PORT);      //步骤一
                if (socket != null){
                    try {
                        socket.close();
                        socket = null;
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
                socket = new Socket();
                // 设置connect timeout 为30000毫秒
                socket.connect(new InetSocketAddress(Config.HOST, Config.PORT), Config.SOCKET_CONNECT_TIMEOUT);
//                socket.setSoTimeout(Config.SOCKET_SO_TIMEOUT);
                printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(   //步骤二
                        socket.getOutputStream(), "UTF-8")), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                receiveMsg();
            } catch (Exception e) {
                isPing = false;
                IM_CONNECT_STATE = IM_DISCONNECT;
                Log.e(TAG, ("connectService:" + e.getMessage()));   //如果Socket对象获取失败，即连接建立失败，会走到这段逻辑
            }
        }
    }

    /**
     * 接收消息
     */
    private void receiveMsg() {
        try {
            while (isPing) {
                //步骤三
                if (in != null && (receiveMsg = in.readLine()) != null) {
//                  receiveMsg = in.readLine();
                    if (receiveMsg != null) {
                        try {
                            ChatRecordData baseBean = JSONObject.parseObject(receiveMsg, ChatRecordData.class);
                            Log.e(TAG, "receiveMsg: " + receiveMsg);
                            /**
                             * type 9：聊天内容； 1：Ping； 2：Connect；3：Disconnect； 4：Login
                             * */
                            int type = baseBean.getMessagetype();
                            Message message;
                            switch (type) {
                                case 1: //Ping
                                    break;
                                case 2: //Connect
                                    sendLogin();
                                    break;
                                case 3: //Disconnect
                                    if (mHandler != null) {
                                        message = Message.obtain();
                                        message.what = 1;
                                        message.obj = baseBean;
                                        mHandler.sendMessage(message);
                                    }
                                    break;
                                case 4: //Login
                                    Log.e(TAG, "receiveMsg: " + "Login");
                                    handler.removeMessages(101);
                                    IM_CONNECT_STATE = IM_CONNECTED;
                                    BroadCastUtil.sendActionBroadCast(App.getInstance(),BroadCastUtil.ACTION_CONNECTED);
                                    break;
                                case 5: //接收到消息后回执服务器
                                    String messageId = baseBean.getMessageid();
                                    ChatRecordData chatRecordData = DBChatRecordImpl.getInstance().queryChatRecordByMessageId(messageId);
                                    if (chatRecordData == null)
                                        return;
                                    if (!baseBean.getMessagecontent().equals(chatRecordData.getMessagecontent()))
                                        chatRecordData.setMessagecontent(baseBean.getMessagecontent());
                                    chatRecordData.setMessagestate(baseBean.getMessagestate());
                                    boolean updateAck = DBChatRecordImpl.getInstance().updateChatRecord(chatRecordData);
                                    FilterTimeOutManager.getInstance().scheduledNotTimeOut(chatRecordData);
                                    if (mHandler != null) {
                                        message = Message.obtain();
                                        message.what = 2;
                                        message.obj = chatRecordData;
                                        mHandler.sendMessage(message);
                                    }
                                    break;
                                case 6: //离线数据
                                    List<ChatRecordData> chatRecordDataList = JSONObject.parseArray(baseBean.getMessagecontent(), ChatRecordData.class);
                                    ChatRecordData lastChatRecordData = null;
                                    for (ChatRecordData item : chatRecordDataList) {
                                        ChatRecordData crd = DBChatRecordImpl.getInstance().queryChatRecordByMessageId(item.getMessageid());
                                        long insert;
                                        if (crd == null){
                                            insert = DBChatRecordImpl.getInstance().insertChatRecord(item);
                                        } else {
                                            DBChatRecordImpl.getInstance().updateChatRecord(item);
                                            insert = 1;
                                        }
//                                        long insert = DBChatRecordImpl.getInstance().insertChatRecord(item);
                                        if (insert > 0) {
                                            lastChatRecordData = item;
                                            // 回执服务器
                                            ackServer(item.getMessageid());

                                            //更新Session表
                                            SessionData sessionData = new SessionData();
                                            sessionData.setMessagecontent(item.getMessagecontent());
                                            sessionData.setMessagefromid(item.getMessagefromid());
                                            sessionData.setMessagefromname(item.getMessagefromname());
                                            sessionData.setMessagefromavatar(item.getMessagefromavatar());
                                            sessionData.setMessageid(item.getMessageid());
                                            sessionData.setMessagestate(item.getMessagestate());
                                            sessionData.setMessagetime(item.getMessagetime());
                                            sessionData.setMessagetype(item.getMessagetype());
                                            sessionData.setMessagechattype(item.getMessagechattype());
                                            sessionData.setGroupdata(item.getGroupdata());
                                            sessionData.setSourcesenderid(item.getSourcesenderid());
                                            sessionData.setSourcesendername(item.getSourcesendername());
                                            String fromid = item.getMessagefromid();
                                            SessionData sessionData1 = DBSessionImpl.getInstance().querySessionDataByFromId(fromid);
                                            if (sessionData1 != null) {
                                                sessionData.setMessage_unread_count(sessionData1.getMessage_unread_count() + 1);
                                            } else {
                                                sessionData.setMessage_unread_count(1);
                                            }
                                            boolean update = DBSessionImpl.getInstance().updateSession(sessionData);
                                        }
                                    }
                                    // 2019/9/20 更新UI
                                    if (mHandler != null) {
                                        mHandler.sendEmptyMessage(3);
                                    } else {
                                        if (lastChatRecordData != null) {
                                            NotificationUtils.getInstance().sendNotification(lastChatRecordData.getMessagefromname(), lastChatRecordData.getMessagecontent());
                                        }
                                    }
                                    BroadCastUtil.sendActionBroadCast(App.getInstance(), BroadCastUtil.ACTION_RECIEVE_MESSAGE);
                                    break;
                                case 7: //群通知消息
                                    long insertNotice = DBChatRecordImpl.getInstance().insertChatRecord(baseBean);
                                    // TODO: 2019/9/20 回执服务器
                                    ackServer(baseBean.getMessageid());
                                    if (insertNotice > 0) {
                                        //更新Session表
                                        SessionData sessionData = new SessionData();
                                        sessionData.setMessagecontent(baseBean.getMessagecontent());
                                        sessionData.setMessagefromid(baseBean.getMessagefromid());
                                        sessionData.setMessagefromname(baseBean.getMessagefromname());
                                        sessionData.setMessagefromavatar(baseBean.getMessagefromavatar());
                                        sessionData.setMessageid(baseBean.getMessageid());
                                        sessionData.setMessagestate(baseBean.getMessagestate());
                                        sessionData.setMessagetime(baseBean.getMessagetime());
                                        sessionData.setMessagetype(baseBean.getMessagetype());
                                        sessionData.setMessagechattype(baseBean.getMessagechattype());
                                        sessionData.setGroupdata(baseBean.getGroupdata());

                                        // 2019/9/20 更新UI
                                        String fromid = baseBean.getMessagefromid();
                                        if (!TextUtils.isEmpty(fromid) && fromid.equals(Config.toUserId) && mHandler != null) {
                                            //接收的消息与正在聊天的人是相同的
                                            message = Message.obtain();
                                            message.what = 1;
                                            message.obj = baseBean;
                                            mHandler.sendMessage(message);
                                        } else {
                                            SessionData sessionData1 = DBSessionImpl.getInstance().querySessionDataByFromId(fromid);
                                            if (sessionData1 != null) {
                                                sessionData.setMessage_unread_count(sessionData1.getMessage_unread_count() + 1);
                                            } else {
                                                sessionData.setMessage_unread_count(1);
                                            }
                                            NotificationUtils.getInstance().sendNotification(baseBean.getMessagefromname(), baseBean.getMessagecontent());
                                        }
                                        boolean update = DBSessionImpl.getInstance().updateSession(sessionData);
                                        BroadCastUtil.sendActionBroadCast(App.getInstance(), BroadCastUtil.ACTION_RECIEVE_MESSAGE);
                                    }
                                    break;
                                case 9: //聊天内容
                                    ChatRecordData crd = DBChatRecordImpl.getInstance().queryChatRecordByMessageId(baseBean.getMessageid());
                                    long insert;
                                    if (crd == null){
                                        insert = DBChatRecordImpl.getInstance().insertChatRecord(baseBean);
                                    } else {
                                        DBChatRecordImpl.getInstance().updateChatRecord(baseBean);
                                        insert = 1;
                                    }
                                    if (insert > 0) {
                                        //更新Session表
                                        SessionData sessionData = new SessionData();
                                        sessionData.setMessagecontent(baseBean.getMessagecontent());
                                        sessionData.setMessagefromid(baseBean.getMessagefromid());
                                        sessionData.setMessagefromname(baseBean.getMessagefromname());
                                        sessionData.setMessagefromavatar(baseBean.getMessagefromavatar());
                                        sessionData.setMessageid(baseBean.getMessageid());
                                        sessionData.setMessagestate(baseBean.getMessagestate());
                                        sessionData.setMessagetime(baseBean.getMessagetime());
                                        sessionData.setMessagetype(baseBean.getMessagetype());
                                        sessionData.setMessagechattype(baseBean.getMessagechattype());
                                        sessionData.setGroupdata(baseBean.getGroupdata());
                                        sessionData.setSourcesenderid(baseBean.getSourcesenderid());
                                        sessionData.setSourcesendername(baseBean.getSourcesendername());
                                        // TODO: 2019/9/20 回执服务器
                                        ackServer(baseBean.getMessageid());
                                        // 2019/9/20 更新UI
                                        String fromid = baseBean.getMessagefromid();
                                        if (!TextUtils.isEmpty(fromid) && fromid.equals(Config.toUserId) && mHandler != null) {
                                            //接收的消息与正在聊天的人是相同的
                                            message = Message.obtain();
                                            message.what = 1;
                                            message.obj = baseBean;
                                            mHandler.sendMessage(message);
                                        } else {
                                            SessionData sessionData1 = DBSessionImpl.getInstance().querySessionDataByFromId(fromid);
                                            if (sessionData1 != null) {
                                                sessionData.setMessage_unread_count(sessionData1.getMessage_unread_count() + 1);
                                            } else {
                                                sessionData.setMessage_unread_count(1);
                                            }
                                            NotificationUtils.getInstance().sendNotification(baseBean.getMessagefromname(), baseBean.getMessagecontent());
                                        }
                                        boolean update = DBSessionImpl.getInstance().updateSession(sessionData);
                                        BroadCastUtil.sendActionBroadCast(App.getInstance(), BroadCastUtil.ACTION_RECIEVE_MESSAGE);
                                    }
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException e) {
//            java.net.SocketTimeoutException: Read timed out
//            java.net.SocketException: Software caused connection abort
//            receiveMsg: recvfrom failed: ECONNRESET (Connection reset by peer)
            Log.e(TAG, "receiveMsg: " + ((e != null) ? e.getMessage() : ""));
            isPing = false;
            IM_CONNECT_STATE = IM_DISCONNECT;
            closeSocket();
            BroadCastUtil.sendActionBroadCast(App.getInstance(),BroadCastUtil.ACTION_DISCONNECT);
            e.printStackTrace();
        }
    }
}
