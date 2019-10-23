package com.king.chat.socket.ui.view.mine;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.king.chat.socket.GlideApp;
import com.king.chat.socket.R;
import com.king.chat.socket.util.DisplayUtil;
import com.king.chat.socket.util.GlideOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by maesinfo on 2019/5/15.
 */

public class MineHeaderActionBar extends LinearLayout {

    @BindView(R.id.layout_content)
    RelativeLayout layout_content;
    @BindView(R.id.view_top_status)
    View viewStatus;

    @BindView(R.id.iv_mine)
    ImageView iv_mine;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_account)
    TextView tv_account;
    @BindView(R.id.tv_huozan)
    TextView tv_huozan;
    @BindView(R.id.tv_guanzhu)
    TextView tv_guanzhu;
    @BindView(R.id.tv_fensi)
    TextView tv_fensi;

    private int statusHeight = 0;
    private boolean fillStatusBar = false;

    public MineHeaderActionBar(Context context) {
        super(context);
        initView(context);
    }

    public MineHeaderActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MineHeaderActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public MineHeaderActionBar(Context context, boolean fillStatusBar) {
        super(context);
        this.fillStatusBar = fillStatusBar;
        initView(context);
    }


    private void initView(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.actionbar_mine_header,this);
        ButterKnife.bind(this,view);
        setStatusHeight(statusHeight);
    }

    private void setStatusHeight(int height) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT || !fillStatusBar)
            return;
        if (statusHeight == 0)
            statusHeight = DisplayUtil.getStatusBarHeight(getContext());
        LayoutParams params = (LayoutParams) viewStatus.getLayoutParams();
        params.height = statusHeight;
        viewStatus.setLayoutParams(params);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewGroup.LayoutParams lp = getLayoutParams();
        if (lp != null) {
            int actionHeight = (int) getContext().getResources().getDimension(R.dimen.margin_300dp);
            lp.height = actionHeight + statusHeight;
            lp.width = LayoutParams.MATCH_PARENT;
        }
    }

    public void setData(){
    }
}
