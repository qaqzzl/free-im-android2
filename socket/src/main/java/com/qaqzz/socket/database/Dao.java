package com.qaqzz.socket.database;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.qaqzz.framework.entity.Constants;
import com.qaqzz.framework.utils.SpUtils;

/**
 * @author qaqzz
 * @description TODO
 * @date 2020/4/1 0:52
 */
public class Dao {
    private Context mContext;
    private static Dao instance;
    private SQLiteDatabase database;
    private static DaoSession mDaoSession;

    private Dao(Context context) {
        mContext = context;
    }

    public static Dao getInstances(Context context){
        if (instance == null) {
            synchronized (Dao.class) {
                if (instance == null) {
                    instance = new Dao(context);
                }
            }
        }
        return instance;
    }

    public void init() {
        String uid = SpUtils.getInstance().getString(Constants.SP_USERID, "");
        //创建数据库
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(mContext, uid+"_FREE_IM.db");
        //获取可写数据库
        SQLiteDatabase database = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(database);
        //获取Dao对象管理者
        mDaoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDb() {
        return database;
    }
}
