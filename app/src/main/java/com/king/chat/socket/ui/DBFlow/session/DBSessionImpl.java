package com.king.chat.socket.ui.DBFlow.session;

import android.text.TextUtils;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.property.Property;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

import java.util.List;

/**
 * Created by maesinfo on 2018/11/5.
 */

public class DBSessionImpl {

    private static class DBSessionImplHolder {
        private static final DBSessionImpl INSTANCE = new DBSessionImpl();
    }

    public DBSessionImpl() {
    }

    private ModelAdapter<SessionData> adapter = FlowManager.getModelAdapter(SessionData.class);

    public static final DBSessionImpl getInstance() {
        return DBSessionImplHolder.INSTANCE;
    }

    public long insertSession(SessionData SessionData) {
        return adapter.insert(SessionData);//插入
    }

//    public void updateSession(String messageId){
//        //再来点福利，update高级用法，增删改查都是同理，就不一一列举了
//        SQLite.update(SessionData.class).set(SessionData_Table.messagestate.eq(1)).where(SessionData_Table.messageid.eq(messageId)).execute();
//    }

    public void updateSession(String messageFromId) {
        //再来点福利，update高级用法，增删改查都是同理，就不一一列举了
        SQLite.update(SessionData.class).set(SessionData_Table.messagestate.eq(1)).where(SessionData_Table.messagefromid.eq(messageFromId)).execute();
    }

    public boolean updateSession(SessionData sessionData) {
        boolean isUpdate = false;
        try {
            if (sessionData == null || TextUtils.isEmpty(sessionData.getMessagefromid()))
                return isUpdate;
            if (querySessionDataByFromId(sessionData.getMessagefromid()) != null){
                isUpdate = adapter.update(sessionData);
            } else {
                long insert = insertSession(sessionData);
                if (insert > 0)
                    isUpdate = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isUpdate;
    }

    public boolean deleteSession(SessionData SessionData) {
        return adapter.delete(SessionData);//删除
    }

    public List<SessionData> querySession() {
        List<SessionData> list = SQLite.select().from(SessionData.class).orderBy(SessionData_Table.messagetime, false).queryList();
        return list;
    }

    public long querySessionAllUnread() {
        long count = SQLite.selectCountOf().from(SessionData.class).where(SessionData_Table.message_unread_count.greaterThan(0)).count();
        return count;
    }

    public int querySessionAllUnread2() {
        Property<Integer> onA = SessionData_Table.message_unread_count;// 别名A条件
        long count = SQLite.select(new Method("sum", onA)).from(SessionData.class)
                .where(SessionData_Table.message_unread_count.greaterThan(0)).count();
        return (int) count;
    }

    public SessionData querySessionDataByFromId(String fromid) {
        if (TextUtils.isEmpty(fromid))
            return null;
        SessionData sessionData = SQLite.select().from(SessionData.class).where(SessionData_Table.messagefromid.eq(fromid)).querySingle();
        return sessionData;
    }
}
