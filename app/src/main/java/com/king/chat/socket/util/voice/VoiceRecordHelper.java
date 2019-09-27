/*
 * Copyright 2014-2024 setNone. All rights reserved. 
 */
package com.king.chat.socket.util.voice;

import android.media.MediaRecorder;

import com.king.chat.socket.util.AppManager;
import com.king.chat.socket.util.SDCardUtil;

import java.io.File;

/**
 * VoiceRecordHelper.java - 调用麦克风录音帮助类
 *
 * @author Kevin.Zhang
 *         <p/>
 *         2014-4-22 上午9:20:13
 */
public class VoiceRecordHelper {

    /**
     * 保证全局只有一个录音对象
     */
    private static MediaRecorder mRecorder = null;

    private static class VoiceRecordHelperHolder {
        private static final VoiceRecordHelper INSTANCE = new VoiceRecordHelper();
    }

    /**
     * 单一实例
     */
    public static final VoiceRecordHelper getInstance() {
        return VoiceRecordHelperHolder.INSTANCE;
    }


    /**
     * 开始录制
     *
     * @param fileName
     * @return false:录制失败
     */
    public boolean beginRecording(String fileName) {
        synchronized (VoiceRecordHelper.class) {
            if (mRecorder != null) {
                stopRecording();
            }
            try {
                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                String filePath = SDCardUtil.getVoiceDir() + File.separator + fileName;
                File file = new File(filePath);
                if (!file.exists()) {
                    file.createNewFile();
                }
                mRecorder.setOutputFile(filePath);
                mRecorder.prepare();
                mRecorder.start();
            } catch (Exception e) {
                stopRecording();
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }

    public void stopRecording() {
        synchronized (VoiceRecordHelper.class) {
            if (mRecorder == null) {
                return;
            }
            try {
                mRecorder.release();
                mRecorder = null;
            } catch (Exception e) {

            }
        }
    }

}
