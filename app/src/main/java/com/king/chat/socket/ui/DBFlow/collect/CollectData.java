package com.king.chat.socket.ui.DBFlow.collect;

import com.king.chat.socket.ui.DBFlow.ChatDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

/**
 * Created by maesinfo on 2018/11/5.
 * 备注：DBFlow会根据你的类名自动生成一个表明，以此为例：
 * 这个类对应的表名为：UserData_Table，这是作者在实践中得出来的
 */

@Table(database = ChatDatabase.class)
public class CollectData extends BaseModel implements Serializable {
    @PrimaryKey(autoincrement = true)//ID自增
    public long id;

    /**
     * 类型
     */
    @Column
    public int collecttype;

    /**
     * 消息发送时间
     */
    @Column
    public long collecttime;

    /**
     * 内容
     */
    @Column
    public String content;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCollecttype() {
        return collecttype;
    }

    public void setCollecttype(int collecttype) {
        this.collecttype = collecttype;
    }

    public long getCollecttime() {
        return collecttime;
    }

    public void setCollecttime(long collecttime) {
        this.collecttime = collecttime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
