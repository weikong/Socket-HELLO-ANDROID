package com.king.chat.socket.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.chat.socket.GlideApp;
import com.king.chat.socket.R;
import com.king.chat.socket.ui.DBFlow.chatRecord.MessageChatType;
import com.king.chat.socket.ui.DBFlow.session.SessionData;
import com.king.chat.socket.util.ChatFaceInputUtil;
import com.king.chat.socket.util.DisplayUtil;
import com.king.chat.socket.util.GlideOptions;
import com.king.chat.socket.util.TimeFormatUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maesinfo on 2019/9/19.
 */

public class MessageAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater layoutInflater;
    private List<SessionData> list = new ArrayList<>();
    private int imageWidth = 100;

    public MessageAdapter(Context mContext) {
        this.mContext = mContext;
        this.layoutInflater = LayoutInflater.from(mContext);
        imageWidth = DisplayUtil.dp2px(100);
    }

    public List<SessionData> getList() {
        return list;
    }

    public void setList(List<SessionData> list) {
        this.list.clear();
        if (list != null) {
            this.list = list;
        }
    }

    public void addList(int position, List<SessionData> list) {
        if (list != null && list.size() > 0) {
            this.list.addAll(position, list);
        }
    }

    public void addList(List<SessionData> list) {
        if (list != null && list.size() > 0) {
            this.list.addAll(list);
        }
    }

    public void addData(SessionData bean) {
        if (bean != null) {
            this.list.add(bean);
        }
    }

    public synchronized void removeData(SessionData bean) {
        int position = -1;
        for (int i = 0; i < list.size(); i++) {
            SessionData sessionData = list.get(i);
            if (sessionData.getMessagefromid() == bean.getMessagefromid()) {
                position = i;
            }
        }
        list.remove(position);
    }

    public void clearData() {
        this.list.clear();
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public SessionData getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.adapter_message, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SessionData bean = getItem(position);
        viewHolder.tv_name.setText(bean.getMessagefromname());
        viewHolder.tv_time.setText(TimeFormatUtils.getSessionFormatDate2(bean.getMessagetime()));
        String strContent = bean.getMessagecontent();
        if (bean.groupdata == 1) {
            if (!TextUtils.isEmpty(bean.getSourcesendername()) && bean.getMessagetype() == 9) {
                strContent = bean.getSourcesendername() + "：" + bean.getMessagecontent();
            }
//            GlideApp.with(mContext).applyDefaultRequestOptions(GlideOptions.optionDefaultHeader3()).load(bean.getMessagefromavatar()).override(imageWidth,imageWidth).dontAnimate().into(viewHolder.iv_header);
            GlideApp.with(mContext).applyDefaultRequestOptions(GlideOptions.optionsTransparentRoundedCorners()).load(bean.getMessagefromavatar()).override(imageWidth,imageWidth).into(viewHolder.iv_header);
        } else {
            GlideApp.with(mContext).applyDefaultRequestOptions(GlideOptions.optionsTransparentRoundedCorners()).load(bean.getMessagefromavatar()).override(imageWidth,imageWidth).into(viewHolder.iv_header);
        }
//        viewHolder.tv_content.setText(strContent);
        ChatFaceInputUtil.getInstance().setExpressionTextView(mContext, strContent, viewHolder.tv_content);
        int unreadCount = bean.getMessage_unread_count();
        if (unreadCount > 0) {
            viewHolder.tv_unread.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tv_unread.setVisibility(View.INVISIBLE);
        }
        if (unreadCount > 99) {
            unreadCount = 99;
        }
        viewHolder.tv_unread.setText("" + unreadCount);
        int messageChatType = bean.getMessagechattype();
        switch (messageChatType) {
            case MessageChatType.TYPE_IMG:
                viewHolder.tv_content.setText(mContext.getString(R.string.text_chat_image));
                break;
            case MessageChatType.TYPE_VOICE:
                viewHolder.tv_content.setText(mContext.getString(R.string.text_chat_voice));
                break;
            case MessageChatType.TYPE_VIDEO:
                viewHolder.tv_content.setText(mContext.getString(R.string.text_chat_video));
                break;
        }
        return convertView;
    }

    class ViewHolder {
        ImageView iv_header;
        TextView tv_name;
        TextView tv_time;
        TextView tv_content;
        TextView tv_unread;

        public ViewHolder(View view) {
            this.iv_header = (ImageView) view.findViewById(R.id.iv_header);
            this.tv_name = (TextView) view.findViewById(R.id.tv_name);
            this.tv_content = (TextView) view.findViewById(R.id.tv_content);
            this.tv_time = (TextView) view.findViewById(R.id.tv_time);
            this.tv_unread = (TextView) view.findViewById(R.id.tv_unread);
        }
    }
}
