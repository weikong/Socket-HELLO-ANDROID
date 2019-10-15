package com.king.chat.socket.ui.view.actionbar;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.king.chat.socket.R;
import com.king.chat.socket.util.BroadCastUtil;
import com.king.chat.socket.util.DisplayUtil;
import com.king.chat.socket.util.socket.SocketUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by maesinfo on 2019/5/15.
 */

public class CommonActionBar extends LinearLayout {

    @BindView(R.id.root)
    LinearLayout root;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_right_tool)
    TextView tv_right_tool;
    @BindView(R.id.iv_right)
    ImageView iv_right;
    @BindView(R.id.view_top_status)
    View viewStatus;
    @BindView(R.id.progressbar)
    ImageView progressbar;


    private int statusHeight = 0;
    private boolean fillStatusBar = false;

    public CommonActionBar(Context context) {
        super(context);
        initView(context);
    }

    public CommonActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CommonActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public CommonActionBar(Context context, boolean fillStatusBar) {
        super(context);
        this.fillStatusBar = fillStatusBar;
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.actionbar_common, this);
        ButterKnife.bind(this, view);
        setStatusHeight(statusHeight);
    }

    public void setFillStatusBar(boolean fillStatusBar) {
        this.fillStatusBar = fillStatusBar;
        setStatusHeight(statusHeight);
    }

    private void setStatusHeight(int height) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT || !fillStatusBar)
            return;
        if (statusHeight == 0)
            statusHeight = DisplayUtil.getStatusBarHeight(getContext());
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewStatus.getLayoutParams();
        params.height = statusHeight;
        viewStatus.setLayoutParams(params);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewGroup.LayoutParams lp = getLayoutParams();
        if (lp != null) {
            int actionHeight = (int) getContext().getResources().getDimension(R.dimen.actionbar_height);
            lp.height = actionHeight + statusHeight;
            lp.width = LayoutParams.MATCH_PARENT;
        }
    }

    public void setBackgroundAlpha(boolean transparent){
        if (transparent)
            root.setBackgroundColor(getResources().getColor(R.color.color_transparent));
        else
            root.setBackgroundColor(getResources().getColor(R.color.color_ffffff));
    }

    public void setTitle(String title) {
        if (tv_title != null) {
            tv_title.setText(title);
        }
    }

    public void setTitleVisiable(int visiable) {
        if (tv_title != null) {
            tv_title.setVisibility(visiable);
        }
    }

    public void setIvBackVisiable(int visiable) {
        if (iv_back != null) {
            iv_back.setVisibility(visiable);
            if (visiable == View.VISIBLE) {
                iv_back.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((Activity) getContext()).finish();
                    }
                });
            }
        }
    }

    public void setRightToolVisiable(int visiable, String text, OnClickListener listener) {
        if (tv_right_tool != null) {
            tv_right_tool.setVisibility(visiable);
            tv_right_tool.setText(text);
            tv_right_tool.setOnClickListener(listener);
        }
    }

    public void setIvRightVisiable(int visiable, OnClickListener listener) {
        if (iv_right != null) {
            iv_right.setVisibility(visiable);
            if (visiable == View.VISIBLE) {
                iv_right.setOnClickListener(listener);
            }
        }
    }

    public void setIvRightSrc(int src, OnClickListener listener) {
        if (iv_right != null) {
            iv_right.setImageResource(src);
            iv_right.setVisibility(View.VISIBLE);
            iv_right.setOnClickListener(listener);
        }
    }

    /**
     * connect:
     * 0、连接成功
     * 1、连接中
     * 2、未连接
     * */

    public void connectSocket(int connect){
        switch (connect){
            case SocketUtil.IM_CONNECTED:
                progressbar.setVisibility(View.INVISIBLE);
                break;
            case SocketUtil.IM_CONNECTING:
                progressbar.setVisibility(View.VISIBLE);
                progressbar.setImageResource(R.drawable.anim_list_more_loading);
                break;
            case SocketUtil.IM_DISCONNECT:
                progressbar.setVisibility(View.VISIBLE);
                progressbar.setImageResource(R.drawable.ic_chat_item_mistake);
                break;
        }
    }

}
