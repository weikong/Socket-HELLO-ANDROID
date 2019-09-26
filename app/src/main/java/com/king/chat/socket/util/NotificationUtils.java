package com.king.chat.socket.util;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.king.chat.socket.App;
import com.king.chat.socket.R;
import com.king.chat.socket.ui.activity.MainActivity;
import com.king.chat.socket.util.badge.BadgeUtil;

import java.util.List;

/**
 * Created by xinzhendi-031 on 2018/4/25.
 */

public class NotificationUtils extends ContextWrapper {

    private Context context;
    private NotificationManager manager;
    public static final String id = "channel_1";
    public static final String name = "channel_name_1";

    private static final String NOTIFY_SETTING = "set_notify";
    private static final int DEFAULT_NOTIFY_FLAG = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
    private long lastTime;//上次响的时候
    private static final long INTERVAL = 2 * 1000;//避免消息接收频繁造成的不好的体验
    /**
     * 未读消息通知
     */
    public static final int NOTIFY_TASK_ID = 2001;

    private static class NotificationUtilsHolder{
        private static final NotificationUtils INSTANCE = new NotificationUtils(App.getInstance());
    }

    /**
     * 单一实例
     */
    public static final NotificationUtils getInstance(){
        return NotificationUtilsHolder.INSTANCE;
    }

    public NotificationUtils(Context context) {
        super(context);
        this.context = context;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createNotificationChannel() {
        NotificationChannel channel = null;
        channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getChannelNotification(String title, String content) {
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), MainActivity.class).setAction(Intent.ACTION_VIEW), PendingIntent.FLAG_CANCEL_CURRENT);
        Notification.Builder builder = new Notification.Builder(getApplicationContext(), id)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        builder.setDefaults(DEFAULT_NOTIFY_FLAG);
        return builder;
    }

    public NotificationCompat.Builder getNotification_25(String title, String content) {
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), MainActivity.class).setAction(Intent.ACTION_VIEW), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), null)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        builder.setDefaults(DEFAULT_NOTIFY_FLAG);
        return builder;
    }

    public void sendNotification(String title, String content) {
//        if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
//            Logger.d("sendNotification", "小米");
//            int count = getUnreadMsg(context);
//            BadgeUtil.getInstance().sendToXiaoMi(context,count,title,content);
//            return;
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            delNotificationChannel();
            createNotificationChannel();
            Notification notification = getChannelNotification
                    (title, content).build();
//            editNotificationContent(notification, title, content,false);
            getManager().notify(NOTIFY_TASK_ID, notification);
        } else {
            Notification notification = getNotification_25(title, content).build();
//            editNotificationContent(notification, title, content,false);
            getManager().notify(NOTIFY_TASK_ID, notification);
        }
//        sendMp3Notification(title,content);
    }

    public void sendNotification(String title, String content,boolean isGooglePush) {
//        if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
//            Logger.d("sendNotification", "小米");
//            int count = getUnreadMsg(context);
//            BadgeUtil.getInstance().sendToXiaoMi(context,count,title,content);
//            return;
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            delNotificationChannel();
            createNotificationChannel();
            Notification notification = getChannelNotification
                    (title, content).build();
//            editNotificationContent(notification, title, content,isGooglePush);
            getManager().notify(NOTIFY_TASK_ID+1, notification);
        } else {
            Notification notification = getNotification_25(title, content).build();
//            editNotificationContent(notification, title, content,isGooglePush);
            getManager().notify(NOTIFY_TASK_ID+1, notification);
        }
//        sendMp3Notification(title,content);
    }

    private void sendMp3Notification(String title, String content){
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), MainActivity.class).setAction(Intent.ACTION_VIEW), PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
//位图文件(Bitmap)，setLargeIcon需要传入 位图文件，方式是BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                //设置跳转
                .setContentIntent(pendingIntent)
                .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.audio_ring))
                .setAutoCancel(true)
                .build();
        getManager().notify(NOTIFY_TASK_ID,notification);
    }

