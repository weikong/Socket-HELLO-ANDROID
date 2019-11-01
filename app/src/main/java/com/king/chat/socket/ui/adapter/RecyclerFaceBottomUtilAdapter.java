package com.king.chat.socket.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.king.chat.socket.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinzhendi-031 on 2018/4/13.
 */

public class RecyclerFaceBottomUtilAdapter extends RecyclerView.Adapter<RecyclerFaceBottomUtilAdapter.ViewHolder> {

    private Context context;

    private List<String> files = new ArrayList<>();

    private int tabIndex = 0;


    public RecyclerFaceBottomUtilAdapter(Context context) {
        this.context = context;
    }

    public void setFileDatas(List<String> list) {
        this.files.clear();
        if (list == null)
            return;
        this.files.addAll(list);
    }

    @Override
    public RecyclerFaceBottomUtilAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_recycler_face_bottom_util, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerFaceBottomUtilAdapter.ViewHolder holder, final int position) {
        final String name = files.get(position);
        holder.tv_face.setText(name);
        if (tabIndex == position){
            holder.tv_face.setPressed(true);
        } else {
            holder.tv_face.setPressed(false);
        }
        holder.tv_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabIndex = position;
                if (callBack != null){
                    callBack.clickGif(position,name);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_face;

        public ViewHolder(View view) {
            super(view);
            tv_face = (TextView) view.findViewById(R.id.tv_face);
        }
    }

    public CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack{
        public void clickGif(int index, String name);
    }
}
