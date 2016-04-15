package com.sf.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * sql数据库
 *
 * @author wjh
 */
public abstract class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String dbName, int dbVersion) {
        // CursorFactory设置为null,使用默认值
        super(context, dbName, null, dbVersion);
    }

    public DBHelper(Context context, String name, CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
    }


}
