/*
 * Copyright 2014-2024 setNone. All rights reserved. 
 */
package com.king.chat.socket.util;

import android.util.Log;

/**
 * Logger.java - Log工具类
 *
 * @author jiengyh
 *         <p/>
 *         2014-2-25 下午1:17:01
 */
public class Logger {

    private static int LOG_LEVEL = 6;

    private static int INFO = 3;
    private static int ERROR = 1;
    private static int WARN = 2;
    private static int DEBUG = 4;
    private static int VERBOS = 5;

    private static String TAG = "LogerHelper";

    /**
     * 不打印tag,自动把TAG合msg打印
     */
    public static boolean isPrintTagInInfo = false;

    public static void i(String tag, String msg) {
        if (LOG_LEVEL > INFO) {
            if (isPrintTagInInfo) {
                msg = "tag:" + tag + ", " + msg;
            }
            Log.i(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (LOG_LEVEL > ERROR) {
            if (isPrintTagInInfo) {
                msg = "tag:" + tag + ", " + msg;
            }
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Exception e) {
        if (LOG_LEVEL > ERROR) {
            if (isPrintTagInInfo) {
                msg = "tag:" + tag + ", " + msg;
            }
            Log.e(tag, msg, e);
        }
    }

    public static void v(String tag, String msg) {
        if (LOG_LEVEL > VERBOS) {
            if (isPrintTagInInfo) {
                msg = "tag:" + tag + ", " + msg;
            }
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (LOG_LEVEL > DEBUG) {
            if (isPrintTagInInfo) {
                msg = "tag:" + tag + ", " + msg;
            }
            Log.d(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (LOG_LEVEL > WARN) {
            if (isPrintTagInInfo) {
                msg = "tag:" + tag + ", " + msg;
            }
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, String msg, Exception e) {
        if (LOG_LEVEL > WARN) {
            if (isPrintTagInInfo) {
                msg = "tag:" + tag + ", " + msg;
            }
            Log.w(tag, msg, e);
        }
    }

    public static void i(String msg) {
        i(TAG, msg);
    }

    public static void e(String msg) {
        e(TAG, msg);
    }

    public static void v(String msg) {
        v(TAG, msg);
    }

    public static void d(String msg) {
        d(TAG, msg);
    }

    public static void w(String msg) {
        w(TAG, msg);
    }

}
