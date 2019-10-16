package com.king.chat.socket.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.chat.socket.GlideApp;
import com.king.chat.socket.R;
import com.king.chat.socket.bean.ContactBean;
import com.king.chat.socket.ui.view.ImageView.RoundAngleImageView;
import com.king.chat.socket.util.GlideOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maesinfo on 2019/9/19.
 */

public class GridMembersAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater layoutInflater;
    private List<ContactBean> list = new ArrayList<>();

    public GridMembersAdapter(Context mContext) {
        this.mContext = mContext;
        this.layoutInflater = LayoutInflater.from(mContext);
    }

    public void setData(List<ContactBean> list) {
        this.list.clear();
        if (list != null && list.size() > 0)
            this.list.addAll(list);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
            convertView = layoutInflater.inflate(R.layout.adapter_grid_members, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ContactBean bean = getItem(position);
        if (bean.getId() == 0 && bean.getName().equals("ADD")) {
            viewHolder.tv_name.setText("");
            viewHolder.iv_header.setBackgroundResource(R.drawable.bg_item_rectangle_gray);
            viewHolder.iv_header.setImageResource(R.drawable.icon_add);
            viewHolder.iv_header.setColorFilter(mContext.getResources().getColor(R.color.color_bbbbbb));
        } else {
            viewHolder.tv_name.setText(bean.getName());
            GlideApp.with(mContext).applyDefaultRequestOptions(GlideOptions.optionDefaultHeader2()).load(bean.getHeadPortrait()).dontAnimate().into(viewHolder.iv_header);
            viewHolder.iv_header.clearColorFilter();
        }

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
