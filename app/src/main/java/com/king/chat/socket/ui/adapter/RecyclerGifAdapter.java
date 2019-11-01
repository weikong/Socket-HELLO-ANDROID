package com.king.chat.socket.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.king.chat.socket.GlideApp;
import com.king.chat.socket.R;
import com.king.chat.socket.config.UrlConfig;
import com.king.chat.socket.util.DisplayUtil;
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
    private List<File> files = new ArrayList<>();

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

    public void setFileDatas(List<File> list) {
        this.files.clear();
        if (list == null)
            return;
        this.files.addAll(list);
    }

    @Override
    public RecyclerGifAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_recycler_gif, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerGifAdapter.ViewHolder holder, final int position) {
        final File file = files.get(position);
        if (file != null && file.getName().toLowerCase().endsWith(".gif")){
            GlideApp.with(context).applyDefaultRequestOptions(GlideOptions.optionsTransparent()).asGif().load(file).into(holder.itemImage);
        } else {
            GlideApp.with(context).applyDefaultRequestOptions(GlideOptions.optionsTransparent()).load(file).into(holder.itemImage);
        }
        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null){
                    String name = file.getName();
                    String parent = file.getParentFile().getName();
                    String url = UrlConfig.HTTP_ROOT + "gif/res"+File.separator+parent+File.separator+name;
                    callBack.clickGif(url);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;

        public ViewHolder(View view) {
            super(view);
            itemImage = (ImageView) view.findViewById(R.id.iv_gif);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) itemImage.getLayoutParams();
            params.height = (DisplayUtil.screenWidth - 6 * DisplayUtil.dp2px(4)) / 5;
            params.width = params.height;
            itemImage.setLayoutParams(params);
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
