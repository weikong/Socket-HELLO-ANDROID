package com.king.chat.socket.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.chat.socket.R;
import com.king.chat.socket.ui.DBFlow.chatRecord.ChatRecordData;
import com.king.chat.socket.ui.DBFlow.session.DBSessionImpl;
import com.king.chat.socket.ui.activity.base.BaseDataActivity;
import com.king.chat.socket.ui.fragment.ContactFragment;
import com.king.chat.socket.ui.fragment.DiscoveryFragment;
import com.king.chat.socket.ui.fragment.MessageFragment;
import com.king.chat.socket.ui.fragment.MineFragment;
import com.king.chat.socket.ui.view.viewpager.CustomViewPager;
import com.king.chat.socket.util.BroadCastUtil;
import com.king.chat.socket.util.socket.SocketUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseDataActivity implements View.OnClickListener {

    @BindView(R.id.viewpager)
    CustomViewPager viewPager;

    @BindView(R.id.layout_1)
    RelativeLayout layout_1;
    @BindView(R.id.iv_tab_1)
    ImageView iv_tab_1;

    @BindView(R.id.layout_2)
    RelativeLayout layout_2;
    @BindView(R.id.iv_tab_2)
    ImageView iv_tab_2;

    @BindView(R.id.layout_3)
    RelativeLayout layout_3;
    @BindView(R.id.iv_tab_3)
    ImageView iv_tab_3;

    @BindView(R.id.layout_4)
    RelativeLayout layout_4;
    @BindView(R.id.iv_tab_4)
    ImageView iv_tab_4;

    @BindView(R.id.tv_unread_all)
    TextView tv_unread_all;

    private MessageFragment messageFragment;
    private ContactFragment contactFragment;
    private DiscoveryFragment discoveryFragment;
    private MineFragment mineFragment;

    private int indexTab = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        regitserReceiver();
        ButterKnife.bind(this);//注意这里必须设置绑定当前Activity
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            long count = DBSessionImpl.getInstance().querySessionAllUnread2();
            if (count > 0){
                tv_unread_all.setVisibility(View.VISIBLE);
            } else {
                tv_unread_all.setVisibility(View.INVISIBLE);
            }
            if (count > 99) {
                count = 99;
            }
            tv_unread_all.setText(""+count);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        SocketUtil.getInstance().disconnect();
        unRegisterReceiver();
        super.onDestroy();
    }

    private void initView() {
        viewPager.setScanScroll(false);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(pageChangeListener);
        layout_1.setOnClickListener(this);
        layout_2.setOnClickListener(this);
        layout_3.setOnClickListener(this);
        layout_4.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        setLineInvisiable();
        switch (view.getId()) {
            case R.id.layout_1:
                indexTab = 0;
                viewPager.setCurrentItem(0, false);
                iv_tab_1.setImageResource(R.drawable.tab_icon_message_hover);
                break;
            case R.id.layout_2:
                indexTab = 1;
                viewPager.setCurrentItem(1, false);
                iv_tab_2.setImageResource(R.drawable.tab_icon_contact_hover);
                break;
            case R.id.layout_3:
                indexTab = 2;
                viewPager.setCurrentItem(2, false);
//                iv_tab_3.setImageResource(R.drawable.tab_icon_home_hover);
                iv_tab_3.setColorFilter(getResources().getColor(R.color.color_main_tab_press));
                break;
            case R.id.layout_4:
                indexTab = 3;
                viewPager.setCurrentItem(3, false);
                iv_tab_4.setImageResource(R.drawable.tab_icon_mine_hover);
                break;
        }
    }

    private void setLineInvisiable() {
        iv_tab_1.setImageResource(R.drawable.tab_icon_message);
        iv_tab_2.setImageResource(R.drawable.tab_icon_contact);
//        iv_tab_3.setImageResource(R.drawable.tab_icon_home);
        iv_tab_3.setColorFilter(getResources().getColor(R.color.color_main_tab_nomal));
        iv_tab_4.setImageResource(R.drawable.tab_icon_mine);
    }

    /**
     * 初始化FragmentPagerAdapter
     */
    private FragmentPagerAdapter adapter = new FragmentPagerAdapter(
            getSupportFragmentManager()) {

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (messageFragment == null) {
                        messageFragment = new MessageFragment();
                    }
                    return messageFragment;
                case 1:
                    if (contactFragment == null) {
                        contactFragment = new ContactFragment();
                    }
                    return contactFragment;
                case 2:
                    if (discoveryFragment == null) {
                        discoveryFragment = new DiscoveryFragment();
                    }
                    return discoveryFragment;
                case 3:
                    if (mineFragment == null) {
                        mineFragment = new MineFragment();
                    }
                    return mineFragment;
                default:
                    if (messageFragment == null) {
                        messageFragment = new MessageFragment();
                    }
                    return messageFragment;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private BroadcastReceiver receiver;

    /**
     * 注册广播
     */
    private void regitserReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadCastUtil.ACTION_CONNECTED);
        intentFilter.addAction(BroadCastUtil.ACTION_CONNECTING);
        intentFilter.addAction(BroadCastUtil.ACTION_DISCONNECT);
        if (receiver == null) {
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    switch (action){
                        case BroadCastUtil.ACTION_CONNECTED:
                            if (!SocketUtil.getInstance().isConnect()){
                                SocketUtil.getInstance().connect();
                            }
                            break;
                        case BroadCastUtil.ACTION_CONNECTING:
                            break;
                        case BroadCastUtil.ACTION_DISCONNECT:
                            if (SocketUtil.getInstance().isConnect()){
                                SocketUtil.getInstance().closeSocket();
                            }
                            break;
                    }
                }
            };
        }
        registerReceiver(receiver, intentFilter);
    }

    /**
     * 注销广播监听
     */
    private void unRegisterReceiver() {
        if (null != receiver) {
            unregisterReceiver(receiver);
        }
    }
}
