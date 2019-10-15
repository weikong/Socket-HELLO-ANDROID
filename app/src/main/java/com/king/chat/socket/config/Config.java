package com.king.chat.socket.config;

import java.io.Serializable;

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
//    public static final String HOST = "https://deepkeep.top/";


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
}
