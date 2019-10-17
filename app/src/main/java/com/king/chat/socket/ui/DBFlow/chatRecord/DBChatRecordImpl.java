package com.king.chat.socket.ui.DBFlow.chatRecord;

import android.text.TextUtils;

import com.king.chat.socket.config.Config;
import com.king.chat.socket.util.Logger;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

import java.util.List;

/**
 * Created by maesinfo on 2018/11/5.
 */

public class DBChatRecordImpl {

    private static class DBChatRecordImplHolder{
        private static final DBChatRecordImpl INSTANCE = new DBChatRecordImpl();
    }

    public DBChatRecordImpl() {
    }

    private ModelAdapter<ChatRecordData> adapter = FlowManager.getModelAdapter(ChatRecordData.class);

    public static final DBChatRecordImpl getInstance(){
        return DBChatRecordImplHolder.INSTANCE;
    }

    public long insertChatRecord(ChatRecordData ChatRecordData){
        return adapter.insert(ChatRecordData);//插入
    }

    public void updateChatRecord(String messageId){
        //再来点福利，update高级用法，增删改查都是同理，就不一一列举了
        SQLite.update(ChatRecordData.class).set(ChatRecordData_Table.messagestate.eq(1)).where(ChatRecordData_Table.messageid.eq(messageId)).execute();
    }

    public boolean updateChatRecord(ChatRecordData ChatRecordData){
        return adapter.update(ChatRecordData);
    }

    public boolean deleteChatRecord(ChatRecordData ChatRecordData){
        return adapter.delete(ChatRecordData);//删除
    }

    public void deleteGroupChatMessage(String groupAccount) {
        SQLite.delete(ChatRecordData.class)
                .where(ChatRecordData_Table.messagefromid.is(groupAccount))
                .execute();
    }

    public void deleteSingleChatMessage(String from,String to) {
        OperatorGroup op=OperatorGroup.clause(OperatorGroup.clause()
                .and(ChatRecordData_Table.messagefromid.eq(from))
                .and(ChatRecordData_Table.messagetoid.eq(to))
                .or(ChatRecordData_Table.messagefromid.eq(to))
                .and(ChatRecordData_Table.messagetoid.eq(from)));
        SQLite.delete(ChatRecordData.class).where(op).execute();
    }

    public List<ChatRecordData> queryChatRecord(){
        List<ChatRecordData> list = SQLite.select().from(ChatRecordData.class).queryList();
        return list;
    }

    public List<ChatRecordData> queryChatRecord(String from,int offset,int pageSize){
        OperatorGroup op=OperatorGroup.clause(OperatorGroup.clause()
                .and(ChatRecordData_Table.messagefromid.eq(from))
                .or(ChatRecordData_Table.messagetoid.eq(from)));
        List<ChatRecordData> list = SQLite.select().from(ChatRecordData.class)
                .where(op)
                .orderBy(ChatRecordData_Table.messagetime,false)
                .limit(pageSize)
                .offset(offset)
                .queryList();
        return list;
    }

    public List<ChatRecordData> queryChatRecord(int offset,int pageSize){
        OperatorGroup op=OperatorGroup.clause(OperatorGroup.clause()
                .and(ChatRecordData_Table.messagefromid.eq(Config.userId))
                .and(ChatRecordData_Table.messagetoid.eq(Config.toUserId))
                .or(ChatRecordData_Table.messagefromid.eq(Config.toUserId))
                .and(ChatRecordData_Table.messagetoid.eq(Config.userId)));
        List<ChatRecordData> list = SQLite.select().from(ChatRecordData.class)
                .where(op)
                .orderBy(ChatRecordData_Table.messagetime,false)
                .limit(pageSize)
                .offset(offset)
                .queryList();
        return list;
    }

    public List<ChatRecordData> queryGroupChatRecord(int offset,int pageSize){
        OperatorGroup op=OperatorGroup.clause(ChatRecordData_Table.messagefromid.eq(Config.toUserId));
        List<ChatRecordData> list = SQLite.select().from(ChatRecordData.class)
                .where(op)
                .orderBy(ChatRecordData_Table.messagetime,false)
                .limit(pageSize)
                .offset(offset)
                .queryList();
        return list;
    }

    public List<ChatRecordData> queryChatRecordImageAndVideo(){
        OperatorGroup op=OperatorGroup.clause(OperatorGroup.clause()
                .and(ChatRecordData_Table.messagefromid.eq(Config.userId))
                .and(ChatRecordData_Table.messagetoid.eq(Config.toUserId))
                .or(ChatRecordData_Table.messagefromid.eq(Config.toUserId))
                .and(ChatRecordData_Table.messagetoid.eq(Config.userId)));
        op.and(ChatRecordData_Table.messagechattype.eq(MessageChatType.TYPE_IMG));
        op.or(ChatRecordData_Table.messagechattype.eq(MessageChatType.TYPE_VIDEO));
        List<ChatRecordData> list = SQLite.select().from(ChatRecordData.class)
                .where(op)
                .orderBy(ChatRecordData_Table.messagetime,true)
                .queryList();
        return list;
    }

    public List<ChatRecordData> queryGroupChatRecordImageAndVideo(){
        OperatorGroup op=OperatorGroup.clause(ChatRecordData_Table.messagefromid.eq(Config.toUserId));
        op.and(ChatRecordData_Table.messagechattype.eq(MessageChatType.TYPE_IMG));
        op.or(ChatRecordData_Table.messagechattype.eq(MessageChatType.TYPE_VIDEO));
        List<ChatRecordData> list = SQLite.select().from(ChatRecordData.class)
                .where(op)
                .orderBy(ChatRecordData_Table.messagetime,true)
                .queryList();
        return list;
    }

    public ChatRecordData queryChatRecordByMessageId(String messageId){
        if (TextUtils.isEmpty(messageId))
            return null;
        ChatRecordData chatRecordData = SQLite.select().from(ChatRecordData.class).where(ChatRecordData_Table.messageid.eq(messageId)).querySingle();
        return chatRecordData;
    }
}
