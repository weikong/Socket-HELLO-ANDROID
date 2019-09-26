package com.king.chat.socket.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.widget.Toast;

/**
 * Created by jian.cao on 2016/3/10.
 */
public class NetWorkUtil {
    public static boolean haveNetWork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo tmpInfo = connectivityManager.getActiveNetworkInfo();
        boolean haveNetWork = tmpInfo != null && tmpInfo.isAvailable();
        return haveNetWork;
    }

    public static boolean haveNetWorkOK(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo tmpInfo = connectivityManager.getActiveNetworkInfo();
        boolean haveNetWork = tmpInfo != null && tmpInfo.isConnected();
        return haveNetWork;
    }

    /**
     * 当连接后回调
     * */
    private void networkChange(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManager.requestNetwork(new NetworkRequest.Builder().build(),
                    new ConnectivityManager.NetworkCallback() {
                        @Override public void onAvailable(Network network) {
                            super.onAvailable(network);
                            Logger.e("ServiceBroadcastReceiver","网络状态改变了");
                        }
                    });
        }
    }
}
