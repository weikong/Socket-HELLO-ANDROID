package com.king.chat.socket.ui.DBFlow.user;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

import java.util.List;

/**
 * Created by maesinfo on 2018/11/5.
 */

public class DBUserImpl {

    private static class DBUserImplHolder{
        private static final DBUserImpl INSTANCE = new DBUserImpl();
    }

    public DBUserImpl() {
    }

    private ModelAdapter<UserData> adapter = FlowManager.getModelAdapter(UserData.class);

    public static final DBUserImpl getInstance(){
        return DBUserImplHolder.INSTANCE;
    }

    public long insertUser(UserData userData){
        return adapter.insert(userData);//插入
    }

    public void updateUser(){
        //再来点福利，update高级用法，增删改查都是同理，就不一一列举了
        SQLite.update(UserData.class).set(UserData_Table.name.eq("888")).where(UserData_Table.id.eq(1L)).execute();
    }

    public void updateUser(UserData userData){
        adapter.update(userData);
    }

    public boolean deleteUser(UserData userData){
        return adapter.delete(userData);//删除
    }

    public List<UserData> queryUser(){
        List<UserData> list = SQLite.select().from(UserData.class).queryList();
        return list;
    }

    public List<UserData> queryUser(String from){
        List<UserData> list = SQLite.select().from(UserData.class).where(UserData_Table.name.eq(from)).queryList();
        return list;
    }
}
