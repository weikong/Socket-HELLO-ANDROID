package com.king.chat.socket.ui.activity.edit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONObject;
import com.king.chat.socket.GlideApp;
import com.king.chat.socket.R;
import com.king.chat.socket.bean.ContactBean;
import com.king.chat.socket.bean.base.BaseTaskBean;
import com.king.chat.socket.config.UrlConfig;
import com.king.chat.socket.ui.activity.base.BaseDataActivity;
import com.king.chat.socket.ui.view.ImageView.RoundAngleImageView;
import com.king.chat.socket.ui.view.actionbar.CommonActionBar;
import com.king.chat.socket.ui.view.setting.ForwardItemView;
import com.king.chat.socket.util.BroadCastUtil;
import com.king.chat.socket.util.GlideOptions;
import com.king.chat.socket.util.ToastUtil;
import com.king.chat.socket.util.UserInfoManager;
import com.king.chat.socket.util.httpUtil.HttpTaskUtil;
import com.king.chat.socket.util.httpUtil.OkHttpClientManager;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MineEditActivity extends BaseDataActivity {

    @BindView(R.id.action_bar)
    CommonActionBar actionBar;
    @BindView(R.id.rl_userPhoto)
    RelativeLayout rl_userPhoto;
    @BindView(R.id.img_view_header)
    RoundAngleImageView img_view_header;
    @BindView(R.id.item_view_name)
    ForwardItemView item_view_name;
    @BindView(R.id.item_view_sex)
    ForwardItemView item_view_sex;
    @BindView(R.id.item_view_like)
    ForwardItemView item_view_like;

    ContactBean contactBean;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_edit);
        ButterKnife.bind(this);
        contactBean = UserInfoManager.getInstance().getContactBean();
        initActionBar();
        initView();
    }

    private void initActionBar() {
        actionBar.setTitle("");
        actionBar.setIvBackVisiable(View.VISIBLE);
        actionBar.setRightToolVisiable(View.VISIBLE, "完成", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMineInfoTask();
            }
        });
    }

    private void initView() {
        if (contactBean != null){
            GlideApp.with(this).applyDefaultRequestOptions(GlideOptions.optionDefaultHeader4()).load(contactBean.getHeadPortrait()).into(img_view_header);
            item_view_name.setContent_text(contactBean.getName());
        }
        item_view_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineEditActivity.this, InputEditActivity.class);
                intent.putExtra("DATA",item_view_name.getContent_text());
                startActivityForResult(intent, 1003);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == 1003){
                String name = data.getStringExtra("DATA");
                item_view_name.setContent_text(name);
            }
        }
    }

    private void updateMineInfoTask() {
        Map<String, String> params = new HashMap<>();
        params.put("account", UserInfoManager.getInstance().getAccount());
        params.put("name", item_view_name.getContent_text());
        params.put("header", "");
        showProgreessDialog();
        HttpTaskUtil.getInstance().postTask(UrlConfig.HTTP_UPDATE_PERSON_INFO, params, new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                dismissProgressDialog();
            }

            @Override
            public void onResponse(String response) {
                boolean isFlag = false;
                try {
                    BaseTaskBean baseTaskBean = JSONObject.parseObject(response, BaseTaskBean.class);
                    if (baseTaskBean.getCode() == 1) {
                        isFlag = true;
                        ContactBean contactBean = JSONObject.parseObject(baseTaskBean.getData(),ContactBean.class);
                        if (contactBean != null && contactBean.getId() > 0){
                            UserInfoManager.getInstance().setContactBean(contactBean);
                        }
                        BroadCastUtil.sendActionBroadCast(MineEditActivity.this,BroadCastUtil.ACTION_UPDATE_USER_INFO);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    final boolean finalIsFlag = isFlag;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            dismissProgressDialog();
                            if (finalIsFlag){
                                ToastUtil.show("个人信息更新成功");
                            } else {
                                ToastUtil.show("个人信息更新失败");
                            }
                        }
                    });
                }

            }
        });
    }

}
