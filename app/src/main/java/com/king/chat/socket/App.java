package com.king.chat.socket;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;

import com.king.chat.socket.broadcast.ServiceBroadcastReceiver;
import com.king.chat.socket.exception.CrashHandler;
import com.king.chat.socket.util.DisplayUtil;
import com.king.chat.socket.util.ExpressionHelper;
import com.king.chat.socket.util.ImageLoaderOptions;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;


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
        CrashHandler.getInstance().init(this);
        regitserConnReceiver();
        DisplayUtil.displayScreen(this);
        initImageLoader(this);
//        FlowManager.init(new FlowConfig.Builder(this).build());
        new Thread(new Runnable() {
            @Override
            public void run() {
                ExpressionHelper.getInstance().buildFaceFileNameList();
            }
        }).start();
    }

    public static void initImageLoader(Context context) {
        int maxMemory = 0;
        int maxImageMemoryCacheSize = (maxMemory == 0) ? ImageLoaderOptions.MAX_IMAGE_DISK_CACHE_SIZE : (maxMemory / 8);
//		File cacheDir = StorageUtils.getOwnCacheDirectory(appContext, "Melinked/imageloader/Cache");
// 				.diskCache(new UnlimitedDiskCache(cacheDir)) //自定义缓存路径
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.icon_default_user) // 设置图片下载期间显示的图片
                .showImageOnLoading(R.drawable.icon_default_user)    //设置下载过程中图片显示
                .showImageForEmptyUri(R.drawable.icon_default_user) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.icon_default_user) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                .build(); // 创建配置过得DisplayImageOption对象
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(options)
                .memoryCache(new LruMemoryCache(maxImageMemoryCacheSize))
                .memoryCacheExtraOptions(ImageLoaderOptions.MAX_IMAGE_WIDTH, ImageLoaderOptions.MAX_IMAGE_HEIGHT)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
//				.diskCacheSize(ImageLoaderOptions.MAX_IMAGE_DISK_CACHE_SIZE)//缓存的文件占sdcard大小
//				.diskCacheFileCount(ImageLoaderOptions.MAX_IMAGE_DISK_FILE_COUNT)//缓存的文件数量
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO) //LIFO:后进先出 --  FIFO:先入先出
                .build();

        ImageLoader.getInstance().init(config);
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
