package com.king.chat.socket.ui.DBFlow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.king.chat.socket.R;
import com.king.chat.socket.ui.DBFlow.user.DBUserImpl;
import com.king.chat.socket.ui.DBFlow.user.UserData;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DBFlowActivity extends AppCompatActivity {

    @BindView(R.id.tv_insert)
    TextView tv_insert;
    @BindView(R.id.tv_delete)
    TextView tv_delete;
    @BindView(R.id.tv_update)
    TextView tv_update;
    @BindView(R.id.tv_query)
    TextView tv_query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbflow);
        FlowManager.init(new FlowConfig.Builder(this).build());
//      FlowManager.init(this);//这句也可以初始化
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_insert)
    public void insertUser(){
        UserData userData = new UserData();
        userData.name = "平行线";
        userData.age = 18;
        userData.sex = true;
        userData.content = "添加內容";
        long id = DBUserImpl.getInstance().insertUser(userData);
        Log.e("DBFlowActivity","id = "+id);
    }

    @OnClick(R.id.tv_delete)
    public void deleteUser(){
        UserData userData = new UserData();
        userData.id = 1;
        boolean del = DBUserImpl.getInstance().deleteUser(userData);
        Log.e("DBFlowActivity","del = "+del);
    }

    @OnClick(R.id.tv_update)
    public void updateUser(){
        UserData userData = new UserData();
        userData.id = 1;
        userData.name = "平行线 2";
        DBUserImpl.getInstance().updateUser(userData);
    }

    @OnClick(R.id.tv_query)
    public void queryUser(){
        List<UserData> users = DBUserImpl.getInstance().queryUser();
        StringBuffer sb = new StringBuffer();
        for (UserData userData : users){
            sb.append(userData.name+" - "+userData.content);
        }
        Log.e("DBFlowActivity","queryUser = "+sb.toString());
    }
}
