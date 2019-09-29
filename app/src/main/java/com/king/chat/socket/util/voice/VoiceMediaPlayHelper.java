/*
 * Copyright 2014-2024 setNone. All rights reserved. 
 */
package com.king.chat.socket.util.voice;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.widget.ImageView;

import com.king.chat.socket.R;
import com.king.chat.socket.ui.DBFlow.chatRecord.ChatRecordData;
import com.king.chat.socket.util.Logger;
import com.king.chat.socket.util.SDCardUtil;

import java.io.File;

/**
 * VoiceRecordHelper.java - 调用麦克风录音帮助类
 *
 * @author Kevin.Zhang
 *         <p/>
 *         2014-4-22 上午9:20:13
 */
public class VoiceMediaPlayHelper {

    /**
     * 保证全局只有一个
     */
    private MediaPlayer mVoiceMediaPlayer = null;
    private ImageView ivAnimVoice;
    private ChatRecordData chatRecordData;

    private static class VoiceMediaPlayHelperHolder {
        private static final VoiceMediaPlayHelper INSTANCE = new VoiceMediaPlayHelper();
    }

    /**
     * 单一实例
     */
    public static final VoiceMediaPlayHelper getInstance() {
        return VoiceMediaPlayHelperHolder.INSTANCE;
    }

    public void playVoiceUrl(Context context, ChatRecordData bean, final ImageView ivAnim) {
        if (this.chatRecordData != null && bean != null){
            if (!this.chatRecordData.getMessagecontent().equals(bean.getMessagecontent())){
                clearAnim(this.ivAnimVoice);
            }
        }
        this.chatRecordData = bean;
        this.ivAnimVoice = ivAnim;
        if (bean == null) {
            Logger.e("--->UN 语音文件测量失败！无效的下载数据!");
            return;
        }
        String url = bean.getMessagecontent();
        if (url == null || url.length() == 0) {
            Logger.e("--->UN 语音文件测量失败！无效的下载地址! url:" + url);
            return;
        }

        if (!url.startsWith("http") && !url.startsWith("file://"))
            url = "file://" + url;
        try {
            releaseVoice();
            mVoiceMediaPlayer = new MediaPlayer();
            mVoiceMediaPlayer.setDataSource(context, Uri.parse(url));
            mVoiceMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Logger.e("VoiceMediaPlayHelper onPrepared");
                    mp.start();
                }
            });
            mVoiceMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Logger.e("VoiceMediaPlayHelper onError");
                    clearAnim(ivAnim);
                    return true;
                }
            });
            mVoiceMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Logger.e("VoiceMediaPlayHelper onCompletion");
                    clearAnim(ivAnim);
                }
            });
            mVoiceMediaPlayer.prepareAsync();
        } catch (Exception e) {
            releaseVoice();
            e.printStackTrace();
            Logger.e("--->UN 语音文件测量失败！");
        }
    }

    public void releaseVoice() {
        if (mVoiceMediaPlayer == null)
            return;
        try {
            if (mVoiceMediaPlayer != null) {
                mVoiceMediaPlayer.release();
                mVoiceMediaPlayer = null;
            }
        } catch (Exception e) {
            Logger.e("--->UN 回收media资源失败！");
        }
    }

    public void clearAnim(ImageView imageView) {
        if (imageView == null)
            return;
        try {
            AnimationDrawable mVoiceAnim = (AnimationDrawable) imageView.getDrawable();
            if (mVoiceAnim != null && mVoiceAnim.isRunning()) {
                mVoiceAnim.stop();
                mVoiceAnim.selectDrawable(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void openAnim(ImageView imageView) {
        if (imageView == null)
            return;
        AnimationDrawable mVoiceAnim = (AnimationDrawable) imageView.getDrawable();
        if (mVoiceAnim != null) {
            mVoiceAnim.setVisible(true, true);
            mVoiceAnim.start();
        }
    }
}
