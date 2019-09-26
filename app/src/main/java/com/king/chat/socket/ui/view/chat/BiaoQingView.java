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

public class BiaoQingView extends RelativeLayout {

    public BiaoQingView(Context context) {
        super(context);
        initView(context);
    }

    public BiaoQingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BiaoQingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_biao_qing, this);
    }
}
