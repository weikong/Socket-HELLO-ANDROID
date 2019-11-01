package com.king.chat.socket.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.chat.socket.GlideApp;
import com.king.chat.socket.R;
import com.king.chat.socket.bean.FaceSourceBean;
import com.king.chat.socket.util.GlideOptions;
import com.king.chat.socket.util.SDCardUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maesinfo on 2019/9/19.
 */

public class FaceSourceDownAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater layoutInflater;
    private List<FaceSourceBean> list = new ArrayList<>();

    public FaceSourceDownAdapter(Context mContext) {
        this.mContext = mContext;
        this.layoutInflater = LayoutInflater.from(mContext);
    }

    public List<FaceSourceBean> getList() {
        return list;
    }

    public void setList(List<FaceSourceBean> list) {
        this.list.clear();
        if (list != null) {
            this.list = list;
        }
    }

    public void addList(int position, List<FaceSourceBean> list) {
        if (list != null && list.size() > 0) {
            this.list.addAll(position, list);
        }
    }

    public void addList(List<FaceSourceBean> list) {
        if (list != null && list.size() > 0) {
            this.list.addAll(list);
        }
    }

    public void addData(FaceSourceBean bean) {
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
    public FaceSourceBean getItem(int position) {
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
            convertView = layoutInflater.inflate(R.layout.adapter_face_source_down, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final FaceSourceBean bean = getItem(position);
        String thumb = bean.getThumb();
        if (!TextUtils.isEmpty(thumb) && thumb.toLowerCase().endsWith(".gif")){
            GlideApp.with(mContext).applyDefaultRequestOptions(GlideOptions.optionsTransparent()).asGif().load(thumb).into(viewHolder.iv_header);
        } else {
            GlideApp.with(mContext).applyDefaultRequestOptions(GlideOptions.optionsTransparent()).load(thumb).into(viewHolder.iv_header);
        }
        String name = bean.getName();
        viewHolder.tv_name.setText(name);
        viewHolder.tv_down.setText("下载");
        String GIFDir = SDCardUtil.getDiskCacheDir(mContext,"gif");
        File GifDirFile = new File(GIFDir);
        if (GifDirFile.exists()){
            File[] childFiles = GifDirFile.listFiles();
            if (childFiles.length > 0) {
                for (File child : childFiles){
                    if (child.getName().equalsIgnoreCase(name)){
                        viewHolder.tv_down.setText("已下载");
                        break;
                    }
                }
            }
        }
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.tv_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null && finalViewHolder.tv_down.getText().toString().equals("下载")){
                    callBack.downLoadGifZip(bean.getZip());
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView iv_header;
        TextView tv_name,tv_down;

        public ViewHolder(View view) {
            this.iv_header = (ImageView) view.findViewById(R.id.iv_header);
            this.tv_name = (TextView) view.findViewById(R.id.tv_name);
            this.tv_down = (TextView) view.findViewById(R.id.tv_down);
        }
    }

    public CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack{
        public void downLoadGifZip(String url);
    }
}
