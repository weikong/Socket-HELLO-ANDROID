package com.king.chat.socket.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.king.chat.socket.util.BroadCastUtil;
import com.king.chat.socket.util.Logger;
import com.king.chat.socket.util.NetWorkUtil;


public class ServiceBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "ServiceBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.i(TAG, "收到广播：" + intent.getAction());
        if(NetWorkUtil.haveNetWorkOK(context)) {
            BroadCastUtil.sendActionBroadCast(context,BroadCastUtil.ACTION_CONNECTED);
        } else {
            BroadCastUtil.sendActionBroadCast(context,BroadCastUtil.ACTION_DISCONNECT);
        }
    }
}
