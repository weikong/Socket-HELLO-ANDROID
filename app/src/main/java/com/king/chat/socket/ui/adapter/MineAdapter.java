package com.king.chat.socket.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.king.chat.socket.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maesinfo on 2019/9/19.
 */

public class MineAdapter<T> extends BaseAdapter {

    private Context mContext;
    private LayoutInflater layoutInflater;
    private List<T> list = new ArrayList<>();

    public MineAdapter(Context mContext) {
        this.mContext = mContext;
        this.layoutInflater = LayoutInflater.from(mContext);
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list.clear();
        if (list != null) {
            this.list.addAll(list);
        }
    }

    public void addList(List<T> list) {
        if (list != null) {
            this.list.addAll(list);
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
            convertView = layoutInflater.inflate(R.layout.adapter_mine, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_name;

        public ViewHolder(View view) {
            this.tv_name = (TextView) view.findViewById(R.id.tv_name);
        }
    }
}
