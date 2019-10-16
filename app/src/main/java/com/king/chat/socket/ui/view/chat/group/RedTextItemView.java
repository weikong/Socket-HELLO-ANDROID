package com.king.chat.socket.ui.view.chat.group;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.chat.socket.R;
import com.king.chat.socket.bean.GroupInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by maesinfo on 2019/5/15.
 */

public class RedTextItemView extends RelativeLayout {

    @BindView(R.id.tv_title)
    TextView tv_title;

    public RedTextItemView(Context context) {
        super(context);
        initView(context);
    }

    public RedTextItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RedTextItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_red_text_item, this);
        ButterKnife.bind(this, view);
    }

    public void loadData(String title) {
        tv_title.setText(title);
    }
}
