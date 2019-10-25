package com.king.chat.socket.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.king.chat.socket.GlideApp;
import com.king.chat.socket.R;
import com.king.chat.socket.bean.ContactBean;
import com.king.chat.socket.bean.Expression;
import com.king.chat.socket.ui.view.ImageView.RoundAngleImageView;
import com.king.chat.socket.ui.view.chat.BiaoQingView;
import com.king.chat.socket.util.GlideOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinzhendi-031 on 2018/4/13.
 */

public class RecyclerGifAdapter extends RecyclerView.Adapter<RecyclerGifAdapter.ViewHolder> {

    private Context context;

    private List<String> datas = new ArrayList<>();

//    private int[] srcs = {R.drawable.gif_gouzi,R.drawable.gif_maomi};


    public RecyclerGifAdapter(Context context) {
        this.context = context;
    }

    public void setDatas(List<String> list) {
        this.datas.clear();
        if (list == null)
            return;
        this.datas.addAll(list);
    }

    @Override
    public RecyclerGifAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_recycler_gif, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerGifAdapter.ViewHolder holder, final int position) {
//        GlideApp.with(context).applyDefaultRequestOptions(GlideOptions.optionsTransparent()).asGif().load(srcs[position]).into(holder.itemImage);
        GlideApp.with(context).applyDefaultRequestOptions(GlideOptions.optionsTransparent()).asGif().load(datas.get(position)).into(holder.itemImage);
        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null){
                    callBack.clickGif(datas.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;

        public ViewHolder(View view) {
            super(view);
            itemImage = (ImageView) view.findViewById(R.id.iv_gif);
        }
    }

    public CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack{
        public void clickGif(String url);
    }
}
