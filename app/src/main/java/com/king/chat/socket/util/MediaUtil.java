package com.king.chat.socket.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;


import com.king.chat.socket.bean.FileItem;
import com.king.chat.socket.bean.MediaInfoBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinzhendi-031 on 2017/12/13.
 */
public class MediaUtil {

    public static MediaInfoBean getPlayTime(String mUri) {
        MediaInfoBean mediaInfoBean = null;
        long time1 = System.currentTimeMillis();
        android.media.MediaMetadataRetriever mmr = new android.media.MediaMetadataRetriever();
        try {
//            if (mUri != null) {
//                FileInputStream inputStream = new FileInputStream(new File(mUri).getAbsolutePath());
//                mmr.setDataSource(inputStream.getFD());
//            } else {
//                //mmr.setDataSource(mFD, mOffset, mLength);
//            }
            mmr.setDataSource(new File(mUri).getAbsolutePath());
            String duration = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);//时长(毫秒)
            String width = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);//宽
            String height = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);//高
            mediaInfoBean = new MediaInfoBean();
            mediaInfoBean.setDuration(duration);
            mediaInfoBean.setWidth(width);
            mediaInfoBean.setHeight(height);
        } catch (Exception ex) {
            Log.e("MediaUtil", "MediaMetadataRetriever exception " + ex);
        } finally {
            mmr.release();
            long time2 = System.currentTimeMillis();
            Log.e("MediaUtil", "time2 - time1 = " + (time2 - time1));
        }
        return mediaInfoBean;
    }

    public static List<FileItem> getAllImageVideo(Context context) {
        List<FileItem> files = new ArrayList<>();
        try {
            Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            // 只查询JPEG和PNG扩展名的图片，并按照最近修改时间排序
            StringBuffer selection = new StringBuffer();
            selection.append(MediaStore.Images.Media.MIME_TYPE).append("= ? or ")
                    .append(MediaStore.Images.Media.MIME_TYPE).append("= ? or ")
                    .append(MediaStore.Images.Media.MIME_TYPE).append("= ? or ")
                    .append(MediaStore.Images.Media.MIME_TYPE).append("= ? or ")
                    .append(MediaStore.Images.Media.MIME_TYPE).append("= ? or ")
                    .append(MediaStore.Images.Media.MIME_TYPE).append("= ? or ")
                    .append(MediaStore.Images.Media.MIME_TYPE).append("= ? or ")
                    .append(MediaStore.Images.Media.MIME_TYPE).append("= ? or ")
                    .append(MediaStore.Images.Media.MIME_TYPE).append("= ? or ")
                    .append(MediaStore.Images.Media.MIME_TYPE).append("= ?");
            String[] args = new String[]{"image/jpeg", "image/png", "image/jpg",
                    "video/3gpp", "video/mpeg", "video/mp4","video/avi","video/quicktime","video/ogm","video/x-flv"};
            Cursor cursor = context.getContentResolver().query(MediaStore.Files.getContentUri("external"), null, selection.toString(), args, MediaStore.Images.Media.DATE_MODIFIED + " desc");
            List<String> photoUrlList = new ArrayList<String>();
            while (cursor.moveToNext()) {
                String fileId = "";
                String fileName = "";
                String filePath = "";
                String fileType = "";
                String fileCover = "";
                int fileDuration = 0;
                long fileSize = 0L;
                long fileDate;
                int width;
                int height;
                // 获取图片的路径
                filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
                if (!TextUtils.isEmpty(filePath)){
                    try {
                        boolean jump = false;
                        String[] folders = filePath.split(File.separator);
                        if (folders != null && folders.length > 0){
                            for (String folder :folders){
                                if (!TextUtils.isEmpty(folder) && folder.startsWith(".")){
                                    jump = true;
                                    break;
                                }
                            }
                            if (jump){
                                continue;
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                fileId = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID));
                fileName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE));
                fileType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE));
                fileSize = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE));
                fileDate = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED));
