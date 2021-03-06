package com.king.chat.socket.ui.view.chat.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.chat.socket.GlideApp;
import com.king.chat.socket.R;
import com.king.chat.socket.ui.DBFlow.chatRecord.ChatRecordData;
import com.king.chat.socket.ui.DBFlow.chatRecord.MessageChatType;
import com.king.chat.socket.ui.activity.media.ShowMediaPlayActivity;
import com.king.chat.socket.util.ChatFaceInputUtil;
import com.king.chat.socket.util.DisplayUtil;
import com.king.chat.socket.util.GlideOptions;
import com.king.chat.socket.util.voice.VoiceMediaPlayHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by maesinfo on 2019/5/15.
 */

public class ChatContentRightView extends RelativeLayout {

    @BindView(R.id.layout_root)
    RelativeLayout layout_root;
    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.iv_content)
    ImageView iv_content;
    @BindView(R.id.iv_viedo_play)
    ImageView iv_viedo_play;
    @BindView(R.id.iv_voice_play)
    ImageView iv_voice_play;
    private int imageWidth = 100;
    private int textWidth = 120;

    public ChatContentRightView(Context context) {
        super(context);
        initView(context);
    }

    public ChatContentRightView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ChatContentRightView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_chat_content_right, this);
        ButterKnife.bind(this, view);
        imageWidth = DisplayUtil.dp2px(200);
        textWidth = DisplayUtil.screenWidth - DisplayUtil.dp2px(120);
    }

    public void setData(final ChatRecordData bean) {
        layout_root.setBackgroundResource(R.drawable.bg_chat_send_item_picture_nor);
        int messageChatType = bean.getMessagechattype();
        switch (messageChatType) {
            case MessageChatType.TYPE_TEXT:
                tv_content.setMaxWidth(textWidth);
                tv_content.setVisibility(View.VISIBLE);
                iv_content.setVisibility(View.GONE);
                iv_viedo_play.setVisibility(View.GONE);
                iv_voice_play.setVisibility(View.GONE);
//                tv_content.setText(bean.getMessagecontent());
                ChatFaceInputUtil.getInstance().setExpressionTextView(getContext(),bean.getMessagecontent(),tv_content);
                break;
            case MessageChatType.TYPE_IMG:
                tv_content.setVisibility(View.GONE);
                iv_content.setVisibility(View.VISIBLE);
                iv_viedo_play.setVisibility(View.GONE);
                iv_voice_play.setVisibility(View.GONE);
//                GlideApp.with(getContext()).applyDefaultRequestOptions(GlideOptions.optionsGrayItem()).asGif().load(bean.getMessagecontent()).into(iv_content);
                String url = bean.getMessagecontent();
                if (!TextUtils.isEmpty(url) && url.toLowerCase().endsWith("gif")){
                    layout_root.setBackgroundColor(getResources().getColor(R.color.color_transparent));
//                    GlideApp.with(getContext()).applyDefaultRequestOptions(GlideOptions.optionsTransparent()).asGif().load(url).override(imageWidth,imageWidth).into(iv_content);
                    GlideApp.with(getContext()).applyDefaultRequestOptions(GlideOptions.optionsTransparentRoundedCorners()).asGif().load(url).into(iv_content);
                } else {
//                    GlideApp.with(getContext()).applyDefaultRequestOptions(GlideOptions.optionsTransparent()).load(url).override(imageWidth,imageWidth).into(iv_content);
                    GlideApp.with(getContext()).applyDefaultRequestOptions(GlideOptions.optionsTransparentRoundedCorners()).load(url).into(iv_content);
                }
                iv_content.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShowMediaPlayActivity.startActivity(getContext(),bean);
                    }
                });
                iv_content.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (callBack != null){
                            callBack.longClickImage(v,bean);
                        }
                        return true;
                    }
                });
                break;
            case MessageChatType.TYPE_VIDEO:
                tv_content.setVisibility(View.GONE);
                iv_content.setVisibility(View.VISIBLE);
                iv_viedo_play.setVisibility(View.VISIBLE);
                iv_voice_play.setVisibility(View.GONE);
//                GlideApp.with(getContext()).applyDefaultRequestOptions(GlideOptions.optionsGrayItem()).load(bean.getMessagecontent()).dontAnimate().into(iv_content);
                GlideApp.with(getContext()).applyDefaultRequestOptions(GlideOptions.optionsTransparentRoundedCorners()).load(bean.getMessagecontent()).into(iv_content);
                iv_viedo_play.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShowMediaPlayActivity.startActivity(getContext(),bean);
                    }
                });
                break;
            case MessageChatType.TYPE_VOICE:
                tv_content.setVisibility(View.GONE);
                iv_content.setVisibility(View.GONE);
                iv_viedo_play.setVisibility(View.GONE);
                iv_voice_play.setVisibility(View.VISIBLE);
                iv_voice_play.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AnimationDrawable mVoiceAnim = (AnimationDrawable) iv_voice_play.getDrawable();
                        if (mVoiceAnim != null) {
                            mVoiceAnim.setVisible(true, true);
                            mVoiceAnim.start();
                        }
                        VoiceMediaPlayHelper.getInstance().playVoiceUrl(getContext(), bean, iv_voice_play);
                    }
                });
                iv_voice_play.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (callBack != null){
                            callBack.longClickVoice(v,bean);
                        }
                        return true;
                    }
                });
                break;
        }
    }

    CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack{
        public void longClickImage(View v,ChatRecordData bean);
        public void longClickVoice(View v,ChatRecordData bean);
    }
}
