package com.king.chat.socket.ui.DBFlow.chatRecord;

import java.io.Serializable;

/**
 * Created by maesinfo on 2019/5/15.
 */

public class MessageChatType {

    /**
     * 消息类型
     * 1、文本；2、语音；3；视频；4、文件；5、链接；6、分享；7、红包；8、图片
     */
    public static final int TYPE_TEXT = 1;
    public static final int TYPE_VOICE = 2;
    public static final int TYPE_VIDEO = 3;
    public static final int TYPE_FILE = 4;
    public static final int TYPE_LINK = 5;
    public static final int TYPE_SHARE = 6;
    public static final int TYPE_RED_PACKET = 7;
    public static final int TYPE_IMG = 8;

}
