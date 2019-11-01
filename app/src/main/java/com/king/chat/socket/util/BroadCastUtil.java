package com.king.chat.socket.util;

import android.content.Context;
import android.content.Intent;

import java.io.Serializable;

/**
 * Created by maesinfo on 2019/5/30.
 */

public class BroadCastUtil {

    public static final String ACTION_USER_LOGIN = "action_user_login";
    public static final String ACTION_USER_LOGOUT = "action_user_logout";
    public static final String ACTION_USER_REGISTER = "action_user_register";
    public static final String ACTION_RECIEVE_MESSAGE = "action_recieve_message";
    public static final String ACTION_UPDATE_MESSAGE = "action_update_message";
    public static final String ACTION_UPDATE_SESSION = "action_update_session";
    public static final String ACTION_CONNECTED = "action_connected";
    public static final String ACTION_CONNECTING = "action_connecting";
    public static final String ACTION_DISCONNECT = "action_disconnect";

    public static final String ACTION_CLEAR_CHAT_MESSAGE = "action_clear_chat_message";
    public static final String ACTION_GROUP_UPDATE = "action_group_update";

    public static final String ACTION_GIF_UPDATE = "action_gif_update";

    /**
     * 发送广播
     *
     * @param context
     * @param action
     * @param data
     */
    public static void sendActionBroadCast(Context context, String action, Object data) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra("DATA", (Serializable) data);
        context.sendBroadcast(intent);
    }

    /**
     * 发送广播
     *
     * @param context
     * @param action
     * @param data
     */
    public static void sendActionBroadCast(Context context, String action, String data) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra("DATA", data);
        context.sendBroadcast(intent);
    }

    /**
     * 发送广播
     *
     * @param context
     * @param action
     */
    public static void sendActionBroadCast(Context context, String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        context.sendBroadcast(intent);
    }
}
