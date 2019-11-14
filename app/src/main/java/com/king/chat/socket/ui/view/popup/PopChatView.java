package com.king.chat.socket.ui.view.popup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.king.chat.socket.R;
import com.king.chat.socket.config.Config;
import com.king.chat.socket.ui.DBFlow.chatRecord.ChatRecordData;
import com.king.chat.socket.ui.DBFlow.chatRecord.MessageChatType;
import com.king.chat.socket.ui.view.dialog.ProgressMyDialog;
import com.king.chat.socket.util.DisplayUtil;
import com.king.chat.socket.util.SDCardUtil;
import com.king.chat.socket.util.httpUtil.OkHttpClientManager;
import com.squareup.okhttp.Request;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by maesinfo on 2019/5/15.
 */

public class PopChatView extends LinearLayout implements View.OnClickListener {

    @BindView(R.id.layout_root)
    LinearLayout layout_root;
    @BindView(R.id.layout_pop)
    LinearLayout layout_pop;
    @BindView(R.id.tv_copy)
    TextView tv_copy;
    @BindView(R.id.tv_forword)
    TextView tv_forword;
    @BindView(R.id.tv_save)
    TextView tv_save;
    @BindView(R.id.tv_collect)
    TextView tv_collect;
    @BindView(R.id.tv_del)
    TextView tv_del;

    ChatRecordData bean;

    public PopChatView(Context context) {
        super(context);
        initView(context);
    }

    public PopChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PopChatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_pop_chat, this);
        ButterKnife.bind(this, view);
        tv_copy.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        tv_forword.setOnClickListener(this);
        tv_del.setOnClickListener(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void setLayoutPopLocation(int location) {
        LayoutParams params = (LayoutParams) layout_pop.getLayoutParams();
        params.topMargin = location;
        layout_pop.setLayoutParams(params);
    }

    public void setData(ChatRecordData bean) {
        this.bean = bean;
        if (bean == null)
            return;
        if (Config.userId.equalsIgnoreCase(bean.getSourcesenderid())) {
            //自己发送的消息
            layout_root.setGravity(Gravity.RIGHT);
            layout_pop.setBackgroundResource(R.drawable.bg_chat_edit_pop_right);
            LayoutParams params = (LayoutParams) layout_pop.getLayoutParams();
            params.rightMargin = DisplayUtil.dp2px(60);
        } else {
            //接收到的消息
            layout_root.setGravity(Gravity.LEFT);
            layout_pop.setBackgroundResource(R.drawable.bg_chat_edit);
            LayoutParams params = (LayoutParams) layout_pop.getLayoutParams();
            params.leftMargin = DisplayUtil.dp2px(60);
        }
        int messageChatType = bean.getMessagechattype();
        switch (messageChatType) {
            case MessageChatType.TYPE_TEXT:
                tv_copy.setVisibility(View.VISIBLE);
                tv_save.setVisibility(View.GONE);
                tv_collect.setVisibility(View.GONE);
                break;
            case MessageChatType.TYPE_IMG:
                tv_copy.setVisibility(View.GONE);
                tv_save.setVisibility(View.VISIBLE);
                tv_collect.setVisibility(View.VISIBLE);
                break;
            case MessageChatType.TYPE_VIDEO:
                tv_copy.setVisibility(View.GONE);
                tv_save.setVisibility(View.VISIBLE);
                tv_collect.setVisibility(View.VISIBLE);
                break;
            case MessageChatType.TYPE_VOICE:
                tv_copy.setVisibility(View.GONE);
                tv_save.setVisibility(View.GONE);
                tv_collect.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_copy:
                DisplayUtil.copy(getContext(), bean.getMessagecontent());
                callBack.actionCopy(bean);
                break;
            case R.id.tv_forword:
                callBack.actionForword(bean);
                break;
            case R.id.tv_save:
                final ProgressMyDialog myDialog = loadDialog();
                OkHttpClientManager.downloadAsyn(bean.getMessagecontent(), SDCardUtil.getImgDir(), new OkHttpClientManager.StringProgressCallback() {
                    @Override
                    public void onProgress(float progress) {
                        myDialog.setProgress((int) (progress * 100));
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                        myDialog.setProgressState(false);
                    }

                    @Override
                    public void onResponse(String response) {
                        myDialog.setProgressState(true);
                    }
                });
                callBack.actionSave(bean);
                break;
            case R.id.tv_collect:
                callBack.actionCollect(bean);
                break;
            case R.id.tv_del:
                callBack.actionDel(bean);
                break;
        }
    }

    CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack {
        void actionCopy(ChatRecordData bean);

        void actionForword(ChatRecordData bean);

        void actionSave(ChatRecordData bean);

        void actionCollect(ChatRecordData bean);

        void actionDel(ChatRecordData bean);
    }

    private ProgressMyDialog loadDialog() {
        ProgressMyDialog myDialog = new ProgressMyDialog(getContext());
        myDialog.show();
        return myDialog;
    }
}
