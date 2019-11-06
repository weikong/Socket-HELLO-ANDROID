package com.king.chat.socket.ui.view.setting;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.chat.socket.GlideApp;
import com.king.chat.socket.R;
import com.king.chat.socket.bean.ContactBean;
import com.king.chat.socket.ui.activity.edit.MineEditActivity;
import com.king.chat.socket.ui.view.ImageView.RoundAngleImageView;
import com.king.chat.socket.util.GlideOptions;
import com.king.chat.socket.util.UserInfoManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by maesinfo on 2019/5/15.
 */

public class SettingHeaderView extends LinearLayout {

    @BindView(R.id.layout_header)
    RelativeLayout layout_header;
    @BindView(R.id.iv_header)
    RoundAngleImageView iv_header;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_phone)
    TextView tv_phone;

    public SettingHeaderView(Context context) {
        super(context);
        initView(context);
    }

    public SettingHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SettingHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_setting_header, this);
        ButterKnife.bind(this, view);
        layout_header.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserInfoManager.getInstance().getContactBean() == null)
                    return;
                Intent intent = new Intent(getContext(), MineEditActivity.class);
                getContext().startActivity(intent);
            }
        });
        setData();
    }

    public void setData() {
        ContactBean contactBean = UserInfoManager.getInstance().getContactBean();
        tv_name.setText(contactBean.getName());
        tv_phone.setText(contactBean.getAccount());
        GlideApp.with(getContext()).applyDefaultRequestOptions(GlideOptions.optionDefaultHeader4()).load(contactBean.getHeadPortrait()).into(iv_header);
    }

}
