package com.king.chat.socket.ui.DBFlow;

import com.king.chat.socket.ui.DBFlow.user.UserData;
import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;

/**
 * Created by maesinfo on 2018/11/5.
 * <p>
 * DBFlow 官方文档
 * https://agrosner.gitbooks.io/dbflow/content/
 */

@Database(name = ChatDatabase.NAME, version = ChatDatabase.VERSION)
public class ChatDatabase {
    //数据库名称
    public static final String NAME = "ChatDatabase";
    //数据库版本号
    public static final int VERSION = 1;

    @Migration(version = 2, database = ChatDatabase.class)
    public static class Migration2UserData extends AlterTableMigration<UserData> {

        public Migration2UserData(Class<UserData> table) {
            super(table);
        }

        @Override
        public void onPreMigrate() {
//            addColumn(SQLiteType.TEXT, "content");
        }
    }

}
