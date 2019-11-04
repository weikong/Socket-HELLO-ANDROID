package com.king.chat.socket.ui.view.chat;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.chat.socket.R;
import com.king.chat.socket.util.AnimUtil;
import com.king.chat.socket.util.SDCardUtil;
import com.king.chat.socket.util.TimeFormatUtils;
import com.king.chat.socket.util.ToastUtil;
import com.king.chat.socket.util.voice.VoiceRecordHelper;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by maesinfo on 2019/5/15.
 */

public class VoiceView extends RelativeLayout {

    private TextView tv_time;
    private ImageView iv_voice, iv_voice_circle, iv_voice_circle2;
    private AnimatorSet animatorSet1, animatorSet2;
    private long dur = 0;
    private String fileName;
    private String filePath;

    private Timer mTimer;
    private TimerTask mTimerTask;
    ObjectAnimator animator;

    public VoiceView(Context context) {
        super(context);
        initView(context);
    }

    public VoiceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public VoiceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_voice, this);
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        iv_voice = (ImageView) view.findViewById(R.id.iv_voice);
        iv_voice_circle = (ImageView) view.findViewById(R.id.iv_voice_circle);
        iv_voice_circle2 = (ImageView) view.findViewById(R.id.iv_voice_circle2);
        iv_voice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        iv_voice.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        startRecord();
                        break;
                    case MotionEvent.ACTION_UP:
                        stopRecord();
                        break;
                }
                return false;
            }
        });
    }

    private void startRecord(){
        animatorSet1 = AnimUtil.scaleAnim(iv_voice_circle, 1.0f, 4.5f, 0, 2000);
        animatorSet2 = AnimUtil.scaleAnim(iv_voice_circle2, 1.0f, 4.5f, 200, 2000);
        dur = System.currentTimeMillis();
        fileName = new StringBuffer().append(dur).append(".m4a").toString();
        boolean isStart = VoiceRecordHelper.getInstance().beginRecording(fileName);
        if (isStart){
            initTimer();
            animator = AnimUtil.alphaAnimLoop(tv_time,0,3000);
        }
    }

    private void stopRecord(){
        if (VoiceRecordHelper.getInstance().getmRecorder() == null)
            return;
        try {
            if (animatorSet1 != null) {
                animatorSet1.end();
                animatorSet1.cancel();
            }
            if (animatorSet2 != null) {
                animatorSet2.end();
                animatorSet2.cancel();
            }
            boolean isStop = VoiceRecordHelper.getInstance().stopRecording();
            unInitTimer();
            String filePath = SDCardUtil.getVoiceDir() + File.separator + fileName;
            long nowTime = System.currentTimeMillis();
            if (nowTime - dur < 1000) {
                ToastUtil.show("录制时间太短");
                File file = new File(filePath);
                if (file.exists()) {
                    file.delete();
                }
            } else {
                if (callBack != null) {
                    callBack.recordVoice(filePath);
                }
            }
            dur = 0;
            timeHandler.sendEmptyMessage(2);
            animator.cancel();
            animator = null;
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private Handler timeHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1://开始录音
                    long duration = System.currentTimeMillis() - dur;
                    long second = duration / 1000;
                    if (second > 60){
                        //录音超过60秒，结束录音
                        stopRecord();
                        return;
                    }
                    String strTime = TimeFormatUtils.formatDurationMMss(duration);
                    tv_time.setText(strTime);
                    break;
                case 2://结束录音
                    tv_time.setAlpha(1.0f);
                    tv_time.setText("00:00");
                    break;

            }
        }
    };

    private void initTimer(){
        try {
            if (mTimer != null){
                mTimer.cancel();
                mTimer = null;
            }
            if (mTimerTask != null){
                mTimerTask.cancel();
                mTimerTask = null;
            }
            mTimer = new Timer();
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    timeHandler.sendEmptyMessage(1);
                }
            };
            mTimer.schedule(mTimerTask,1000,100);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void unInitTimer(){
        try {
            if (mTimer != null){
                mTimer.cancel();
                mTimer = null;
            }
            if (mTimerTask != null){
                mTimerTask.cancel();
                mTimerTask = null;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack {
        public void recordVoice(String path);
    }
}
