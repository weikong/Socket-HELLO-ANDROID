package com.king.chat.socket.ui.view.chat.group;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.chat.socket.R;
import com.king.chat.socket.bean.ContactBean;
import com.king.chat.socket.bean.GroupInfo;
import com.king.chat.socket.ui.adapter.GridMembersAdapter;
import com.king.chat.socket.ui.view.gridview.CustomGridView;
import com.king.chat.socket.util.DisplayUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by maesinfo on 2019/5/15.
 */

public class InfoItemView extends RelativeLayout {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_content)
    TextView tv_content;

    @BindView(R.id.iv_arrow)
    ImageView iv_arrow;
    @BindView(R.id.iv_switch)
    ImageView iv_switch;

    private GroupInfo groupInfo;

    public InfoItemView(Context context) {
        super(context);
        initView(context);
    }

    public InfoItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public InfoItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_info_item, this);
        ButterKnife.bind(this, view);
    }

    public void loadData(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }

    public void loadData(String title, String content, boolean isSwitch) {
        tv_title.setText(title);
        tv_content.setText(content);
        if (isSwitch) {
            iv_switch.setVisibility(View.VISIBLE);
            iv_arrow.setVisibility(View.INVISIBLE);
            tv_content.setVisibility(View.INVISIBLE);
        } else {
            iv_switch.setVisibility(View.INVISIBLE);
            iv_arrow.setVisibility(View.VISIBLE);
            tv_content.setVisibility(View.VISIBLE);
        }
    }
}
