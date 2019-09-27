package com.king.chat.socket;

import android.app.Application;
import android.content.IntentFilter;
import android.util.Log;

import com.king.chat.socket.broadcast.ServiceBroadcastReceiver;
import com.king.chat.socket.util.DisplayUtil;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;


/**
 * Created by kongwei on 2017/2/16.
 */

public class App extends Application {

    private static final String TAG = "App";
    public static App instance = null;
    public ServiceBroadcastReceiver receiver;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        regitserConnReceiver();
        DisplayUtil.displayScreen(this);
        FlowManager.init(new FlowConfig.Builder(this).build());
    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        unRegisterReceiver();
        Log.d(TAG, "onTerminate");
        super.onTerminate();
    }
    @Override
    public void onLowMemory() {
        // 低内存的时候执行
        Log.d(TAG, "onLowMemory");
        super.onLowMemory();
    }
    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行
        Log.d(TAG, "onTrimMemory");
        super.onTrimMemory(level);
    }

    /**
     * 注册广播
     */
    private void regitserConnReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        if (receiver == null) {
            receiver = new ServiceBroadcastReceiver();
        }
        registerReceiver(receiver, intentFilter);
    }

    /**
     * 注销广播监听
     */
    private void unRegisterReceiver() {
        if (null != receiver) {
            unregisterReceiver(receiver);
        }
    }
}
