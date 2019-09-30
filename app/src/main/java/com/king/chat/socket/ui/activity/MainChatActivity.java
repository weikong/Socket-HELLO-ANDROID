package com.king.chat.socket.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.king.chat.socket.R;
import com.king.chat.socket.bean.ContactBean;
import com.king.chat.socket.bean.FileItem;
import com.king.chat.socket.bean.UploadFileBean;
import com.king.chat.socket.bean.base.BaseTaskBean;
import com.king.chat.socket.config.UrlConfig;
import com.king.chat.socket.ui.DBFlow.chatRecord.ChatRecordData;
import com.king.chat.socket.ui.DBFlow.chatRecord.DBChatRecordImpl;
import com.king.chat.socket.ui.DBFlow.chatRecord.MessageChatType;
import com.king.chat.socket.ui.DBFlow.session.DBSessionImpl;
import com.king.chat.socket.ui.DBFlow.session.SessionData;
import com.king.chat.socket.ui.activity.base.BaseDataActivity;
import com.king.chat.socket.ui.adapter.MainChatAdapter;
import com.king.chat.socket.config.Config;
import com.king.chat.socket.ui.view.actionbar.CommonActionBar;
import com.king.chat.socket.ui.view.chat.BiaoQingView;
import com.king.chat.socket.ui.view.chat.MoreView;
import com.king.chat.socket.ui.view.chat.VoiceView;
import com.king.chat.socket.ui.view.gridview.CustomGridView;
import com.king.chat.socket.util.AppManager;
import com.king.chat.socket.util.BroadCastUtil;
import com.king.chat.socket.util.FilterTimeOutManager;
import com.king.chat.socket.util.ToastUtil;
import com.king.chat.socket.util.httpUtil.HttpTaskUtil;
import com.king.chat.socket.util.httpUtil.OkHttpClientManager;
import com.king.chat.socket.util.socket.SocketUtil;
import com.squareup.okhttp.Request;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainChatActivity extends BaseDataActivity {

    private CommonActionBar actionBar;
    private EditText mEditText;
    private TextView mTextView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private MainChatAdapter adapter;
    private ImageView iv_voice, iv_biaoqing;
    private RelativeLayout layout_expand;
    @BindView(R.id.viewVoice)
    VoiceView viewVoice;
    @BindView(R.id.viewBiaoQing)
    BiaoQingView viewBiaoQing;
    @BindView(R.id.viewMore)
    MoreView viewMore;
    private static final String TAG = "MyChatSccket";
    private boolean inputHasContent = false;
    private boolean isPullScroll = false;
    private float oldY = 0;
    private ContactBean contactBean;
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_main_chat);
        ButterKnife.bind(this);
        regitserReceiver();
        contactBean = (ContactBean) getIntent().getSerializableExtra("DATA");
        actionBar = (CommonActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle(contactBean.getName());
        actionBar.setIvBackVisiable(View.VISIBLE);
        if (SocketUtil.IM_CONNECT_STATE == SocketUtil.IM_CONNECTED) {
            actionBar.setTitleVisiable(View.VISIBLE);
        } else {
            actionBar.setTitleVisiable(View.INVISIBLE);
            actionBar.connectSocket(SocketUtil.IM_CONNECT_STATE);
        }
        mEditText = (EditText) findViewById(R.id.editText);
        mTextView = (TextView) findViewById(R.id.textView);
        initSwipeRefreshLayout();
        listView = (ListView) findViewById(R.id.listview);
        adapter = new MainChatAdapter(this);
        adapter.setContactBean(contactBean);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    isPullScroll = true;
                } else if (scrollState == SCROLL_STATE_IDLE) {
                    isPullScroll = false;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                float newY = 0;
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        oldY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        newY = event.getY();
                        if (isPullScroll) {
                            if (newY - oldY > 50) {
                                if (layout_expand.getVisibility() == View.VISIBLE) {
                                    hideExpandView();
                                }
                                if (mEditText.hasFocus()) {
                                    hideSoftInput(mEditText);
                                }
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return false;
            }
        });
        iv_voice = (ImageView) findViewById(R.id.iv_voice);
        iv_biaoqing = (ImageView) findViewById(R.id.iv_biaoqing);
        layout_expand = (RelativeLayout) findViewById(R.id.layout_expand);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputHasContent) {
                    SocketUtil.getInstance().sendContent(mEditText.getText().toString(), MessageChatType.TYPE_TEXT, contactBean);
                } else {
                    showExpandView("更多");
                }
            }
        });
        iv_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExpandView("语音");
            }
        });
        iv_biaoqing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExpandView("表情");
            }
        });
        mEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_UP) {
                    hideExpandView();
                }
                return false;
            }
        });
        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        mEditText.addTextChangedListener(textWatcher);
        viewVoice.setCallBack(new VoiceView.CallBack() {
            @Override
            public void recordVoice(String path) {
                sendFileTask(MessageChatType.TYPE_VOICE, path);
            }
        });
        SocketUtil.getInstance().setmHandler(mHandler);
        clearUnreadCount();
        loadData();
    }

    /**
     * 系统下拉刷新
     */
    private void initSwipeRefreshLayout() {
//        swipeRefreshLayout.setProgressViewEndTarget(false, DisplayUtil.dp2px(130));
        // 设置颜色属性的时候一定要注意是引用了资源文件还是直接设置16进制的颜色，因为都是int值容易搞混
        // 设置下拉进度的背景颜色，默认就是白色的
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
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
                        pageNum++;
                        loadData();
                    }
                }, 200);
            }
        });
        swipeRefreshLayout.setEnabled(true);
    }

    @Override
    protected void onDestroy() {
        clearUserHandler();
        unRegisterReceiver();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == INTENT_REQUEST_PHOTO) {
                int type = data.getIntExtra("TYPE",0);
                String path = data.getStringExtra("Path");
                if (type>0){
                    sendFileTask(type, path);
                }
            } else if (requestCode == REQUEST_ALBUM) {
                ArrayList<FileItem> chooseItems = data.getParcelableArrayListExtra("images");
                if (chooseItems != null && chooseItems.size() > 0) {
                    for (FileItem fileItem : chooseItems) {
                        if (fileItem.getFileType().contains("image/")) {
                            sendFileTask(MessageChatType.TYPE_IMG, fileItem.getFilePath());
                        } else if (fileItem.getFileType().contains("video/")) {
                            sendFileTask(MessageChatType.TYPE_VIDEO, fileItem.getFilePath());
                        } else {
                            ToastUtil.show("文件格式不正確");
                        }
                    }
                }
            }
        }
    }

    private void clearUserHandler() {
        SocketUtil.getInstance().setmHandler(null);
        Config.toUserId = "";
        Config.toUserName = "";
    }

    @Override
    public void onBackPressed() {
        if (layout_expand.getVisibility() == View.VISIBLE) {
            layout_expand.setVisibility(View.GONE);
        } else {
            clearUserHandler();
            super.onBackPressed();
        }
    }

    private void hideExpandViews() {
        viewVoice.setVisibility(View.INVISIBLE);
        viewBiaoQing.setVisibility(View.INVISIBLE);
        viewMore.setVisibility(View.INVISIBLE);
    }

    private void showExpandView(String more) {
        hideExpandViews();
        switch (more) {
            case "语音":
                viewVoice.setVisibility(View.VISIBLE);
                break;
            case "表情":
                viewBiaoQing.setVisibility(View.VISIBLE);
                break;
            case "更多":
                viewMore.setVisibility(View.VISIBLE);
                break;
        }
        hideSoftInput(mEditText);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (layout_expand.getVisibility() != View.VISIBLE) {
                    layout_expand.setVisibility(View.VISIBLE);
                }
            }
        }, 200);
    }

    private void hideExpandView() {
        if (layout_expand.getVisibility() != View.GONE) {
            layout_expand.setVisibility(View.GONE);
        }
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(s)) {
                inputHasContent = false;
            } else {
                inputHasContent = true;
            }
            if (inputHasContent) {
                mTextView.setBackgroundResource(R.drawable.ic_chat_send);
            } else {
                mTextView.setBackgroundResource(R.drawable.icon_chat_add);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void clearUnreadCount() {
        SessionData sessionData = DBSessionImpl.getInstance().querySessionDataByFromId(Config.toUserId);
        if (sessionData != null) {
            sessionData.setMessage_unread_count(0);
            DBSessionImpl.getInstance().updateSession(sessionData);
        }
        BroadCastUtil.sendActionBroadCast(this, BroadCastUtil.ACTION_UPDATE_SESSION);
    }

    int pageNum = 0;

    private void loadData() {
        try {
            int offset = adapter.getCount();
            List<ChatRecordData> list = DBChatRecordImpl.getInstance().queryChatRecord(offset, Config.PageSize);
            if (list == null || list.size() == 0) {
                if (pageNum > 0) {
                    pageNum--;
                }
                return;
            }
            final int listCount = list.size();
            Collections.reverse(list);
            if (adapter != null) {
                if (pageNum == 0) {
                    adapter.setList(list);
                } else {
                    adapter.addList(0, list);
                }
                adapter.notifyDataSetChanged();
                listView.setSelection(listCount);
                if (pageNum > 0) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listView.smoothScrollToPosition(listCount - 1);
                        }
                    });
                }
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

    Handler handler = new Handler();

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        ChatRecordData baseBean = (ChatRecordData) msg.obj;
                        if (baseBean == null)
                            return;
                        /**
                         * type 9：聊天内容； 1：Ping； 2：Connect；3：Disconnect； 4：Login
                         * */
                        int type = baseBean.getMessagetype();
                        switch (type) {
                            case 1: //Ping
                                break;
                            case 2: //Connect
                                break;
                            case 3: //Disconnect
                                finish();
                                AppManager.getInstance().killActivity(MainActivity.class);
                                break;
                            case 4: //Login
                                break;
                            case 9: //聊天内容
                                break;
                        }
                        if (type != 9)
                            return;
                        adapter.addData(baseBean);
                        adapter.notifyDataSetChanged();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listView.smoothScrollToPosition(adapter.getCount());
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mEditText.setText("");
                    break;
                case 2:
                    ChatRecordData baseBean = (ChatRecordData) msg.obj;
                    if (baseBean == null)
                        return;
                    for (ChatRecordData chatRecordData : adapter.getList()) {
                        if (chatRecordData.getMessageid().equals(baseBean.getMessageid())) {
                            chatRecordData.setMessagestate(baseBean.getMessagestate());
                            break;
                        }
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case 3:
                    adapter.clearData();
                    pageNum = 0;
                    loadData();
                    break;
            }
        }
    };


    /**
     * 关闭输入法
     *
     * @param view
     */
    protected void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null && view != null) {
            view.clearFocus();
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 打开输入法
     */
    protected void toggleSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private BroadcastReceiver receiver;

    /**
     * 注册广播
     */
    private void regitserReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadCastUtil.ACTION_UPDATE_MESSAGE);
        intentFilter.addAction(BroadCastUtil.ACTION_CONNECTED);
        intentFilter.addAction(BroadCastUtil.ACTION_CONNECTING);
        intentFilter.addAction(BroadCastUtil.ACTION_DISCONNECT);
        if (receiver == null) {
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    switch (action) {
                        case BroadCastUtil.ACTION_UPDATE_MESSAGE:
                            ChatRecordData chatRecordData = (ChatRecordData) intent.getSerializableExtra("DATA");
                            if (adapter != null && chatRecordData != null && chatRecordData.getMessagetoid().equals(contactBean.getAccount())) {
                                List<ChatRecordData> list = adapter.getList();
                                for (ChatRecordData item : list) {
                                    item.setMessagestate(chatRecordData.getMessagestate());
                                    break;
                                }
                                adapter.notifyDataSetChanged();
                            }
                            break;
                        case BroadCastUtil.ACTION_CONNECTED:
                            actionBar.setTitleVisiable(View.VISIBLE);
                            actionBar.connectSocket(SocketUtil.IM_CONNECTED);
                            break;
                        case BroadCastUtil.ACTION_CONNECTING:
                            actionBar.setTitleVisiable(View.INVISIBLE);
                            actionBar.connectSocket(SocketUtil.IM_CONNECTING);
                            break;
                        case BroadCastUtil.ACTION_DISCONNECT:
                            actionBar.setTitleVisiable(View.INVISIBLE);
                            actionBar.connectSocket(SocketUtil.IM_DISCONNECT);
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

    private void sendFileTask(int fileType, String path) {
        if (TextUtils.isEmpty(path))
            return;
        File file = new File(path);
        if (file == null || !file.exists() || file.length() <= 0) {
            ToastUtil.show("文件不存在");
            return;
        }
        final ChatRecordData chatRecordData = SocketUtil.getInstance().sendContentFilePre(file.getAbsolutePath(), fileType, contactBean);
        if (chatRecordData == null)
            return;
        HttpTaskUtil.getInstance().postUploadTask(UrlConfig.HTTP_UPLOAD_IMAGES, new File(path), "file", new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                dismissProgressDialog();
                chatRecordData.setMessagestate(9);
                DBChatRecordImpl.getInstance().updateChatRecord(chatRecordData);
                FilterTimeOutManager.getInstance().scheduledNotTimeOut(chatRecordData);
                if (mHandler != null) {
                    Message message = Message.obtain();
                    message.what = 2;
                    message.obj = chatRecordData;
                    mHandler.sendMessage(message);
                }
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
                            String filePath = uploadFileBean.getFilePathMD5();
                            if (!TextUtils.isEmpty(filePath)) {
//                                        SocketUtil.getInstance().sendContent(uploadFileBean.getFilePathMD5(),MessageChatType.TYPE_IMG,contactBean);
                                chatRecordData.setMessagecontent(filePath);
                                SocketUtil.getInstance().sendContentFilePost(chatRecordData);
                                return;
                            }
                        }
                    }

                    chatRecordData.setMessagestate(9);
                    DBChatRecordImpl.getInstance().updateChatRecord(chatRecordData);
                    FilterTimeOutManager.getInstance().scheduledNotTimeOut(chatRecordData);
                    if (mHandler != null) {
                        Message message = Message.obtain();
                        message.what = 2;
                        message.obj = chatRecordData;
                        mHandler.sendMessage(message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    dismissProgressDialog();
                } finally {
                    dismissProgressDialog();
                }
            }
        });
    }
}
