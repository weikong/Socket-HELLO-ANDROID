package com.king.chat.socket.config;

/**
 * Created by maesinfo on 2019/9/18.
 */

public class Config {

    /**
     * 公司 IP
     */
    public static final String HOST = "172.17.7.164";
    /**
     * MAC IP
     */
//    public static final String HOST = "172.17.7.134";
    /**
     * 华为云
     */
//    public static final String HOST = "deepkeep.top";
//    public static final String HOST = "114.116.124.23";


    public static final int PORT = 9999;
    /**
     * socket连接超时
     */
    public static final int SOCKET_CONNECT_TIMEOUT = 30 * 1000;
    /**
     * socket读取数据超时
     */
    public static final int SOCKET_SO_TIMEOUT = 15 * 1000;


    public static String SystemID = "-1";

    public static String userId = "";
    public static String userName = "";

    public static String toUserId = "";
    public static String toUserName = "";


    public static String LOGIN_USER_NAME = "LOGIN_USER_NAME";
    public static String LOGIN_USER_PSD = "LOGIN_USER_PSD";

    public static int PageSize = 20;

    public static int MaxMembers = 100;


    public static String strJson = "{\n" +
            "\"code\": 10001,\n" +
            "\"messageId\": \"5140721d-0141-4d41-b2a5-e8792f15476f\",\n" +
            "\"header\": {\n" +
            "\"isAck\": 1,\n" +
            "\"translator\": \"2\",\n" +
            "\"servertimestamp\": 1572427313098,\n" +
            "\"mimeType\": \"multimedia\"\n" +
            "},\n" +
            "\"from\": \"G3450214845283328@muc.melinked.com\",\n" +
            "\"to\": \"33740971587164161@melinked.com/web_chrome_78.0.3904.70\",\n" +
            "\"type\": \"groupchat\",\n" +
            "\"protocal\": \"{\\\"actor\\\":\\\"13022222222\\\",\\\"fileName\\\":\\\"1572427558298.m4a\\\",\\\"fileSize\\\":2530,\\\"messageId\\\":\\\"5140721d-0141-4d41-b2a5-e8792f15476f\\\",\\\"multimediaLength\\\":1,\\\"multimediaTranslateMessage\\\":\\\"[{\\\\\\\"language\\\\\\\":\\\\\\\"1\\\\\\\",\\\\\\\"translateMsg\\\\\\\":\\\\\\\"语音翻译测试文本。\\\\\\\",\\\\\\\"translateTime\\\\\\\":\\\\\\\"1572427312651\\\\\\\",\\\\\\\"translator_id\\\\\\\":\\\\\\\"2\\\\\\\"},{\\\\\\\"language\\\\\\\":\\\\\\\"2\\\\\\\",\\\\\\\"translateMsg\\\\\\\":\\\\\\\"Voice translation test text.\\\\\\\\n\\\\\\\",\\\\\\\"translateTime\\\\\\\":\\\\\\\"1572427312859\\\\\\\",\\\\\\\"translator_id\\\\\\\":\\\\\\\"2\\\\\\\"},{\\\\\\\"language\\\\\\\":\\\\\\\"8\\\\\\\",\\\\\\\"translateMsg\\\\\\\":\\\\\\\"音声翻訳テストテキスト。\\\\\\\\n\\\\\\\",\\\\\\\"translateTime\\\\\\\":\\\\\\\"1572427313037\\\\\\\",\\\\\\\"translator_id\\\\\\\":\\\\\\\"2\\\\\\\"}]\\\",\\\"originalSenderId\\\":\\\"34502006640793601\\\",\\\"subName\\\":\\\"34502006640793601\\\",\\\"subSendTime\\\":1572427313038,\\\"subSenderId\\\":\\\"34502006640793601\\\",\\\"subType\\\":\\\"multimedia\\\",\\\"url\\\":\\\"http://tfile.melinked.com/2019/10/5f62dfa3-414d-4a17-a4b0-432eebcc7d19.m4a\\\",\\\"version\\\":\\\"and_3.0.3\\\"}\",\n" +
            "\"errors\": {},\n" +
            "\"sourceSender\": \"34502006640793601\"\n" +
            "}";
}
