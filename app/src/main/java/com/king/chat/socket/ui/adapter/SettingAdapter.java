package com.king.chat.socket.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.chat.socket.R;
import com.king.chat.socket.bean.MineBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maesinfo on 2019/5/15.
 */

public class SettingAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<MineBean> list = new ArrayList<>();

    public SettingAdapter(Context context) {
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
    }

    public List<MineBean> getList() {
        return list;
    }

    public void setList(List<MineBean> list) {
        this.list.clear();
        if (list != null && list.size() > 0) {
            this.list.addAll(list);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_setting, null);
            viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.iv_more = (ImageView) convertView.findViewById(R.id.iv_more);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MineBean bean = (MineBean) getItem(position);
        viewHolder.iv_icon.setImageResource(bean.getDrawable());
        viewHolder.tv_title.setText(bean.getTitle());
        return convertView;
    }

    private class ViewHolder {
        ImageView iv_icon;
        TextView tv_title;
        ImageView iv_more;
    }
}
