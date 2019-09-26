package com.king.chat.socket.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.king.chat.socket.GlideApp;
import com.king.chat.socket.R;
import com.king.chat.socket.bean.ContactBean;
import com.king.chat.socket.ui.DBFlow.session.SessionData;
import com.king.chat.socket.ui.view.ImageView.RoundAngleImageView;
import com.king.chat.socket.util.GlideOptions;
import com.king.chat.socket.util.TimeFormatUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maesinfo on 2019/9/19.
 */

public class ContactAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater layoutInflater;
    private List<ContactBean> list = new ArrayList<>();

    public ContactAdapter(Context mContext) {
        this.mContext = mContext;
        this.layoutInflater = LayoutInflater.from(mContext);
    }

    public List<ContactBean> getList() {
        return list;
    }

    public void setList(List<ContactBean> list) {
        this.list.clear();
        if (list != null) {
            this.list = list;
        }
    }

    public void addList(int position, List<ContactBean> list) {
        if (list != null && list.size() > 0) {
            this.list.addAll(position, list);
        }
    }

    public void addList(List<ContactBean> list) {
        if (list != null && list.size() > 0) {
            this.list.addAll(list);
        }
    }

    public void addData(ContactBean bean) {
        if (bean != null) {
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
    public ContactBean getItem(int position) {
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
            convertView = layoutInflater.inflate(R.layout.adapter_contact, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ContactBean bean = getItem(position);
        viewHolder.tv_name.setText(bean.getName());
        GlideApp.with(mContext).applyDefaultRequestOptions(GlideOptions.optionDefaultHeader2()).load(bean.getHeadPortrait()).dontAnimate().into(viewHolder.iv_header);
        return convertView;
    }

    class ViewHolder {
        RoundAngleImageView iv_header;
        TextView tv_name;

        public ViewHolder(View view) {
            this.iv_header = (RoundAngleImageView) view.findViewById(R.id.iv_header);
            this.tv_name = (TextView) view.findViewById(R.id.tv_name);
        }
    }
}
