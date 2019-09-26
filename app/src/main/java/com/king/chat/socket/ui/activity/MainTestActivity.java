package com.king.chat.socket.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.king.chat.socket.config.Config;
import com.king.chat.socket.R;
import com.king.chat.socket.bean.BaseBean;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainTestActivity extends AppCompatActivity {

    private EditText mEditText;
    private TextView mTextView;
    private TextView tv_content;
    private static final String TAG = "MyChatSccket";
    private PrintWriter printWriter;
    private BufferedReader in;
    private ExecutorService mExecutorService = null;
    private ExecutorService mPingExecutorService = null;
    private String receiveMsg;
    private boolean isPing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);
        mEditText = (EditText) findViewById(R.id.editText);
        mTextView = (TextView) findViewById(R.id.textView);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_content.setMovementMethod(new ScrollingMovementMethod());
        mPingExecutorService = Executors.newSingleThreadExecutor();
        mExecutorService = Executors.newCachedThreadPool();
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendContent();
            }
        });
        connect();
    }

    @Override
    protected void onDestroy() {
        disconnect();
        super.onDestroy();

    }

    /**
     * 连接服务器
     * */
    public void connect() {
        mExecutorService.execute(new connectService());  //在一个新的线程中请求 Socket 连接
        mPingExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                if (!isPing) {
                    return;
                }
                while (isPing) {
                    try {
                        Thread.sleep(55 * 1000);
                        sendPing();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 发送聊天内容
     * */
    public void sendContent() {
        String sendMsg = mEditText.getText().toString();
        BaseBean baseBean = new BaseBean();
        baseBean.setContent(sendMsg);
        baseBean.setType(9);
        baseBean.setUserId(Config.userId);
        baseBean.setUserName(Config.userName);
        baseBean.setMessageId(UUID.randomUUID().toString());
        baseBean.setFrom(Config.userId);
        baseBean.setTo(Config.toUserId);
//        mExecutorService.execute(new sendService(sendMsg));
        mExecutorService.execute(new sendService(baseBean));
    }

    int PingCount = 0;

    /**
     * 发送Ping包
     * */
    public void sendPing() {
        PingCount++;
        BaseBean baseBean = new BaseBean();
        baseBean.setContent("PING");
        baseBean.setType(1);
        baseBean.setUserId(Config.userId);
        baseBean.setUserName(Config.userName);
        baseBean.setMessageId(UUID.randomUUID().toString());
        baseBean.setFrom(Config.userId);
//        mExecutorService.execute(new sendService("PING "+PingCount));
        mExecutorService.execute(new sendService(baseBean));
    }

    /**
     * 连接成功后登陆
     * */
    public BaseBean sendLogin() {
        BaseBean baseBean = new BaseBean();
        baseBean.setContent("Login");
        baseBean.setType(4);
        baseBean.setUserId(Config.userId);
        baseBean.setUserName(Config.userName);
        baseBean.setMessageId(UUID.randomUUID().toString());
        baseBean.setFrom(Config.userId);
        return baseBean;
    }

    /**
     * 取消连接Socket服务器
     * */
    public void disconnect() {
        BaseBean baseBean = new BaseBean();
        baseBean.setContent("断开连接");
        baseBean.setType(3);
        baseBean.setUserId(Config.userId);
        baseBean.setUserName(Config.userName);
        baseBean.setMessageId(UUID.randomUUID().toString());
        baseBean.setFrom(Config.userId);
        mExecutorService.execute(new sendService(baseBean));
        isPing = false;
    }

    /**
     * 发送服务接口
     * */
    private class sendService implements Runnable {
        private BaseBean baseBean;

        sendService(BaseBean baseBean) {
            this.baseBean = baseBean;
        }

        @Override
        public void run() {
            printWriter.println(JSON.toJSONString(baseBean));
        }
    }

    /**
     * 连接服务器接口
     * */
    private class connectService implements Runnable {
        @Override
        public void run() {//可以考虑在此处添加一个while循环，结合下面的catch语句，实现Socket对象获取失败后的超时重连，直到成功建立Socket连接
            try {
                Socket socket = new Socket(Config.HOST, Config.PORT);      //步骤一
                socket.setSoTimeout(60000);
                printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(   //步骤二
                        socket.getOutputStream(), "UTF-8")), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                receiveMsg();
            } catch (Exception e) {
                isPing = false;
                Log.e(TAG, ("connectService:" + e.getMessage()));   //如果Socket对象获取失败，即连接建立失败，会走到这段逻辑
            }
        }
    }

    /**
     * 接收消息
     * */
    private void receiveMsg() {
        try {
            while (isPing) {
                //步骤三
                if (in != null && (receiveMsg = in.readLine()) != null) {
//                  receiveMsg = in.readLine();
                    StringBuffer sbContent = new StringBuffer();
                    if (receiveMsg != null) {
                        sbContent.append(receiveMsg);
                        Message message = Message.obtain();
                        message.what = 1;
                        message.obj = receiveMsg;
                        mHandler.sendMessage(message);
                    }
                }
            }
        } catch (IOException e) {
//            receiveMsg: recvfrom failed: ECONNRESET (Connection reset by peer)
            Log.e(TAG, "receiveMsg: " + ((e != null) ? e.getMessage() : ""));
            isPing = false;
            e.printStackTrace();
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String jsonContent = (String) msg.obj;
                    try {
                        BaseBean baseBean = JSONObject.parseObject(jsonContent, BaseBean.class);
                        /**
                         * type 9：聊天内容； 1：Ping； 2：Connect；3：Disconnect； 4：Login
                         * */
                        int type = baseBean.getType();
                        switch (type) {
                            case 1: //Ping
                                break;
                            case 2: //Connect
                                printWriter.println(JSON.toJSONString(sendLogin()));
                                break;
                            case 3: //Disconnect
                                break;
                            case 4: //Login
                                break;
                            case 9: //聊天内容
                                break;
                        }
                        if (baseBean.getType() != 9)
                            return;
                        tv_content.setText(baseBean.getUserName() + "：" + baseBean.getContent() + "\n" + tv_content.getText());
                    } catch (Exception e) {
                        e.printStackTrace();
                        tv_content.setText(jsonContent + "\n" + tv_content.getText());
                    }
                    mEditText.setText("");
                    break;
            }
        }
    };

}
