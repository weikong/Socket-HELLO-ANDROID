package com.king.chat.socket.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSONObject;
import com.king.chat.socket.R;
import com.king.chat.socket.bean.ContactBean;
import com.king.chat.socket.bean.base.BaseTaskBean;
import com.king.chat.socket.config.Config;
import com.king.chat.socket.config.UrlConfig;
import com.king.chat.socket.ui.activity.MainChatActivity;
import com.king.chat.socket.ui.adapter.ContactAdapter;
import com.king.chat.socket.ui.fragment.base.BaseFragment;
import com.king.chat.socket.ui.view.actionbar.CommonActionBar;
import com.king.chat.socket.util.httpUtil.HttpTaskUtil;
import com.king.chat.socket.util.httpUtil.OkHttpClientManager;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ContactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    @BindView(R.id.action_bar)
    CommonActionBar actionBar;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.listview)
    ListView listview;

    ContactAdapter adapter;

    private Handler handler = new Handler();


    public ContactFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompaniesFragment.
     */
    public static ContactFragment newInstance(String param1, String param2) {
        ContactFragment fragment = new ContactFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static ContactFragment newInstance() {
        ContactFragment fragment = new ContactFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        ButterKnife.bind(this, view);
        initActionBar();
        initView(view);
        isOncreate = true;
        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isOncreate) {
            if (adapter != null && adapter.getCount() == 0){
                swipeRefreshLayout.setRefreshing(false);
                loadContactTask();
            }
        }
    }

    private void initActionBar() {
        actionBar.setTitle("通讯录");
    }

    private void initView(View view) {
        adapter = new ContactAdapter(getActivity());
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContactBean contactBean = adapter.getItem(position);
                Config.toUserId = contactBean.getAccount();
                Config.toUserName = contactBean.getName();
                intent2Activity(MainChatActivity.class, contactBean);
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
                        loadContactTask();
                    }
                }, 200);
            }
        });
        swipeRefreshLayout.setEnabled(true);
    }

    private void loadContactTask() {
        HttpTaskUtil.getInstance().getTask(UrlConfig.HTTP_ACCOUNT_LIST, new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onResponse(String response) {
                try {
                    BaseTaskBean baseTaskBean = JSONObject.parseObject(response, BaseTaskBean.class);
                    if (baseTaskBean.getCode() == 1) {
                        List<ContactBean> list = JSONObject.parseArray(baseTaskBean.getData(), ContactBean.class);
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
}
