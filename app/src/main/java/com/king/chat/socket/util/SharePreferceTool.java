package com.king.chat.socket.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.king.chat.socket.App;

/**
 * SharePreferce保存数据到内存中
 *
 * @author luomin
 */
@SuppressLint({"WorldReadableFiles", "WorldWriteableFiles"})
public class SharePreferceTool {

    private SharedPreferences shareprefece;
    private SharedPreferences.Editor editor;
    public static final String _NAME = "HELLO_";

    /**
     * 获取单例
     *
     * @return SharePreferce
     * @author kw
     */
    private static class SharePreferceToolHolder{
        private static final SharePreferceTool INSTANCE = new SharePreferceTool();
    }

    @SuppressLint("WrongConstant")
    private SharePreferceTool(){
        String name = _NAME;
        shareprefece = App.getInstance().getSharedPreferences(name,
                Context.MODE_MULTI_PROCESS +
                        Context.MODE_APPEND +
                        Context.MODE_PRIVATE
        );
        editor = shareprefece.edit();
    }

    public static final SharePreferceTool getInstance(){
        return SharePreferceTool.SharePreferceToolHolder.INSTANCE;
    }


    public boolean isEmpty(String key) {
        return !shareprefece.contains(key);
    }

    /**
     * 清理缓存
     */
    public void clearCache() {
        editor.clear();
        editor.commit();
    }

    /**
     * 设置SharedPrefere缓存
     *
     * @param key   key 键值
     * @param value value 缓存内容
     */
    public boolean setCache(String key, Object value) {
        if (value instanceof Boolean)// 布尔对象
            editor.putBoolean(key, (Boolean) value);
        else if (value instanceof String)// 字符串
            editor.putString(key, (String) value);
        else if (value instanceof Integer)// 整型数
            editor.putInt(key, (Integer) value);
        else if (value instanceof Long)// 长整型
            editor.putLong(key, (Long) value);
        else if (value instanceof Float)// 浮点数
            editor.putFloat(key, (Float) value);
        return editor.commit();
    }

    public boolean delete(String key) {
        boolean isDel = false;
        if (editor == null)
            return isDel;
        editor.remove(key);
        isDel = editor.commit();
        return isDel;
    }

    /**
     * 读取缓存中的字符串 Create at 2013-6-17
     *
     * @param key
     * @return String
     */
    public String getString(String key) {
        return shareprefece.getString(key, "");
    }

    /**
     * 读取缓存中的字符串 Create at 2013-6-17
     *
     * @param key
     * @return String
     */
    public String getString(String key, String default_value) {
        return shareprefece.getString(key, "");
    }

    /**
     * 读取缓存中的布尔型缓存 Create at 2013-6-17
     *
     * @param key
     * @return boolean
     */
    public boolean getBoolean(String key) {
        return shareprefece.getBoolean(key, false);
    }

    /**
     * 读取缓存中的整型数 Create at 2013-6-17
     *
     * @param key
     * @return int
     */
    public int getInt(String key) {
        return shareprefece.getInt(key, 0);
    }

    /**
     * 读取缓存中的长整型数 Create at 2013-6-17
     *
     * @param key
     * @return long
     */
    public long getLong(String key) {
        return shareprefece.getLong(key, 0);
    }

    /**
     * 读取缓存中的长整型数 Create at 2013-6-17
     *
     * @param key
     * @return long
     */
    public long getLong(String key, long default_value) {
        return shareprefece.getLong(key, default_value);
    }

    /**
     * 读取缓存中的浮点数 Create at 2013-6-17
     *
     * @param key
     * @return float
     */
    public float getFloat(String key) {
        return shareprefece.getFloat(key, 0.0f);
    }

    /**
     * 判断是否有缓存
     *
     * @param string
     * @return
     */
    public boolean ifHaveShare(String string) {
        return shareprefece.contains(string);
    }

}