//    public void editNotificationContent(Notification notification, String title, String content,boolean isGooglePush) {
//        int count = getUnreadMsg(context);
//        notification.contentView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
//        notification.contentView.setTextViewText(R.id.tv_title, Html.fromHtml(title));
//        notification.contentView.removeAllViews(R.id.ll_content);
//        if (isGooglePush){
//            notification.contentView.addView(R.id.ll_content, getTextRemoteViews(context, "[" + "Google" + "]"));
//        }
////        notification.contentView.addView(R.id.ll_content, getTextRemoteViews(context, "[" + count + context.getResources().getString(R.string.text_strip) + "]"));
////        List<String> stringList = ChatFaceUtils.subString(content);
//        List<String> stringList = ChatFaceUtils.subNoticeString(content);
//        if (stringList != null) {
//            for (String strrel : stringList) {
//                if (!TextUtils.isEmpty(strrel)) {
//                    if (strrel.startsWith("[!") && strrel.endsWith("]")) {
//                        boolean isFace = false;
//                        List<Expression> expressionList = ExpressionHelper.getFaceFileNameList().getExpList();
//                        if (expressionList != null) {
//                            for (int i = 0; i < expressionList.size(); i++) {
//                                if (strrel.equals(expressionList.get(i).getFlag())) {
//                                    String faceid = expressionList.get(i).getFileName();
//                                    Bitmap bitmap = ImageLoader.getInstance().loadImageSync(new StringBuffer().append("assets://smiley/")//
//                                            .append(faceid)//
//                                            .append(".png")//
//                                            .toString());
//                                    notification.contentView.addView(R.id.ll_content, getImgRemoteViews(context, bitmap));
//                                    isFace = true;
//                                    break;
//                                }
//                            }
//                        }
//                        if (!isFace) {
//                            notification.contentView.addView(R.id.ll_content, getTextRemoteViews(context, strrel));
//                        }
//                    } else if (strrel.startsWith("[") && !strrel.startsWith("[!") && strrel.endsWith("]")) {
//                        if (noticeToWordsConfig == null) {
//                            noticeToWordsConfig = new NoticeToWordsConfig();
//                        }
//                        Bitmap bitmap;
//                        if (noticeToWordsConfig.getNoticeMap().containsKey(strrel)) {
//                            bitmap = noticeToWordsConfig.getNoticeMap().get(strrel);
//                            notification.contentView.addView(R.id.ll_content, getImgRemoteViews(context, bitmap));
//                        } else {
//                            notification.contentView.addView(R.id.ll_content, getTextRemoteViews(context, strrel));
//                        }
//                    } else {
//                        notification.contentView.addView(R.id.ll_content, getTextRemoteViews(context, strrel));
//                    }
//                } else {
//                    notification.contentView.addView(R.id.ll_content, getTextRemoteViews(context, strrel));
//                }
//            }
//        } else {
//            notification.contentView.addView(R.id.ll_content, getTextRemoteViews(context, content));
//        }
//        BadgeUtil.getInstance().setBadgeCount(context,count);
//    }
//
//
//    private RemoteViews getImgRemoteViews(Context context, Bitmap bitmap) {
//        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.remote_views_img);
//        remoteViews.setImageViewBitmap(R.id.imageView, bitmap);
//        return remoteViews;
//    }
//
//    private RemoteViews getTextRemoteViews(Context context, String text) {
//        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.remote_views_text);
//        remoteViews.setTextViewText(R.id.textView, text);
//        return remoteViews;
//    }

    private int getUnreadMsg(Context context) {
        int count = 0;
//        try {
//            count = SessionDBHelper.statisticsAllUnReadCount(context) + 1;
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        return count;
    }

    public Notification getNotification(String title, String content) {
        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            delNotificationChannel();
            createNotificationChannel();
            notification = getChannelNotification
                    (title, content).build();
        } else {
            notification = getNotification_25(title, content).build();
        }
        return notification;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void delNotificationChannel() {
        NotificationChannel mChannel = getManager().getNotificationChannel(id);
        getManager().deleteNotificationChannel(String.valueOf(mChannel));
        getManager().cancelAll();
    }

//    private int getNotifyFlagFromSharedPreferences() {
//        int notifyFlag = SharePreferceTool.getInstance(getApplicationContext()).getInt(NOTIFY_SETTING);
//        if (notifyFlag == 0) {
//            notifyFlag = DEFAULT_NOTIFY_FLAG;
//        }
//        return notifyFlag;
//    }
}
