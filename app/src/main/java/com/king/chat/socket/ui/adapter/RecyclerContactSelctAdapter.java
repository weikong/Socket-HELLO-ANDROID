package com.king.chat.socket.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.king.chat.socket.GlideApp;
import com.king.chat.socket.R;
import com.king.chat.socket.bean.ContactBean;
import com.king.chat.socket.ui.view.ImageView.RoundAngleImageView;
import com.king.chat.socket.util.GlideOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinzhendi-031 on 2018/4/13.
 */

public class RecyclerContactSelctAdapter extends RecyclerView.Adapter<RecyclerContactSelctAdapter.ViewHolder> {

    private Context context;

    private List<ContactBean> datas = new ArrayList<>();

    public RecyclerContactSelctAdapter(Context context) {
        this.context = context;
    }

    public void setDatas(List<ContactBean> list) {
        this.datas.clear();
        if (list == null)
            return;
        this.datas.addAll(list);
    }

    @Override
    public RecyclerContactSelctAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_recycler_contact_select, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerContactSelctAdapter.ViewHolder holder, int position) {
        ContactBean bean = datas.get(position);
        GlideApp.with(context).applyDefaultRequestOptions(GlideOptions.optionDefaultHeader2()).load(bean.getHeadPortrait()).dontAnimate().into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RoundAngleImageView itemImage;

        public ViewHolder(View view) {
            super(view);
            itemImage = (RoundAngleImageView) view.findViewById(R.id.iv_header);
        }
    }
}
