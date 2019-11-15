package com.king.chat.socket.ui.DBFlow.collect;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

import java.util.List;

/**
 * Created by maesinfo on 2018/11/5.
 */

public class DBCollectImpl {

    private static class DBCollectImplHolder {
        private static final DBCollectImpl INSTANCE = new DBCollectImpl();
    }

    public DBCollectImpl() {
    }

    public static final DBCollectImpl getInstance() {
        return DBCollectImplHolder.INSTANCE;
    }

    private ModelAdapter<CollectData> adapter = FlowManager.getModelAdapter(CollectData.class);

    public long insertCollect(CollectData CollectData) {
        return adapter.insert(CollectData);//插入
    }

    public void updateCollect() {
        //再来点福利，update高级用法，增删改查都是同理，就不一一列举了
        SQLite.update(CollectData.class).set(CollectData_Table.content.eq("888")).where(CollectData_Table.id.eq(1L)).execute();
    }

    public void updateCollect(CollectData CollectData) {
        adapter.update(CollectData);
    }

    public boolean deleteCollect(CollectData CollectData) {
        return adapter.delete(CollectData);//删除
    }

    public List<CollectData> queryCollect() {
        List<CollectData> list = SQLite.select().from(CollectData.class).orderBy(CollectData_Table.collecttime, false).queryList();
        return list;
    }
}
