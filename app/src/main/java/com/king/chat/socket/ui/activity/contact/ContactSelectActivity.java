package com.king.chat.socket.ui.activity.contact;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.king.chat.socket.GlideApp;
import com.king.chat.socket.R;
import com.king.chat.socket.bean.ContactBean;
import com.king.chat.socket.bean.GroupInfo;
import com.king.chat.socket.bean.UploadFileBean;
import com.king.chat.socket.bean.base.BaseTaskBean;
import com.king.chat.socket.config.Config;
import com.king.chat.socket.config.UrlConfig;
import com.king.chat.socket.ui.DBFlow.chatRecord.DBChatRecordImpl;
import com.king.chat.socket.ui.DBFlow.session.SessionData;
import com.king.chat.socket.ui.activity.chat.MainChatActivity;
import com.king.chat.socket.ui.activity.base.BaseDataActivity;
import com.king.chat.socket.ui.adapter.ContactSelectAdapter;
import com.king.chat.socket.ui.adapter.RecyclerContactSelctAdapter;
import com.king.chat.socket.ui.view.actionbar.CommonActionBar;
import com.king.chat.socket.util.BitmapUtil;
import com.king.chat.socket.util.CombineNineRect;
import com.king.chat.socket.util.ContactManager;
import com.king.chat.socket.util.FileUtil;
import com.king.chat.socket.util.FilterTimeOutManager;
import com.king.chat.socket.util.ToastUtil;
import com.king.chat.socket.util.UserInfoManager;
import com.king.chat.socket.util.httpUtil.HttpTaskUtil;
import com.king.chat.socket.util.httpUtil.OkHttpClientManager;
import com.king.chat.socket.util.socket.SocketUtil;
import com.squareup.okhttp.Request;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactSelectActivity extends BaseDataActivity {

    @BindView(R.id.action_bar)
    CommonActionBar actionBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.listview)
    ListView listview;

    ContactSelectAdapter adapter;
    RecyclerContactSelctAdapter selectAdapter;
    List<ContactBean> selectContacts = new ArrayList<>();
    String groupHeader = "";

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_select);
        ButterKnife.bind(this);//注意这里必须设置绑定当前Activity
        initActionBar();
        initView();
    }


    private void initActionBar() {
        actionBar.setTitle("通讯录");
        actionBar.setIvBackVisiable(View.VISIBLE);
        actionBar.setRightToolVisiable(View.VISIBLE, "完成", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hechengUploadImage();
            }
        });
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //创建LinearLayoutManager 对象 这里使用 <span style="font-family:'Source Code Pro';">LinearLayoutManager 是线性布局的意思</span>
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        //设置RecyclerView 布局
        recyclerView.setLayoutManager(layoutmanager);
        //设置为水平布局，这也是默认的
        layoutmanager.setOrientation(OrientationHelper.HORIZONTAL);
        //设置Adapter
        selectAdapter = new RecyclerContactSelctAdapter(this);
        recyclerView.setAdapter(selectAdapter);

        initSwipeRefreshLayout();
        adapter = new ContactSelectAdapter(this);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContactBean contactBean = adapter.getItem(position);
                boolean isCheck = contactBean.isCheck();
                if (isCheck){
                    selectContacts.remove(contactBean);
                } else {
                    selectContacts.add(contactBean);
                }
                contactBean.setCheck(!isCheck);
                adapter.notifyDataSetChanged();

                selectAdapter.setDatas(selectContacts);
                selectAdapter.notifyDataSetChanged();
            }
        });
//        if (ContactManager.getInstance().getContactList().size() > 0){
//            adapter.setList(ContactManager.getInstance().getContactList());
//            adapter.notifyDataSetChanged();
//            swipeRefreshLayout.setEnabled(false);
//        } else {
            swipeRefreshLayout.setRefreshing(true);
            loadContactTask();
