package com.king.chat.socket.ui.DBFlow.user;

import com.king.chat.socket.ui.DBFlow.ChatDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by maesinfo on 2018/11/5.
 * 备注：DBFlow会根据你的类名自动生成一个表明，以此为例：
 * 这个类对应的表名为：UserData_Table，这是作者在实践中得出来的
 */

@Table(database = ChatDatabase.class)
public class UserData extends BaseModel {
    @PrimaryKey(autoincrement = true)//ID自增
    public long id;

    /**
     * 姓名
     */
    @Column
    public String name;

    /**
     * 年龄
     */
    @Column
    public int age;

    /**
     * 性别
     */
    @Column
    public boolean sex;

    /**
     * 新增字段，内容
     */
    @Column
    public String content;//增加的字段
}
