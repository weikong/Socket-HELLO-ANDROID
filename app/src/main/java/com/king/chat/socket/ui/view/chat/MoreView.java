package com.king.chat.socket.ui.view.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.king.chat.socket.R;
import com.king.chat.socket.ui.activity.base.BaseDataActivity;
import com.king.chat.socket.ui.activity.camera.CameraActivity;
import com.king.chat.socket.ui.adapter.GridMoreAdapter;
import com.king.chat.socket.ui.view.gridview.CustomGridView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by maesinfo on 2019/5/15.
 */

public class MoreView extends RelativeLayout {

    @BindView(R.id.gridview)
    CustomGridView gridView;
    GridMoreAdapter adapter;

    public MoreView(Context context) {
        super(context);
        initView(context);
    }

    public MoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_more, this);
        ButterKnife.bind(this,view);
        adapter = new GridMoreAdapter(context);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        break;
                    case 1:
                        chooseCamera();
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                }
            }
        });
    }

    /**
     * 跳转到拍照页面
     */
    protected void chooseCamera() {
        boolean onlyImage = false;
        Intent intent = new Intent(getContext(), CameraActivity.class);
        intent.putExtra(CameraActivity.ONLY_CAPTURE, onlyImage);
        ((Activity)getContext()).startActivityForResult(intent, BaseDataActivity.INTENT_REQUEST_PHOTO);
    }
}