//        }
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
                        ContactManager.getInstance().setContactList(list);
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setEnabled(false);
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

    private void groupCreateTask() {
        if (!UserInfoManager.getInstance().isLogin()){
            ToastUtil.show("请先登录");
            return;
        }
        if (selectContacts.size() == 0){
            ToastUtil.show("请选择群成员");
            return;
        }
//        showProgreessDialog();
        StringBuffer sb = new StringBuffer();
        for (ContactBean contactBean : selectContacts){
            sb.append(contactBean.getId()).append(",");
        }
        if (sb.length() > 0)
            sb.setLength(sb.length()-1);
        Map<String,String> params = new HashMap<>();
        params.put("ownerid", ""+UserInfoManager.getInstance().getAccountId());
        params.put("groupname", "群聊");
        params.put("groupheader", groupHeader);
        params.put("groupmembers", sb.toString());
        HttpTaskUtil.getInstance().postTask(UrlConfig.HTTP_GROUP_CREATE,params, new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                dismissProgressDialog();
            }

            @Override
            public void onResponse(String response) {
                try {
                    BaseTaskBean baseTaskBean = JSONObject.parseObject(response, BaseTaskBean.class);
                    if (baseTaskBean.getCode() == 1) {
                        GroupInfo groupInfo = JSONObject.parseObject(baseTaskBean.getData(),GroupInfo.class);
                        // TODO: 2019/10/10 进入群聊会话，加入session表
                        SessionData sessionData = new SessionData();
                        sessionData.setGroupdata(1);
                        sessionData.setMessagefromid(groupInfo.getGroupaccount());
                        sessionData.setMessagefromname(groupInfo.getGroupname());
                        sessionData.setMessagefromavatar(groupHeader);
                        Config.toUserId = sessionData.getMessagefromid();
                        Config.toUserName = sessionData.getMessagefromname();
                        Intent intent = new Intent(ContactSelectActivity.this, MainChatActivity.class);
                        intent.putExtra("DATA", sessionData);
                        startActivity(intent);
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

    private void uploadGroupImage(String path){
        if (!UserInfoManager.getInstance().isLogin()){
            ToastUtil.show("请先登录");
            return;
        }
        if (selectContacts.size() == 0){
            ToastUtil.show("请选择群成员");
            return;
        }
//        showProgreessDialog();

        HttpTaskUtil.getInstance().postUploadTask(UrlConfig.HTTP_UPLOAD_IMAGES, new File(path), "file", new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                dismissProgressDialog();
                ToastUtil.show("头像上传失败");
            }

            @Override
            public void onResponse(String response) {
//                        {"data":[{"fileName":"picture_1569497730337.jpg","suffix":".jpg","filePathMD5":"http://172.17.7.164:9090/fad279fc-732c-4261-8c42-684484dc2612.jpg","filePathMD5Thumb":"http://172.17.7.164:9090/fad279fc-732c-4261-8c42-684484dc2612.jpg"}],"code":1}
                try {
                    BaseTaskBean resultTaskBean = JSONObject.parseObject(response, BaseTaskBean.class);
                    if (resultTaskBean.getCode() == 1) {
                        List<UploadFileBean> list = JSONArray.parseArray(resultTaskBean.getData(), UploadFileBean.class);
                        if (list != null && list.size() > 0) {
                            UploadFileBean uploadFileBean = list.get(0);
                            groupHeader = uploadFileBean.getFilePathMD5();
                            groupCreateTask();
                            return;
                        }
                    }
                    dismissProgressDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                    dismissProgressDialog();
                    ToastUtil.show(e != null ? e.getMessage() : "头像上传数据解析异常");
                }
            }
        });
    }

    public void hechengUploadImage(){
        if (!UserInfoManager.getInstance().isLogin()){
            ToastUtil.show("请先登录");
            return;
        }
        if (selectContacts.size() == 0){
            ToastUtil.show("请选择群成员");
            return;
        }
        showProgreessDialog();
        List<ContactBean> list;
        if (selectContacts.size() > 9){
            list = selectContacts.subList(0,9);
        } else {
            list = selectContacts;
        }
        for (ContactBean contactBean : list){
            loadImageSimpleTarget(contactBean.getHeadPortrait(),selectContacts.size());
        }
    }

    List<Bitmap> bitmaps = new ArrayList<>();

    private void loadImageSimpleTarget(String url, final int count) {
        GlideApp.with(this).asBitmap().load(url).into(new SimpleTarget<Bitmap>(CombineNineRect.imgWidth,CombineNineRect.imgHeight) {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                if (resource != null){
                    bitmaps.add(resource);
                }
                if (bitmaps.size() == count){
                    Bitmap bitmap = BitmapUtil.getInstance().combimeBitmap(ContactSelectActivity.this, CombineNineRect.imgWidth,CombineNineRect.imgHeight,bitmaps);
                    try {
                        File file = FileUtil.getInstance().saveFile(bitmap,System.currentTimeMillis()+".jpg");
                        uploadGroupImage(file.getAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                        dismissProgressDialog();
                    }
                }
            }
        });
    }


}
