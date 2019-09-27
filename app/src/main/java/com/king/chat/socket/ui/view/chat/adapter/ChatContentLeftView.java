package com.king.chat.socket.ui.view.chat.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.chat.socket.GlideApp;
import com.king.chat.socket.R;
import com.king.chat.socket.ui.DBFlow.chatRecord.ChatRecordData;
import com.king.chat.socket.ui.DBFlow.chatRecord.MessageChatType;
import com.king.chat.socket.ui.activity.base.BaseDataActivity;
import com.king.chat.socket.ui.activity.camera.CameraActivity;
import com.king.chat.socket.ui.adapter.GridMoreAdapter;
import com.king.chat.socket.ui.view.ImageView.RoundAngleImageView;
import com.king.chat.socket.ui.view.gridview.CustomGridView;
import com.king.chat.socket.util.GlideOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by maesinfo on 2019/5/15.
 */

public class ChatContentLeftView extends RelativeLayout {

    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.iv_content)
    RoundAngleImageView iv_content;

    public ChatContentLeftView(Context context) {
        super(context);
        initView(context);
    }

    public ChatContentLeftView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ChatContentLeftView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_chat_content_left, this);
        ButterKnife.bind(this,view);
    }

    public void setData(ChatRecordData bean){
        int messageChatType = bean.getMessagechattype();
        switch (messageChatType){
            case MessageChatType.TYPE_TEXT:
                tv_content.setVisibility(View.VISIBLE);
                iv_content.setVisibility(View.GONE);
                tv_content.setText(bean.getMessagecontent());
                break;
            case MessageChatType.TYPE_IMG:
                tv_content.setVisibility(View.GONE);
                iv_content.setVisibility(View.VISIBLE);
                GlideApp.with(getContext()).applyDefaultRequestOptions(GlideOptions.optionsGrayItem()).load(bean.getMessagecontent()).dontAnimate().into(iv_content);
                break;
        }
    }
}
