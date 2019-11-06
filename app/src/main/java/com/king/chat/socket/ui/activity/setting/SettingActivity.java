package com.king.chat.socket.ui.activity.setting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.king.chat.socket.R;
import com.king.chat.socket.bean.MineBean;
import com.king.chat.socket.config.UrlConfig;
import com.king.chat.socket.ui.activity.base.BaseDataActivity;
import com.king.chat.socket.ui.adapter.SettingAdapter;
import com.king.chat.socket.ui.view.actionbar.CommonActionBar;
import com.king.chat.socket.ui.view.setting.SettingExitView;
import com.king.chat.socket.ui.view.setting.SettingHeaderView;
import com.king.chat.socket.util.BroadCastUtil;
import com.king.chat.socket.util.UserInfoManager;
import com.king.chat.socket.util.httpUtil.OkHttpClientManager;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends BaseDataActivity {

    @BindView(R.id.actionBar)
    CommonActionBar actionBar;
    @BindView(R.id.list_view)
    ListView list_view;
    SettingHeaderView settingHeaderView;
    SettingExitView settingExitView;

    SettingAdapter adapter;
    private boolean isLoad = false;

    Handler handler = new Handler();

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_setting);
        ButterKnife.bind(this);
        regitserReceiver();
        initActionBar();
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterReceiver();
    }

    private void initActionBar() {
        actionBar.setTitle("设置");
        actionBar.setIvBackVisiable(View.VISIBLE);
    }

    private void initView() {
        settingHeaderView = new SettingHeaderView(this);
        settingExitView = new SettingExitView(this);
        list_view.addHeaderView(settingHeaderView);
        list_view.addFooterView(settingExitView);
        adapter = new SettingAdapter(this);
        list_view.setAdapter(adapter);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 1:
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
        settingExitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutTask();
            }
        });
    }

    private void initData() {
        String[] arrayTitle = getResources().getStringArray(R.array.array_tab_mine_title);
        TypedArray imageTypedArray = getResources().obtainTypedArray(R.array.array_tab_mine_icon);
        List<MineBean> list = new ArrayList<>();
        for (int i = 0; i < arrayTitle.length; i++) {
            MineBean bean = new MineBean();
            bean.setTitle(arrayTitle[i]);
            bean.setDrawable(imageTypedArray.getResourceId(i, R.drawable.icon_tab_mine_help));
            list.add(bean);
        }
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }

    /**
     * 退出登录接口
     * */
    private void logoutTask(){
        showProgreessDialog();
        try {
            OkHttpClientManager.Param paramAccount = new OkHttpClientManager.Param("account", UserInfoManager.getInstance().getAccount());
            OkHttpClientManager.getInstance()._postAsyn(UrlConfig.HTTP_LOGOUT, new OkHttpClientManager.StringCallback() {
                @Override
                public void onFailure(Request request, IOException e) {
                }

                @Override
                public void onResponse(String response) {

                }
            },paramAccount);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dismissProgressDialog();
        }
    }


    private BroadcastReceiver receiver;

    /**
     * 注册广播
     */
    private void regitserReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadCastUtil.ACTION_UPDATE_USER_INFO);
        if (receiver == null) {
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    settingHeaderView.setData();
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
