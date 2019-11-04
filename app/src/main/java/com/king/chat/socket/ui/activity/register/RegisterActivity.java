package com.king.chat.socket.ui.activity.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.king.chat.socket.R;
import com.king.chat.socket.bean.ContactBean;
import com.king.chat.socket.bean.base.BaseTaskBean;
import com.king.chat.socket.config.Config;
import com.king.chat.socket.config.UrlConfig;
import com.king.chat.socket.ui.activity.MainActivity;
import com.king.chat.socket.ui.activity.base.BaseDataActivity;
import com.king.chat.socket.ui.view.actionbar.CommonActionBar;
import com.king.chat.socket.util.DBFlowUtil;
import com.king.chat.socket.util.SharePreferceTool;
import com.king.chat.socket.util.ToastUtil;
import com.king.chat.socket.util.UserInfoManager;
import com.king.chat.socket.util.httpUtil.HttpTaskUtil;
import com.king.chat.socket.util.httpUtil.OkHttpClientManager;
import com.king.chat.socket.util.socket.SocketUtil;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends BaseDataActivity {

    @BindView(R.id.action_bar)
    CommonActionBar actionBar;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_psd)
    EditText et_psd;
    @BindView(R.id.tv_register)
    TextView tv_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initActionBar();
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissionAllGranted(permissionAll)) {
                    registerTask();
                } else {
                    setExternalStoragePermissions(permissionAll);
                }
            }
        });
    }

    private void initActionBar() {
        actionBar.setTitle("注册");
    }


    @Override
    public void resultPermissions(boolean result) {
        if (result) {
            registerTask();
        }
    }

    private Map<String, String> buildRegister() {
        Map<String, String> params = new HashMap<>();
        String name = et_name.getText().toString();
        String psd = et_psd.getText().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(psd)) {
            Toast.makeText(RegisterActivity.this, "请输入用户名或密码", Toast.LENGTH_SHORT).show();
            return null;
        }
        params.put("account", name);
        params.put("password", psd);
        return params;
    }

    private void registerTask() {
        final Map<String, String> params = buildRegister();
        if (params == null)
            return;
        showProgreessDialog();
        HttpTaskUtil.getInstance().postTask(UrlConfig.HTTP_REGISTER, params, new OkHttpClientManager.StringCallback() {
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
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtil.show(baseTaskBean.getMessage());
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
