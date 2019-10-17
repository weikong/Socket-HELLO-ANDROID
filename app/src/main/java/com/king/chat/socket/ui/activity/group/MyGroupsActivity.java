package com.king.chat.socket.ui.activity.group;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.king.chat.socket.R;
import com.king.chat.socket.bean.ContactBean;
import com.king.chat.socket.bean.GroupInfo;
import com.king.chat.socket.bean.base.BaseTaskBean;
import com.king.chat.socket.config.Config;
import com.king.chat.socket.config.UrlConfig;
import com.king.chat.socket.ui.DBFlow.chatRecord.DBChatRecordImpl;
import com.king.chat.socket.ui.DBFlow.session.SessionData;
import com.king.chat.socket.ui.activity.base.BaseDataActivity;
import com.king.chat.socket.ui.activity.chat.MainChatActivity;
import com.king.chat.socket.ui.adapter.MyGroupsAdapter;
import com.king.chat.socket.ui.view.actionbar.CommonActionBar;
import com.king.chat.socket.ui.view.chat.group.InfoItemView;
import com.king.chat.socket.ui.view.chat.group.MembersGridView;
import com.king.chat.socket.ui.view.chat.group.RedTextItemView;
import com.king.chat.socket.util.BroadCastUtil;
import com.king.chat.socket.util.ContactManager;
import com.king.chat.socket.util.DisplayUtil;
import com.king.chat.socket.util.ToastUtil;
import com.king.chat.socket.util.UserInfoManager;
import com.king.chat.socket.util.httpUtil.HttpTaskUtil;
import com.king.chat.socket.util.httpUtil.OkHttpClientManager;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyGroupsActivity extends BaseDataActivity {

    @BindView(R.id.action_bar)
    CommonActionBar actionBar;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.list_view)
    ListView list_view;

    MyGroupsAdapter adapter;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);
        regitserReceiver();
        ButterKnife.bind(this);
        initActionBar();
        initView();
        swipeRefreshLayout.setRefreshing(true);
        loadDataTask();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterReceiver();
    }

    private void initActionBar() {
        actionBar.setTitle("群聊");
        actionBar.setIvBackVisiable(View.VISIBLE);
    }

    private void initView() {
        adapter = new MyGroupsAdapter(this);
        list_view.setAdapter(adapter);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ContactBean contactBean = adapter.getItem(position);
                GroupInfo groupInfo = (GroupInfo) adapter.getItem(position);
                Config.toUserId = groupInfo.getGroupaccount();
                Config.toUserName = groupInfo.getGroupname();
                SessionData sessionData = new SessionData();
                sessionData.setMessagefromid(groupInfo.getGroupaccount());
                sessionData.setMessagefromname(groupInfo.getGroupname());
                sessionData.setMessagefromavatar(groupInfo.getGroupheader());
                sessionData.setGroupdata(1);
                intent2Activity(MainChatActivity.class, sessionData);
            }
        });
        initSwipeRefreshLayout();
    }

    /**
     * 系统下拉刷新
     */
    private void initSwipeRefreshLayout() {
//        swipeRefreshLayout.setProgressViewEndTarget(false, DisplayUtil.dp2px(130));
        // 设置颜色属性的时候一定要注意是引用了资源文件还是直接设置16进制的颜色，因为都是int值容易搞混
        // 设置下拉进度的背景颜色，默认就是白色的
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_purple, android.R.color.holo_green_light, R.color.colorAccent, android.R.color.holo_blue_light);
        // 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadDataTask();
                    }
                }, 200);
            }
        });
        swipeRefreshLayout.setEnabled(true);
    }

    private void loadDataTask() {
        Map<String, String> params = new HashMap<>();
        params.put("ownerid", "" + UserInfoManager.getInstance().getAccountId());
        HttpTaskUtil.getInstance().postTask(UrlConfig.HTTP_GROUP_QUERY_MY_GROUPS, params, new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onResponse(String response) {
                try {
                    BaseTaskBean baseTaskBean = JSONObject.parseObject(response, BaseTaskBean.class);
                    if (baseTaskBean.getCode() == 1) {
                        List<GroupInfo> list = JSONArray.parseArray(baseTaskBean.getData(), GroupInfo.class);
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }

            }
        });
    }

    private BroadcastReceiver receiver;

    /**
     * 注册广播
     */
    private void regitserReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadCastUtil.ACTION_GROUP_UPDATE);
        if (receiver == null) {
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    switch (action) {
                        case BroadCastUtil.ACTION_GROUP_UPDATE:
                            GroupInfo gi = (GroupInfo) intent.getSerializableExtra("DATA");
                            if (gi != null) {
                                loadDataTask();
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
