package com.king.chat.socket.util;

import android.app.Activity;

import java.util.Stack;

/**
 * Activity栈管理
 *
 * @author luomin
 */
public class AppManager {
    private static Stack<Activity> mActivityStack;
    private static AppManager mAppManager;

    private static class AppManagerHolder{
        private static final AppManager INSTANCE = new AppManager();
    }

    /**
     * 单一实例
     */
    public static final AppManager getInstance(){
        return AppManagerHolder.INSTANCE;
    }

//    /**
//     * 单一实例
//     */
//    public static AppManager getInstance() {
//        if (mAppManager == null) {
//            mAppManager = new AppManager();
//        }
//        return mAppManager;
//    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<Activity>();
        }
        mActivityStack.add(activity);
    }

    /**
     * 获取栈顶Activity（堆栈中最后一个压入的）
     */
    public Activity getTopActivity() {
        Activity activity = mActivityStack.lastElement();
        return activity;
    }

    /**
     * 结束栈顶Activity（堆栈中最后一个压入的）
     */
    public synchronized void killTopActivity() {
        Activity activity = mActivityStack.lastElement();
        killActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public synchronized void killActivity(Activity activity) {
        if (activity != null) {
            mActivityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 查找指定activity
     *
     * @param activity
     */
    public String SearchActivity(Activity activity) {
        String activity2 = "";
        if (mActivityStack.contains(activity)) {
            activity2 = "0";
            return activity2;
        }
        return activity2;


    }

    /**
     * 结束指定类名的Activity
     */
    public synchronized void killActivity(Class<?> cls) {
        for (Activity activity : mActivityStack) {
            if (activity.getClass().equals(cls)) {
                killActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public synchronized void killAllActivity() {
        for (int i = 0, size = mActivityStack.size(); i < size; i++) {
            if (null != mActivityStack.get(i)) {
                mActivityStack.get(i).finish();
            }
        }
        mActivityStack.clear();
    }

    /**
     * 结束所有Activity
     */
    public synchronized void killAllActivityExcept(Class<?> cls) {
        for (int i = 0, size = mActivityStack.size(); i < size; i++) {
            Activity activity = mActivityStack.get(i);
            if (null != activity && !activity.getClass().equals(cls)) {
                mActivityStack.get(i).finish();
            }
        }
        mActivityStack.clear();
    }

    public void weakExit() {
        killAllActivity();
    }
}
