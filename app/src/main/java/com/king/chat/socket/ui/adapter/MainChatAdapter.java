package com.king.chat.socket.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.king.chat.socket.GlideApp;
import com.king.chat.socket.R;
import com.king.chat.socket.bean.ContactBean;
import com.king.chat.socket.ui.DBFlow.chatRecord.ChatRecordData;
import com.king.chat.socket.config.Config;
import com.king.chat.socket.ui.DBFlow.chatRecord.MessageChatType;
import com.king.chat.socket.ui.view.ImageView.RoundAngleImageView;
import com.king.chat.socket.ui.view.chat.adapter.ChatContentLeftView;
import com.king.chat.socket.ui.view.chat.adapter.ChatContentRightView;
import com.king.chat.socket.ui.view.popup.CustomPopupWindow;
import com.king.chat.socket.ui.view.popup.PopChatView;
import com.king.chat.socket.util.DisplayUtil;
import com.king.chat.socket.util.GlideOptions;
import com.king.chat.socket.util.Logger;
import com.king.chat.socket.util.TimeFormatUtils;
import com.king.chat.socket.util.ToastUtil;
import com.king.chat.socket.util.UserInfoManager;
import com.king.chat.socket.util.socket.SocketUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maesinfo on 2019/9/19.
 */

public class MainChatAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater layoutInflater;
    private List<ChatRecordData> list = new ArrayList<>();
    private ContactBean contactBean;

    public MainChatAdapter(Context mContext) {
        this.mContext = mContext;
        this.layoutInflater = LayoutInflater.from(mContext);
    }

    public void setContactBean(ContactBean contactBean) {
        this.contactBean = contactBean;
    }

    public List<ChatRecordData> getList() {
        return list;
    }

    public void setList(List<ChatRecordData> list) {
        this.list.clear();
        if (list != null){
            this.list = list;
        }
    }

    public void addList(int position,List<ChatRecordData> list) {
        if (list != null && list.size() > 0){
            this.list.addAll(position,list);
        }
    }

    public void addList(List<ChatRecordData> list) {
        if (list != null && list.size() > 0){
            this.list.addAll(list);
        }
    }

    public void addData(ChatRecordData bean) {
        if (bean != null){
            this.list.add(bean);
        }
    }

    public void clearData() {
        this.list.clear();
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public ChatRecordData getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (Config.userId.equalsIgnoreCase(getItem(position).getMessagefromid())){
            return 1; //自己发送的消息
        } else {
            return 2; //接收到的消息
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        int type = getItemViewType(position);
        final ChatRecordData bean = getItem(position);
        int messageChatType = bean.getMessagechattype();
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.adapter_chat,null);
            viewHolder = new ViewHolder(convertView,type);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder.getType() != type){
                viewHolder = new ViewHolder(convertView,type);
                convertView.setTag(viewHolder);
            }
        }
        String strNameTime = "";
        if (type == 1){
            strNameTime = TimeFormatUtils.getSessionFormatDate(bean.getMessagetime()) + "  " + bean.getMessagefromname();
            GlideApp.with(mContext).applyDefaultRequestOptions(GlideOptions.optionDefaultHeader()).load(UserInfoManager.getInstance().getContactBean().getHeadPortrait()).dontAnimate().into(viewHolder.iv_header);
            viewHolder.chatContentRightView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (callBack != null){
                        callBack.showRightPop(v,bean);
                    }
                    return false;
                }
            });
            viewHolder.chatContentRightView.setCallBack(new ChatContentRightView.CallBack() {
                @Override
                public void longClickImage(View v, ChatRecordData bean) {
                    if (callBack != null){
                        callBack.showRightPop(v,bean);
                    }
                }

                @Override
                public void longClickVoice(View v, ChatRecordData bean) {
                    if (callBack != null){
                        callBack.showRightPop(v,bean);
                    }
                }
            });
        } else {
            strNameTime = bean.getMessagefromname() + "  " + TimeFormatUtils.getSessionFormatDate(bean.getMessagetime());
            GlideApp.with(mContext).applyDefaultRequestOptions(GlideOptions.optionDefaultHeader()).load(contactBean.getHeadPortrait()).dontAnimate().into(viewHolder.iv_header);
            viewHolder.chatContentLeftView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (callBack != null){
                        callBack.showLeftPop(v,bean);
                    }
                    return false;
                }
            });
            viewHolder.chatContentLeftView.setCallBack(new ChatContentLeftView.CallBack() {
                @Override
                public void longClickImage(View v, ChatRecordData bean) {
                    if (callBack != null){
                        callBack.showLeftPop(v,bean);
                    }
                }

                @Override
                public void longClickVoice(View v, ChatRecordData bean) {
                    if (callBack != null){
                        callBack.showLeftPop(v,bean);
                    }
                }
            });
        }
        viewHolder.tv_name_time.setText(strNameTime);
        viewHolder.iv_send_error.setVisibility(View.INVISIBLE);
        if (bean.getMessagestate() == 0 && type == 1){
            viewHolder.progressbar.setVisibility(View.VISIBLE);
        } else if (bean.getMessagestate() == 9 && type == 1){
            viewHolder.progressbar.setVisibility(View.INVISIBLE);
            viewHolder.iv_send_error.setVisibility(View.VISIBLE);
        }else {
            viewHolder.progressbar.setVisibility(View.INVISIBLE);
        }
        viewHolder.iv_send_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocketUtil.getInstance().reSendContent(bean);
            }
        });
        switch (bean.getMessagechattype()){
            case MessageChatType.TYPE_IMG:
                if (type == 1){
                    viewHolder.chatContentRightView.setData(bean);
                } else {
                    viewHolder.chatContentLeftView.setData(bean);
                }
                break;
            default:
                if (type == 1){
                    viewHolder.chatContentRightView.setData(bean);
                } else {
                    viewHolder.chatContentLeftView.setData(bean);
                }
                break;
        }
        return convertView;
    }

    class ViewHolder{
        int type = 0;
        View viewLeft;
        View viewRight;
        ImageView iv_header;
        TextView tv_name_time;
        ProgressBar progressbar;
        ImageView iv_send_error;
        ChatContentLeftView chatContentLeftView;
        ChatContentRightView chatContentRightView;

        public int getType() {
            return type;
        }

        public ViewHolder(View view, int type) {
            this.type = type;
            this.viewLeft = (View)view.findViewById(R.id.layout_left);
            this.viewRight = (View)view.findViewById(R.id.layout_right);
            viewLeft.setVisibility(View.GONE);
            viewRight.setVisibility(View.GONE);
            if (type == 1){ //自己发送的消息
                viewRight.setVisibility(View.VISIBLE);
                this.iv_header = (ImageView)viewRight.findViewById(R.id.iv_header);
                this.tv_name_time = (TextView)viewRight.findViewById(R.id.tv_name_time);
                this.progressbar = (ProgressBar)viewRight.findViewById(R.id.progressbar);
                this.iv_send_error = (ImageView)viewRight.findViewById(R.id.iv_send_error);
                this.chatContentRightView = (ChatContentRightView)viewRight.findViewById(R.id.content_right_view);
            } else { //接收到的消息
                viewLeft.setVisibility(View.VISIBLE);
                this.iv_header = (ImageView)viewLeft.findViewById(R.id.iv_header);
                this.tv_name_time = (TextView)viewLeft.findViewById(R.id.tv_name_time);
                this.progressbar = (ProgressBar)viewLeft.findViewById(R.id.progressbar);
                this.iv_send_error = (ImageView)viewLeft.findViewById(R.id.iv_send_error);
                this.chatContentLeftView = (ChatContentLeftView)viewLeft.findViewById(R.id.content_left_view);
            }
        }
    }

    CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack{
        public void showLeftPop(View v,ChatRecordData bean);
        public void showRightPop(View v,ChatRecordData bean);
    }
}
