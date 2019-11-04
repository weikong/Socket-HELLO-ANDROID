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
import android.widget.AdapterView;
import android.widget.ListView;

import com.king.chat.socket.R;
import com.king.chat.socket.config.Config;
import com.king.chat.socket.ui.DBFlow.chatRecord.DBChatRecordImpl;
import com.king.chat.socket.ui.DBFlow.session.DBSessionImpl;
import com.king.chat.socket.ui.DBFlow.session.SessionData;
import com.king.chat.socket.ui.activity.chat.MainChatActivity;
import com.king.chat.socket.ui.activity.contact.ContactSelectActivity;
import com.king.chat.socket.ui.adapter.MessageAdapter;
import com.king.chat.socket.ui.fragment.base.BaseFragment;
import com.king.chat.socket.ui.view.actionbar.CommonActionBar;
import com.king.chat.socket.util.BroadCastUtil;
import com.king.chat.socket.util.UserInfoManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    @BindView(R.id.action_bar)
    CommonActionBar actionBar;
    @BindView(R.id.listview)
    ListView listview;

    MessageAdapter adapter;

    private Handler handler = new Handler();


    public MessageFragment() {
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
    public static MessageFragment newInstance(String param1, String param2) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static MessageFragment newInstance() {
        MessageFragment fragment = new MessageFragment();
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
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, view);
        regitserReceiver();
        initActionBar();
        initView(view);
        isOncreate = true;
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isOncreate) {
            if (adapter != null && adapter.getCount() == 0)
                loadData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisibleToUser && isOncreate) {
            if (adapter != null && adapter.getCount() == 0)
                loadData();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unRegisterReceiver();
    }

    private void initActionBar() {
        actionBar.setFillStatusBar(true);
        actionBar.setTitle("hi");
        actionBar.setIvRightSrc(R.drawable.ic_time_add_press, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent2Activity(ContactSelectActivity.class);
            }
        });
    }

    private void initView(View view) {
        adapter = new MessageAdapter(getActivity());
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SessionData sessionData = adapter.getItem(position);
                Config.toUserId = sessionData.getMessagefromid();
                Config.toUserName = sessionData.getMessagefromname();
//                ContactBean contactBean = new ContactBean();
//                contactBean.setAccount(sessionData.getMessagefromid());
//                contactBean.setHeadPortrait(sessionData.getMessagefromavatar());
//                contactBean.setName(sessionData.getMessagefromname());
                intent2Activity(MainChatActivity.class, sessionData);
            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    SessionData sessionData = adapter.getItem(position);
                    int group = sessionData.getGroupdata();
                    if (group == 1) {
                        DBChatRecordImpl.getInstance().deleteGroupChatMessage(sessionData.getMessagefromid());
                    } else {
                        DBChatRecordImpl.getInstance().deleteSingleChatMessage(UserInfoManager.getInstance().getAccount(), sessionData.getMessagefromid());
                    }
                    DBSessionImpl.getInstance().deleteSession(sessionData);
                    adapter.removeData(sessionData);
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
    }

    private void loadData() {
        List<SessionData> list = DBSessionImpl.getInstance().querySession();
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }


    private BroadcastReceiver receiver;

    /**
     * 注册广播
     */
    private void regitserReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadCastUtil.ACTION_RECIEVE_MESSAGE);
//        intentFilter.addAction(BroadCastUtil.ACTION_UPDATE_MESSAGE);
        intentFilter.addAction(BroadCastUtil.ACTION_UPDATE_SESSION);
        intentFilter.addAction(BroadCastUtil.ACTION_GROUP_UPDATE);
        if (receiver == null) {
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    loadData();
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