//            Logger.e("getAllImageVideoVoice filePath = "+filePath);
                if (filePath.contains("image")){
//                imageId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID));
//                fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME));
//                filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
//                String fileType = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE));
                    FileItem fileItem = new FileItem(fileId, filePath, fileName);
                    fileItem.setFileType(fileType);
                    files.add(fileItem);
                } else {
                    if (fileSize == 0)
                        continue;
                    if (fileType.contains("video/")) {
//                fileCover = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
//                        fileCover = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA));
                        fileCover = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM));
                        fileDuration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                        width = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.WIDTH));
                        height = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT));
                    } else {
//                歌曲的专辑名：MediaStore.Audio.Media.ALBUM
                        String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                        fileCover = album;
//                歌曲的歌手名：MediaStore.Audio.Media.ARTIST
                        String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
//                歌曲文件的路径：MediaStore.Audio.Media.DATA
                        String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
//                歌曲的总播放时长：MediaStore.Audio.Media.DURATION
                        fileDuration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
//                歌曲文件的大小：MediaStore.Audio.Media.SIZE
                    }
                    Log.e("AllVideoVoice", fileType + " -- " + fileCover + "--" + fileDuration + " -- " + fileSize + " -- " + fileName);
                    FileItem fileItem = new FileItem();
                    fileItem.setImageId(fileId);
                    fileItem.setFileName(fileName);
                    fileItem.setFilePath(filePath);
                    fileItem.setFileType(fileType);
                    fileItem.setFileCover(fileCover);
                    fileItem.setFileDuration(fileDuration);
                    fileItem.setFileSize(fileSize);
                    fileItem.setIsAlbumMedia(1);//0:不是相册中的多媒体文件；1：相册中的多媒体文件
                    fileItem.setFileDate(fileDate);
                    files.add(fileItem);
                }
            }
            cursor.close();
            cursor = null;
        } catch (Exception e){
            e.printStackTrace();
        }
        return files;
    }

    public static List<FileItem> getAllImageVideoVoice(Context context) {
        List<FileItem> files = new ArrayList<>();
        try {
            Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            // 只查询JPEG和PNG扩展名的图片，并按照最近修改时间排序
            StringBuffer selection = new StringBuffer();
            selection.append(MediaStore.Images.Media.MIME_TYPE).append("= ? or ").append(MediaStore.Images.Media.MIME_TYPE).append("= ? or ")
                    .append(MediaStore.Images.Media.MIME_TYPE).append("= ? or ")
                    .append(MediaStore.Images.Media.MIME_TYPE).append("= ? or ")
                    .append(MediaStore.Images.Media.MIME_TYPE).append("= ? or ")
                    .append(MediaStore.Images.Media.MIME_TYPE).append("= ? or ")
                    .append(MediaStore.Images.Media.MIME_TYPE).append("= ? or ")
                    .append(MediaStore.Images.Media.MIME_TYPE).append("= ? or ")
                    .append(MediaStore.Images.Media.MIME_TYPE).append("= ? or ")
                    .append(MediaStore.Images.Media.MIME_TYPE).append("= ? or ")
                    .append(MediaStore.Images.Media.MIME_TYPE).append("= ? or ")
                    .append(MediaStore.Images.Media.MIME_TYPE).append("= ? or ")
                    .append(MediaStore.Images.Media.MIME_TYPE).append("= ? or ")
                    .append(MediaStore.Images.Media.MIME_TYPE).append("= ? or ")
                    .append(MediaStore.Images.Media.MIME_TYPE).append("= ? or ")
                    .append(MediaStore.Images.Media.MIME_TYPE).append("= ? or ")
                    .append(MediaStore.Images.Media.MIME_TYPE).append("= ? or ")
                    .append(MediaStore.Images.Media.MIME_TYPE).append("= ? or ")
                    .append(MediaStore.Images.Media.MIME_TYPE).append("= ? or ")
                    .append(MediaStore.Images.Media.MIME_TYPE).append("= ? or ")
                    .append(MediaStore.Images.Media.MIME_TYPE).append("= ?");
            String[] args = new String[]{"image/jpeg", "image/png", "audio/3gpp", "audio/ac3","audio/wav", "audio/mpeg","audio/quicktime","audio/mp4", "audio/aac","audio/ogg", "audio/amr","audio/amr-wb","audio/quicktime",
                    "video/3gpp", "video/mpeg", "video/mp4","video/avi","video/quicktime","video/ogm","video/x-flv", "image/jpg"};
            Cursor cursor = context.getContentResolver().query(MediaStore.Files.getContentUri("external"), null, selection.toString(), args, MediaStore.Images.Media.DATE_MODIFIED + " desc");
            List<String> photoUrlList = new ArrayList<String>();
            while (cursor.moveToNext()) {
                String fileId = "";
                String fileName = "";
                String filePath = "";
                String fileType = "";
                String fileCover = "";
                int fileDuration = 0;
                long fileSize = 0L;
                // 获取图片的路径
                filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
                if (!TextUtils.isEmpty(filePath)){
                    try {
                        boolean jump = false;
                        String[] folders = filePath.split(File.separator);
                        if (folders != null && folders.length > 0){
                            for (String folder :folders){
                                if (!TextUtils.isEmpty(folder) && folder.startsWith(".")){
                                    jump = true;
                                    break;
                                }
                            }
                            if (jump){
                                continue;
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                fileId = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID));
                fileName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE));
                fileType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE));
                fileSize = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE));
