package com.sf.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.List;

/**
 * dao 层
 *
 * @author wjh
 */
public abstract class DbDao<T> {
    protected SQLiteDatabase db;

    public DbDao(SQLiteDatabase db) {
        super();
        this.db = db;
    }

    /**
     * 增加
     *
     * @param t
     */
    public synchronized void add(T t) {
    }

    ;

    /**
     * 增加 List<T>
     *
     * @param t
     */
    public synchronized void addList(List<T> t) {
    }

    ;

    /**
     * 删除
     *
     * @param values 条件参数
     */
    public synchronized void delete(String... values) {
    }

    ;

    /**
     * 修改
     *
     * @param t
     * @param values 条件参数
     */
    public synchronized void update(T t, String... values) {
    }

    ;

    /**
     * 根据values条件 查询
     *
     * @param values 条件参数
     */
    public synchronized List<T> query(String... values) {
        return null;
    }

    ;

    /**
     * 根据values条件 查询
     *
     * @param page
     * @param count
     * @param values
     * @return
     */
    public synchronized List<T> queryLimit(int page, int count, String... values) {
        return null;
    }

    ;

    /**
     * 分页查询获取一个表的所有的数据
     *
     * @param tableName
     * @param page
     * @param count
     * @return
     */
    protected Cursor queryTheCursor(String tableName, int page, int count) {
        return queryTheCursor(tableName, page, count, null);
    }

    /**
     * 分页查询 加条件
     *
     * @param tableName
     * @param page
     * @param count
     * @param where
     * @return
     */
    protected Cursor queryTheCursor(String tableName, int page, int count, String where) {
        if (db == null || tableName == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * FROM").append(" ").append(tableName);
        if (!TextUtils.isEmpty(where)) {
            builder.append(" ").append(where);
        }
        builder.append(" ").append("limit ?,?");
        return db.rawQuery(builder.toString(), new String[]{String.valueOf(page), String.valueOf(count)});
    }

    /**
     * 查询获取一个表的所有的数据
     *
     * @param tableName
     * @return
     */
    protected Cursor queryTheCursor(String tableName) {
        if (db == null || tableName == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * FROM").append(" ").append(tableName);
        return db.rawQuery(builder.toString(), null);
    }
}
