package com.king.chat.socket.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.king.chat.socket.R;
import com.king.chat.socket.ui.activity.setting.SettingActivity;
import com.king.chat.socket.ui.adapter.MineAdapter;
import com.king.chat.socket.ui.fragment.base.BaseFragment;
import com.king.chat.socket.ui.view.actionbar.CommonActionBar;
import com.king.chat.socket.ui.view.mine.MineHeaderActionBar;
import com.king.chat.socket.util.BroadCastUtil;
import com.king.chat.socket.util.DisplayUtil;
import com.king.chat.socket.util.Logger;
import com.king.chat.socket.util.UserInfoManager;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    @BindView(R.id.action_bar)
    CommonActionBar actionBar;
    @BindView(R.id.iv_setting)
    ImageView iv_setting;
    @BindView(R.id.listview)
    ListView listview;

    MineAdapter adapter;
    MineHeaderActionBar mineHeaderActionBar;
    int headerImageHeight = 300;

    private Handler handler = new Handler();


    public MineFragment() {
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
    public static MineFragment newInstance(String param1, String param2) {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
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
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, view);
        regitserReceiver();
        headerImageHeight = DisplayUtil.dp2px(300) - DisplayUtil.dp2px(40) - DisplayUtil.getStatusBarHeight(getActivity());
        initActionBar();
        initView(view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unRegisterReceiver();
    }

    private void initActionBar() {
        actionBar.setBackgroundAlpha(true);
        actionBar.setFillStatusBar(true);
        actionBar.setTitle(UserInfoManager.getInstance().getContactBean().getName());
        actionBar.setTitleAlpha(0);
    }

    private void initView(View view) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_setting.getLayoutParams();
        params.topMargin = DisplayUtil.getStatusBarHeight(getActivity());
        iv_setting.setLayoutParams(params);
        iv_setting.setOnClickListener(this);
        mineHeaderActionBar = new MineHeaderActionBar(getActivity());
        listview.addHeaderView(mineHeaderActionBar);
        adapter = new MineAdapter(getActivity());
        listview.setAdapter(adapter);
        String[] array = getResources().getStringArray(R.array.array_grid_more);
        adapter.setList(Arrays.asList(array));
        adapter.addList(Arrays.asList(array));
        adapter.addList(Arrays.asList(array));
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Logger.e(TAG,"onScrollStateChanged scrollState = "+scrollState);

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                View topView  = listview.getChildAt(0);
                if (topView != null){
                    float alpha = 1.0f;
                    if (firstVisibleItem == 0){
                        int top = listview.getChildAt(0).getTop();
                        alpha = Math.abs(top * 1.0f/headerImageHeight);
                        actionBar.setBackgroundAlpha(alpha);
                    } else {
                        actionBar.setBackgroundAlpha(alpha);
                    }
                    actionBar.setTitleAlpha(alpha);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_setting:
                intent2Activity(SettingActivity.class);
                break;
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
                    mineHeaderActionBar.setData();
                }
            };
        }
        getActivity().registerReceiver(receiver, intentFilter);
    }

    /**
     * 注销广播监听
     */
    private void unRegisterReceiver() {
        if (null != receiver) {
            getActivity().unregisterReceiver(receiver);
        }
    }
}
