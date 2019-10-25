package com.king.chat.socket.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.king.chat.socket.bean.ContactBean;
import com.king.chat.socket.bean.base.BaseTaskBean;
import com.king.chat.socket.config.UrlConfig;
import com.king.chat.socket.ui.activity.MainActivity;
import com.king.chat.socket.ui.activity.base.BaseDataActivity;
import com.king.chat.socket.config.Config;
import com.king.chat.socket.R;
import com.king.chat.socket.ui.activity.register.RegisterActivity;
import com.king.chat.socket.util.DBFlowUtil;
import com.king.chat.socket.util.SharePreferceTool;
import com.king.chat.socket.util.UserInfoManager;
import com.king.chat.socket.util.httpUtil.HttpTaskUtil;
import com.king.chat.socket.util.httpUtil.OkHttpClientManager;
import com.king.chat.socket.util.socket.SocketUtil;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseDataActivity {

    EditText et_name, et_psd;
    TextView tv_register;
    TextView tv_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_name = (EditText) findViewById(R.id.et_name);
        et_psd = (EditText) findViewById(R.id.et_psd);
        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissionAllGranted(permissionAll)){
                    loginTask();
                } else {
                    setExternalStoragePermissions(permissionAll);
                }
            }
        });
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent2Activity(RegisterActivity.class);
            }
        });
    }

    @Override
    public void resultPermissions(boolean result) {
        if (result){
            loginTask();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String strUserName = SharePreferceTool.getInstance().getString(Config.LOGIN_USER_NAME);
        if (!TextUtils.isEmpty(strUserName)){
            et_name.setText(strUserName);
            et_name.setSelection(strUserName.length());
        }
        String strPsd = SharePreferceTool.getInstance().getString(Config.LOGIN_USER_PSD);
        if (!TextUtils.isEmpty(strPsd)){
            et_psd.setText(strPsd);
            et_psd.setSelection(strPsd.length());
        }
    }

    private Map<String, String> buildLogin() {
        Map<String, String> params = new HashMap<>();
        String name = et_name.getText().toString();
        String psd = et_psd.getText().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(psd)) {
            Toast.makeText(LoginActivity.this, "请输入用户名或密码", Toast.LENGTH_SHORT).show();
            return null;
        }
        params.put("account", name);
        params.put("password", psd);
        return params;
    }

    private void loginTask() {
        final Map<String, String> params = buildLogin();
        if (params == null)
            return;
        showProgreessDialog();
        HttpTaskUtil.getInstance().postTask(UrlConfig.HTTP_LOGIN, params, new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                dismissProgressDialog();
            }

            @Override
            public void onResponse(String response) {
                try {
                    BaseTaskBean baseTaskBean = JSONObject.parseObject(response, BaseTaskBean.class);
                    if (baseTaskBean.getCode() == 1) {
                        SharePreferceTool.getInstance().setCache(Config.LOGIN_USER_NAME, params.get("account"));
                        SharePreferceTool.getInstance().setCache(Config.LOGIN_USER_PSD, params.get("password"));
                        ContactBean contactBean = JSONObject.parseObject(baseTaskBean.getData(), ContactBean.class);
                        UserInfoManager.getInstance().setContactBean(contactBean);
                        DBFlowUtil.getInstance().initDBFlow();
                        SocketUtil.getInstance().connect();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    dismissProgressDialog();
                }
            }
        });
    }
}
