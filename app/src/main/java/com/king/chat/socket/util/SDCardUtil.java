package com.king.chat.socket.util;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by xinzhendi-031 on 2016/11/22.
 */
public class SDCardUtil {

    public static final String localAppDir = "HELLO";
    public static final String localAppImgDir = "IMG";
    public static final String localAppLogDir = "LOG";
    public static final String localAppGifDir = "GIF";
    public static final String localAppVoiceDir = "VOICE";
    public static final String localAppVideoDir = "VIDEO";

    /**
     * 判断SDCard是否可用
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡路径
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator;
    }

    public static String createRootDir() {
        File dir = new File(getSDCardPath() + localAppDir);
        if (!dir.exists())
            dir.mkdirs();
        return dir != null ? dir.getAbsolutePath() : getSDCardPath();
    }

    public static String getImgDir() {
        File dir = new File(createRootDir() + File.separator + localAppImgDir);
        if (!dir.exists())
            dir.mkdirs();
        return dir != null ? dir.getAbsolutePath() : getSDCardPath();
    }

    public static String getGifDir() {
        File dir = new File(createRootDir() + File.separator + localAppGifDir);
        if (!dir.exists())
            dir.mkdirs();
        return dir != null ? dir.getAbsolutePath() : getSDCardPath();
    }

    public static String getVoiceDir() {
        File dir = new File(createRootDir() + File.separator + localAppVoiceDir);
        if (!dir.exists())
            dir.mkdirs();
        return dir != null ? dir.getAbsolutePath() : getSDCardPath();
    }

    public static String getVideoDir() {
        File dir = new File(createRootDir() + File.separator + localAppVideoDir);
        if (!dir.exists())
            dir.mkdirs();
        return dir != null ? dir.getAbsolutePath() : getSDCardPath();
    }

    public static String getLogDir() {
        File dir = new File(createRootDir() + File.separator + localAppLogDir);
        if (!dir.exists())
            dir.mkdirs();
        return dir != null ? dir.getAbsolutePath() : getSDCardPath();
    }

    public static File createTmpFile(String fileName) {
        File dir = new File(getSDCardPath() + localAppDir);
        if (!dir.exists())
            dir.mkdirs();
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(dir, fileName);
        if (f != null && f.exists())
            f.delete();
        return f;
    }

    /**
     * 获取SD卡的剩余容量 单位byte
     */
    public static long getSDCardAllSize() {
        if (isSDCardEnable()) {
            StatFs stat = new StatFs(getSDCardPath());
            long availableBlocks = (long) stat.getAvailableBlocks() - 4;
            long freeBlocks = stat.getAvailableBlocks();
            return freeBlocks * availableBlocks;
        }
        return 0;
    }

    /**
     * 获取指定路径所在空间的剩余可用容量字节数，单位byte
     */
    public static long getFreeBytes(String filePath) {
        // 如果是sd卡的下的路径，则获取sd卡可用容量
        if (filePath.startsWith(getSDCardPath())) {
            filePath = getSDCardPath();
        } else {// 如果是内部存储的路径，则获取内存存储的可用容量
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs stat = new StatFs(filePath);
        long availableBlocks = (long) stat.getAvailableBlocks() - 4;
        return stat.getBlockSize() * availableBlocks;
    }

    /**
     * 获取系统存储路径
     */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }


    // 构建私有路径
    public static String buildConfigDir(Context context, String pathName) {
        // Logger.i("--->UN 不存在SD卡,将返回data路径!");
        return buildPrivateDataDir(context, pathName);
    }

    // 构建私有路径
    public static String buildPrivateDataDir(Context context, String pathName) {
        // Logger.i("--->UN 不存在SD卡,将返回data路径!");
        return context.getDir(pathName, Context.MODE_PRIVATE).toString() + File.separator;
    }

    public static String buildDataDir() {
        return Environment.getDataDirectory().toString();
    }

}
