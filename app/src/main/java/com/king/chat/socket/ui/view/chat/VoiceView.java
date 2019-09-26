package com.king.chat.socket.ui.view.chat;

import android.animation.AnimatorSet;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.king.chat.socket.R;
import com.king.chat.socket.util.AnimUtil;

/**
 * Created by maesinfo on 2019/5/15.
 */

public class VoiceView extends RelativeLayout {

    private ImageView iv_voice,iv_voice_circle,iv_voice_circle2;
    AnimatorSet animatorSet1,animatorSet2;

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
        iv_voice = (ImageView)view.findViewById(R.id.iv_voice);
        iv_voice_circle = (ImageView)view.findViewById(R.id.iv_voice_circle);
        iv_voice_circle2 = (ImageView)view.findViewById(R.id.iv_voice_circle2);
        iv_voice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        iv_voice.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        animatorSet1 = AnimUtil.scaleAnim(iv_voice_circle,1.0f,4.5f,0,2000);
                        animatorSet2 = AnimUtil.scaleAnim(iv_voice_circle2,1.0f,4.5f,200,2000);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (animatorSet1 != null){
                            animatorSet1.end();
                            animatorSet1.cancel();
                        }
                        if (animatorSet2 != null){
                            animatorSet2.end();
                            animatorSet2.cancel();
                        }
                        break;
                }
                return false;
            }
        });
    }
}
