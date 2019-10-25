package com.king.chat.socket.util;


import com.king.chat.socket.App;
import com.king.chat.socket.ui.DBFlow.ChatDatabase;
import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.DatabaseHolder;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by maesinfo on 2019/10/25.
 */

public class DBFlowUtil {

    private static class DBFlowUtilHolder{
        private static final DBFlowUtil INSTANCE = new DBFlowUtil();
    }

    /**
     * 单一实例
     */
    public static final DBFlowUtil getInstance(){
        return DBFlowUtil.DBFlowUtilHolder.INSTANCE;
    }

    public void initDBFlow() {
        FlowManager.close();
        FlowConfig.Builder flowConfig = new FlowConfig.Builder(App.getInstance()).openDatabasesOnInit(false);
        addDatabase(flowConfig,null,ChatDatabase.class);
        FlowManager.init(flowConfig.build());
    }
    private void addDatabase(FlowConfig.Builder flowConfig, Class<? extends DatabaseHolder> databaseHolderClass, Class<? extends ChatDatabase> databaseClass){
//        flowConfig.addDatabaseHolder(databaseHolderClass);
        flowConfig.addDatabaseConfig(new DatabaseConfig.Builder(databaseClass).extensionName(UserInfoManager.getInstance().getAccount()+".db").build());
    }
}
