package com.sf.db;

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.sf.base.IApplication;

/**
 * 数据库 dao层管理者
 *
 * @author wjh
 */
public abstract class DBManager {

    private static String dbName;
    private static int dbVersion;
    private SQLiteDatabase db;

    public DBManager(String dbName, int dbVersion) {
        this.dbName = dbName;
        this.dbVersion = dbVersion;

        if (TextUtils.isEmpty(dbName) || dbVersion < 1) {
            throw new NullPointerException("dbName != null  || Version must be >= 1");
        }
    }

    public final LockObject lo = new LockObject();

    // 此对象为钥匙，可以去开锁
    private static class LockObject {
    }

    ;

    /**
     * 获取 db
     *
     * @return
     */
    public SQLiteDatabase getDB() {
        synchronized (lo) {
            if (db == null) {
                // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
                db = Helper.helper.getWritableDatabase();
            }
        }
        return db;
    }

    /**
     * 关闭 db
     */
    public void closeDB() {
        if (db == null) {
            return;
        }

        db.close();
    }

    private static class Helper {
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName,
        // 0,mFactory);
        public static final DBHelper helper = new DBHelper(
                IApplication.getIApplication(), dbName, dbVersion) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                onCreate(db);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                onUpgrade(db, oldVersion, newVersion);
            }
        };
    }

    // 数据库第一次创建时onCreate会被调用
    public abstract void onCreate(SQLiteDatabase db);

    // 版本号修改大于当前版本时,即会调用onUpgrade,仅执行一次。版本号小于当前版本号报错
    public abstract void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
}