//            Logger.e("getAllImageVideoVoice filePath = "+filePath);
                if (filePath.contains("image")){
//                imageId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID));
//                fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME));
//                filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
//                String fileType = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE));
                    FileItem fileItem = new FileItem(fileId, filePath, fileName);
                    fileItem.setFileType(fileType);
                    files.add(fileItem);
                } else {
                    if (fileSize == 0)
                        continue;
                    if (fileType.contains("video/")) {
//                fileCover = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                        fileCover = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA));
                        fileDuration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    } else {
//                歌曲的专辑名：MediaStore.Audio.Media.ALBUM
                        String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                        fileCover = album;
//                歌曲的歌手名：MediaStore.Audio.Media.ARTIST
                        String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
//                歌曲文件的路径：MediaStore.Audio.Media.DATA
                        String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
//                歌曲的总播放时长：MediaStore.Audio.Media.DURATION
                        fileDuration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
//                歌曲文件的大小：MediaStore.Audio.Media.SIZE
                    }
                    Log.e("AllVideoVoice", fileType + " -- " + fileCover + "--" + fileDuration + " -- " + fileSize + " -- " + fileName);
                    FileItem fileItem = new FileItem();
                    fileItem.setImageId(fileId);
                    fileItem.setFileName(fileName);
                    fileItem.setFilePath(filePath);
                    fileItem.setFileType(fileType);
                    fileItem.setFileCover(fileCover);
                    fileItem.setFileDuration(fileDuration);
                    fileItem.setFileSize(fileSize);
                    fileItem.setIsAlbumMedia(1);//0:不是相册中的多媒体文件；1：相册中的多媒体文件
                    files.add(fileItem);
                }
            }
            cursor.close();
            cursor = null;
        } catch (Exception e){
            e.printStackTrace();
        }
        return files;
    }

    /**
     * 查询图片
     */
    public static List<FileItem> getAllPhoto(Context context) {
        List<FileItem> photos = new ArrayList<>();
        String[] projection = new String[]{MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.DISPLAY_NAME, MediaStore.Images.ImageColumns.DATE_MODIFIED};
        //asc 按升序排列
        //desc 按降序排列
        //projection 是定义返回的数据，selection 通常的sql 语句，例如  selection=MediaStore.Images.ImageColumns.MIME_TYPE+"=? " 那么 selectionArgs=new String[]{"jpg"};
//        String selection = MediaStore.Images.ImageColumns.MIME_TYPE + "=? ";
//        String[] selectionArgs=new String[]{"jpg"};
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, MediaStore.Images.ImageColumns.DATE_MODIFIED + "  desc");
        String imageId = null;
        String fileName;
        String filePath;
        String fileType;
        long fileDate;
        while (cursor.moveToNext()) {
            imageId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID));
            fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME));
            filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
            fileDate = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_MODIFIED));
