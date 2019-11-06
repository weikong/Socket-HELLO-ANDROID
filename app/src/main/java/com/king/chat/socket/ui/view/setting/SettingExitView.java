package com.king.chat.socket.ui.view.setting;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.king.chat.socket.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by maesinfo on 2019/5/15.
 */

public class SettingExitView extends LinearLayout {

    @BindView(R.id.tv_exit)
    TextView tv_exit;

    public SettingExitView(Context context) {
        super(context);
        initView(context);
    }

    public SettingExitView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SettingExitView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_setting_exit, this);
        ButterKnife.bind(this, view);
    }

    public void setData() {

    }

}
