package com.king.chat.socket.ui.view.toast;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.king.chat.socket.R;
import com.king.chat.socket.util.DisplayUtil;

/**
 * Created by xinzhendi-031 on 2017/11/29.
 */
public class CustomToast {

    public static void showMessageToast(Context context, String message) {
        try {
            //加载Toast布局
            View toastRoot = LayoutInflater.from(context).inflate(R.layout.view_toast_message, null);
            //初始化布局控件
            TextView mTextView = (TextView) toastRoot.findViewById(R.id.tv_message);
            mTextView.setText(message);
            //Toast的初始化
            Toast toastStart = new Toast(context);
            //获取屏幕高度
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        int height = wm.getDefaultDisplay().getHeight();
            int height = DisplayUtil.screenHeight;
            int width = DisplayUtil.screenWidth;
//        toastRoot.setMinimumWidth(width * 2 / 3);
            //Toast的Y坐标是屏幕高度的1/3，不会出现不适配的问题
            toastStart.setGravity(Gravity.TOP, 0, height / 2);
            toastStart.setDuration(Toast.LENGTH_SHORT);
            toastStart.setView(toastRoot);
            toastStart.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showImageToast(Context context, int drawable) {
        try {
            //加载Toast布局
            View toastRoot = LayoutInflater.from(context).inflate(R.layout.view_toast_message, null);
            //初始化布局控件
            ImageView imageView = (ImageView) toastRoot.findViewById(R.id.iv_message);
            if (drawable > 0) {
                imageView.setImageResource(drawable);
            }
            //Toast的初始化
            Toast toastStart = new Toast(context);
            //获取屏幕高度
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        int height = wm.getDefaultDisplay().getHeight();
            int height = DisplayUtil.screenHeight;
            int width = DisplayUtil.screenWidth;
//        toastRoot.setMinimumWidth(width * 2 / 3);
            //Toast的Y坐标是屏幕高度的1/3，不会出现不适配的问题
            toastStart.setGravity(Gravity.TOP, 0, height / 2);
            toastStart.setDuration(Toast.LENGTH_SHORT);
            toastStart.setView(toastRoot);
            toastStart.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
