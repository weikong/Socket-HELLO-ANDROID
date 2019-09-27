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

public class GridMoreAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater layoutInflater;
    private String[] gridMore;
    private int[] images = {R.drawable.btn_chat_more_item_image,R.drawable.btn_chat_more_item_camera
            ,R.drawable.btn_chat_more_item_online_voice,R.drawable.btn_chat_more_item_online_video
            ,R.drawable.btn_chat_more_item_wallet,R.drawable.btn_chat_more_item_file};

    public GridMoreAdapter(Context mContext) {
        this.mContext = mContext;
        this.layoutInflater = LayoutInflater.from(mContext);
        gridMore = mContext.getResources().getStringArray(R.array.array_grid_more);
    }

    @Override
    public int getCount() {
        return gridMore == null ? 0 : gridMore.length;
    }

    @Override
    public String getItem(int position) {
        return this.gridMore[position];
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
            convertView = layoutInflater.inflate(R.layout.adapter_grid_more, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_name.setText(getItem(position));
        viewHolder.iv_header.setImageResource(images[position]);
        return convertView;
    }

    class ViewHolder {
        ImageView iv_header;
        TextView tv_name;

        public ViewHolder(View view) {
            this.iv_header = (ImageView) view.findViewById(R.id.iv_header);
            this.tv_name = (TextView) view.findViewById(R.id.tv_name);
        }
    }
}
