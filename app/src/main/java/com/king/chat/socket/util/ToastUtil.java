package com.king.chat.socket.util;


import android.widget.Toast;

import com.king.chat.socket.App;

public class ToastUtil {

    public static void show(int resId) {
        show(App.getInstance().getResources().getText(resId), Toast.LENGTH_SHORT);
    }

    public static void show(int resId, int duration) {
        show(App.getInstance().getResources().getText(resId), duration);
    }


    public static void show(CharSequence text) {
        show(text, Toast.LENGTH_SHORT);
    }

    public static void show(CharSequence text, int duration) {
        Toast.makeText(App.getInstance(), text, duration).show();
    }

    public static void show(int resId, Object... args) {
        show(String.format(App.getInstance().getResources().getString(resId), args), Toast.LENGTH_SHORT);
    }

    public static void show(String format, Object... args) {
        show(String.format(format, args), Toast.LENGTH_SHORT);
    }

    public static void show(int resId, int duration, Object... args) {
        show(String.format(App.getInstance().getResources().getString(resId), args), duration);
    }

    public static void show(String format, int duration, Object... args) {
        show(String.format(format, args), duration);
    }
}