//            fileType = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.MIME_TYPE));
            Log.e("getAllPhoto", "fileDate = "+fileDate+"--" + filePath);
            FileItem fileItem = new FileItem(imageId, filePath, fileName,fileDate,"");
            photos.add(fileItem);
        }
        cursor.close();
        cursor = null;
        return photos;
    }

    /**
     * 查询文本文件
     */
    public static List<FileItem> getAllText(Context context) {
        List<FileItem> texts = new ArrayList<>();
        String[] projection = new String[]{MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.TITLE, MediaStore.Files.FileColumns.MIME_TYPE};
        //相当于我们常用sql where 后面的写法
        String selection = MediaStore.Files.FileColumns.MIME_TYPE + "= ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? ";
        String[] selectionArgs = new String[]{"text/plain", "application/msword", "application/pdf", "application/vnd.ms-powerpoint", "application/vnd.ms-excel"};
        Cursor cursor = context.getContentResolver().query(MediaStore.Files.getContentUri("external"), projection, selection, selectionArgs, MediaStore.Files.FileColumns.DATE_MODIFIED + " desc");
        String fileId;
        String fileName;
        String filePath;
        while (cursor.moveToNext()) {
            fileId = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
            fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE));
            filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
            Log.e("AllText", fileId + " -- " + fileName + " -- " + "--" + cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE)) + filePath);
            FileItem fileItem = new FileItem(fileId, filePath, fileName);
            texts.add(fileItem);
        }
        cursor.close();
        cursor = null;
        return texts;
    }

    /**
     * 查询视频文件
     */
    public static List<FileItem> getAllVideo(Context context) {
        List<FileItem> texts = new ArrayList<>();
        String[] projection = new String[]{MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.TITLE, MediaStore.Files.FileColumns.MIME_TYPE};
        //相当于我们常用sql where 后面的写法
        String selection = MediaStore.Files.FileColumns.MIME_TYPE + "= ? ";
        String[] selectionArgs = new String[]{"video/mp4"};
        Cursor cursor = context.getContentResolver().query(MediaStore.Files.getContentUri("external"), projection, selection, selectionArgs, MediaStore.Files.FileColumns.DATE_MODIFIED + " desc");
        String fileId;
        String fileName;
        String filePath;
        while (cursor.moveToNext()) {
            fileId = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
            fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE));
            filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
            Log.e("video", fileId + " -- " + fileName + " -- " + cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE)) + "--" + filePath);
            FileItem fileItem = new FileItem(fileId, filePath, fileName);
            texts.add(fileItem);
        }
        cursor.close();
        cursor = null;
        return texts;
    }

    /**
     * 查询视频文件
     */
    public static List<FileItem> getAllVideos(Context context) {
        List<FileItem> texts = new ArrayList<>();
        String[] projection = new String[]{MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA, MediaStore.Video.Media.TITLE, MediaStore.Video.Media.MIME_TYPE, MediaStore.Video.Media.DURATION, MediaStore.Video.Media.DATE_MODIFIED};
        //相当于我们常用sql where 后面的写法
        String selection = MediaStore.Video.Media.MIME_TYPE + "= ? ";
        String[] selectionArgs = new String[]{"video/mp4"};
        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, MediaStore.Video.Media.DATE_MODIFIED + " desc");
        String fileId;
        String fileName;
        String filePath;
        String fileType;
        int duration;
        long fileDate;
        while (cursor.moveToNext()) {
            fileId = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
            fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE));
            filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
            fileDate = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_MODIFIED));
            fileType = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE));
            duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
            Log.e("video", fileDate + " -- " + fileName + " -- " + cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE)) + "--" + filePath);
            FileItem fileItem = new FileItem(fileId, filePath, fileName,fileDate,fileType,duration);
            texts.add(fileItem);
        }
        cursor.close();
        cursor = null;
        return texts;
    }

    public static List<FileItem> getAllVideoImages(Context context) {
        List<FileItem> files = new ArrayList<>();
        Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        // 只查询JPEG和PNG扩展名的图片，并按照最近修改时间排序
        StringBuffer selection = new StringBuffer();
//        selection.append(MediaStore.Images.Media.MIME_TYPE).append("= ? or ").append(MediaStore.Images.Media.MIME_TYPE);
//        selection.append(" = ? or ").append(MediaStore.Files.FileColumns.MIME_TYPE).append("= ?");
//        selection.append(" = ? or ").append(MediaStore.Video.Media.MIME_TYPE).append("= ?");
//        String[] args = new String[]{"image/jpeg", "image/png", "video/mp4"};
        selection.append(MediaStore.Images.Media.MIME_TYPE).append("= ? or ").append(MediaStore.Images.Media.MIME_TYPE).append(" = ?");
        String[] args = new String[]{"image/jpeg", "image/png"};
        Cursor cursor = context.getContentResolver().query(MediaStore.Files.getContentUri("external"), null, selection.toString(), args, MediaStore.Images.Media.DATE_MODIFIED);
        List<String> photoUrlList = new ArrayList<String>();
        String fileId;
        String fileName;
        String filePath;
        while (cursor.moveToNext()) {
            // 获取图片的路径
            fileId = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
            fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE));
            filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
            Log.e("AllVideoImages", cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE)) + "--" + filePath);
            FileItem fileItem = new FileItem(fileId, filePath, fileName);
            files.add(fileItem);
        }
        cursor.close();
        cursor = null;
        return files;
    }

}
