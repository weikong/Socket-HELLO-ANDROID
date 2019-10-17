package com.king.chat.socket.ui.activity.edit;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.king.chat.socket.R;
import com.king.chat.socket.bean.GroupInfo;
import com.king.chat.socket.bean.base.BaseTaskBean;
import com.king.chat.socket.config.UrlConfig;
import com.king.chat.socket.ui.DBFlow.session.DBSessionImpl;
import com.king.chat.socket.ui.DBFlow.session.SessionData;
import com.king.chat.socket.ui.activity.base.BaseDataActivity;
import com.king.chat.socket.ui.adapter.MyGroupsAdapter;
import com.king.chat.socket.ui.view.actionbar.CommonActionBar;
import com.king.chat.socket.util.BroadCastUtil;
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

public class SingleInputEditActivity extends BaseDataActivity {

    @BindView(R.id.action_bar)
    CommonActionBar actionBar;
    @BindView(R.id.et_input)
    EditText et_input;

    GroupInfo groupInfo;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_input_edit);
        ButterKnife.bind(this);
        groupInfo = (GroupInfo) getIntent().getSerializableExtra("DATA");
        initActionBar();
        initView();
    }

    private void initActionBar() {
        actionBar.setTitle("");
        actionBar.setIvBackVisiable(View.VISIBLE);
        actionBar.setRightToolVisiable(View.VISIBLE, "完成", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateGroupInfoTask();
            }
        });
    }

    private void initView() {
        et_input.setText(groupInfo.getGroupname());
        et_input.setSelection(et_input.getText().length());
    }

    private void updateGroupInfoTask() {
        String groupName = et_input.getText().toString();
        if (TextUtils.isEmpty(groupName) || TextUtils.isEmpty(groupName.trim())) {
            ToastUtil.show("请输入内容");
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("id", "" + groupInfo.getId());
        params.put("groupname", groupName);
        showProgreessDialog();
        HttpTaskUtil.getInstance().postTask(UrlConfig.HTTP_GROUP_UPDATE, params, new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                dismissProgressDialog();
            }

            @Override
            public void onResponse(String response) {
                try {
                    BaseTaskBean baseTaskBean = JSONObject.parseObject(response, BaseTaskBean.class);
                    if (baseTaskBean.getCode() == 1) {
                        GroupInfo groupInfo = JSONObject.parseObject(baseTaskBean.getData(), GroupInfo.class);
                        SessionData sessionData = DBSessionImpl.getInstance().querySessionDataByFromId(groupInfo.getGroupaccount());
                        sessionData.setMessagefromname(groupInfo.getGroupname());
                        sessionData.setMessagefromavatar(groupInfo.getGroupheader());
                        if (sessionData != null) {
                            DBSessionImpl.getInstance().updateSession(sessionData);
                        }
                        BroadCastUtil.sendActionBroadCast(SingleInputEditActivity.this, BroadCastUtil.ACTION_GROUP_UPDATE, groupInfo);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            dismissProgressDialog();
                        }
                    });
                }

            }
        });
    }

}
