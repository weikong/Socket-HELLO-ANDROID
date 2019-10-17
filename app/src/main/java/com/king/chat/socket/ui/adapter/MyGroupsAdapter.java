package com.king.chat.socket.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.king.chat.socket.GlideApp;
import com.king.chat.socket.R;
import com.king.chat.socket.bean.GroupInfo;
import com.king.chat.socket.ui.view.ImageView.RoundAngleImageView;
import com.king.chat.socket.util.GlideOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maesinfo on 2019/9/19.
 */

public class MyGroupsAdapter<T> extends BaseAdapter {

    private Context mContext;
    private LayoutInflater layoutInflater;
    private List<T> list = new ArrayList<>();

    public MyGroupsAdapter(Context mContext) {
        this.mContext = mContext;
        this.layoutInflater = LayoutInflater.from(mContext);
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list.clear();
        if (list != null) {
            this.list = list;
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
    public T getItem(int position) {
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
            convertView = layoutInflater.inflate(R.layout.adapter_my_groups, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        GroupInfo groupInfo = (GroupInfo) getItem(position);
        if (TextUtils.isEmpty(groupInfo.getGroupheader())){
            viewHolder.tv_name.setText(groupInfo.getGroupname() + "_" + groupInfo.getId());
        } else {
            viewHolder.tv_name.setText(groupInfo.getGroupname());
        }
        GlideApp.with(mContext).applyDefaultRequestOptions(GlideOptions.optionDefaultHeader3()).load(groupInfo.getGroupheader()).dontAnimate().into(viewHolder.iv_header);
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
