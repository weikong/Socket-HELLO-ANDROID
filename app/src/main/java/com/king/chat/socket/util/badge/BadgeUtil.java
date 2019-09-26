package com.king.chat.socket.util.badge;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;

import com.king.chat.socket.ui.activity.MainActivity;
import com.king.chat.socket.util.Logger;
import com.king.chat.socket.util.NotificationUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

//BadgeUtil provides static utility methods to set "badge count" on Launcher (by Samsung, LG).   
//Currently, it's working from Android 4.0.   
//But some devices, which are released from the manufacturers, are not working.  

public class BadgeUtil {

    private static final String TAG = "BadgeUtil";

    private static class BadgeUtilHolder{
        private static final BadgeUtil INSTANCE = new BadgeUtil();
    }

    private BadgeUtil(){
        Logger.e("BadgeUtil Instance");
    }

    public static final BadgeUtil getInstance(){
        return BadgeUtilHolder.INSTANCE;
    }

    /**
     * Set badge count<br/>
     * 针对 Samsung / xiaomi / sony 手机有效
     *
     * @param context The context of the application package.
     * @param count   Badge count to be set
     */
    public void setBadgeCount(Context context, int count) {
        if (count <= 0) {
            count = 0;
        } else {
            count = Math.max(0, Math.min(count, 99));
        }

        if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
//            sendToXiaoMi(context, count);
            Logger.d(TAG, "小米");
        } else if (Build.MANUFACTURER.equalsIgnoreCase("sony")) {
            sendToSony(context, count);
            Logger.d(TAG, "不支持");
            Logger.d(TAG, "索尼");
        } else if (Build.MANUFACTURER.toLowerCase().contains("samsung")) {
            sendToSamsumg(context, count);
            Logger.d(TAG, "三星");
        } else {
//            createShortCut(context, R.drawable.ic_launcher, R.string.app_name, count);
            Logger.d(TAG, "其它");
        }
    }

    /**
     * Set badge count<br/>
     * 针对 Samsung / xiaomi / sony 手机有效
     *
     * @param context The context of the application package.
     * @param count   Badge count to be set
     */
    public void setBadgeCount(Context context, int count,Notification notification) {
        if (count <= 0) {
            count = 0;
        } else {
            count = Math.max(0, Math.min(count, 99));
        }

        if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
            sendToXiaoMi(context, count,notification);
            Logger.d(TAG, "小米");
        } else if (Build.MANUFACTURER.equalsIgnoreCase("sony")) {
            sendToSony(context, count);
            Logger.d(TAG, "不支持");
            Logger.d(TAG, "索尼");
        } else if (Build.MANUFACTURER.toLowerCase().contains("samsung")) {
            sendToSamsumg(context, count);
            Logger.d(TAG, "三星");
        } else {
//            createShortCut(context, R.drawable.ic_launcher, R.string.app_name, count);
            Logger.d(TAG, "其它");
        }
    }

    /**
     * 向小米手机发送未读消息数广播
     *
     * @param count
     */
    public void sendToXiaoMi(Context context, int count,String title,String content) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationUtils utils = null;
        Notification notification = null;
        boolean isMiUIV6 = true;
        try {
//            Class miuiNotificationClass = Class.forName("android.app.MiuiNotification");
//            Object miuiNotification = miuiNotificationClass.newInstance();
//            Field field = miuiNotification.getClass().getDeclaredField("messageCount");
//            field.setAccessible(true);
//            field.set(miuiNotification, String.valueOf(count == 0 ? "" : count));  // 设置信息数-->这种发送必须是miui 6才行
            utils = new NotificationUtils(context);
            notification = utils.getNotification(title,content);
//            Field field = notification.getClass().getDeclaredField("extraNotification");
//            Object extraNotification = field.get(notification);
//            Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int.class);
//            method.invoke(extraNotification, count);

            Class miuiNotificationClass = Class.forName("android.app.MiuiNotification");
            Object miuiNotification = miuiNotificationClass.newInstance();
            Field field = miuiNotification.getClass().getDeclaredField("messageCount");
            field.setAccessible(true);
            field.set(miuiNotification, count);// 设置信息数
            field = notification.getClass().getField("extraNotification");
            field.setAccessible(true);
            field.set(notification, miuiNotification);
        } catch (Exception e) {
            e.printStackTrace();
            // miui 6之前的版本
            isMiUIV6 = false;
            Intent localIntent = new Intent(
                    "android.intent.action.APPLICATION_MESSAGE_UPDATE");
            localIntent.putExtra(
                    "android.intent.extra.update_application_component_name",
                    context.getPackageName() + "/" + getLauncherClassName(context));
            localIntent.putExtra(
                    "android.intent.extra.update_application_message_text", String.valueOf(count == 0 ? "" : count));
            context.sendBroadcast(localIntent);
        } finally {
            if(notification!=null && isMiUIV6 )
            {
                //miui6以上版本需要使用通知发送
//                nm.notify(101010, notification);
                if (utils != null && notification != null){
//                    utils.editNotificationContent(notification, title, content,false);
                    nm.notify(NotificationUtils.NOTIFY_TASK_ID, notification);
                }
            }
        }
    }

    /**
     * 向小米手机发送未读消息数广播
     *
     * @param count
     */
    private void sendToXiaoMi(Context context, int count,Notification notification) {
        try {
            Field field = notification.getClass().getDeclaredField("extraNotification");
            Object extraNotification = field.get(notification);
            Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int.class);
            method.invoke(extraNotification, count);
        } catch (Exception e) {
            e.printStackTrace();
            // miui 6之前的版本
            Intent localIntent = new Intent(
                    "android.intent.action.APPLICATION_MESSAGE_UPDATE");
            localIntent.putExtra(
                    "android.intent.extra.update_application_component_name",
                    context.getPackageName() + "/" + getLauncherClassName(context));
            localIntent.putExtra(
                    "android.intent.extra.update_application_message_text", String.valueOf(count == 0 ? "" : count));
            context.sendBroadcast(localIntent);
        }
    }

    /**
     * 向索尼手机发送未读消息数广播<br/>
     * 据说：需添加权限：<uses-permission android:name="com.sonyericsson.home.permission.BROADCAST_BADGE" /> [未验证]
     *
     * @param count
     */
    private void sendToSony(Context context, int count) {
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }

        boolean isShow = true;
        if (count == 0) {
            isShow = false;
        }
        Intent localIntent = new Intent();
        localIntent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", isShow);//是否显示
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME", launcherClassName);//启动页
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", String.valueOf(count));//数字
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", context.getPackageName());//包名
        context.sendBroadcast(localIntent);
    }


    /**
     * 向三星手机发送未读消息数广播
     *
     * @param count
     */
    private void sendToSamsumg(Context context, int count) {
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        context.sendBroadcast(intent);
    }

    //快捷方式
    public static void createShortCut(Context act, int iconResId,
                                      int appNameResId, int count) {
        AppShortCutUtil.deleteRawShortCut(act,
                MainActivity.class, appNameResId);
        AppShortCutUtil.addNumShortCut(act,
                MainActivity.class, iconResId, appNameResId, true,
                count + "", false);
    }

    /**
     * 重置、清除Badge未读显示数<br/>
     *
     * @param context
     */
    public void resetBadgeCount(Notification notification,Context context) {
        if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
            setBadgeCount(context, 0,notification);
            Logger.d(TAG, "小米");
        } else if (Build.MANUFACTURER.equalsIgnoreCase("sony")) {
            setBadgeCount(context, 0);
            Logger.d(TAG, "索尼");
        } else if (Build.MANUFACTURER.toLowerCase().contains("samsung")) {
            setBadgeCount(context, 0);
            Logger.d(TAG, "三星");
        } else {
//            AppShortCutUtil.deleteRawShortCut(context,
//                    WelActivity.class, R.string.app_name);
//            AppShortCutUtil.addNumShortCut(context,
//                    WelActivity.class, R.drawable.ic_launcher, R.string.app_name, true,
//                    "0", false);
            Logger.d(TAG, "其它");
        }
    }


    /**
     * Retrieve launcher activity name of the application from the context
     *
     * @param context The context of the application package.
     * @return launcher activity name of this application. From the
     * "android:name" attribute.
     */
    private String getLauncherClassName(Context context) {
        PackageManager packageManager = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        // To limit the components this Intent will resolve to, by setting an
        // explicit package name.
        intent.setPackage(context.getPackageName());
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        // All Application must have 1 Activity at least.
        // Launcher activity must be found!
        ResolveInfo info = packageManager
                .resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

        // get a ResolveInfo containing ACTION_MAIN, CATEGORY_LAUNCHER
        // if there is no Activity which has filtered by CATEGORY_DEFAULT
        if (info == null) {
            info = packageManager.resolveActivity(intent, 0);
        }

        return info.activityInfo.name;
    }
}  