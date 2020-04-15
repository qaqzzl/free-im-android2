package com.qaqzz.free_im;

import android.content.Context;
import android.util.Log;

import com.qaqzz.socket.database.Dao;
import com.qaqzz.socket.socket.SocketManager;

/**
 * @author qaqzz
 * @description TODO
 * @date 2020/3/31 22:49
 */
public class AppUtil {
    private Context mContext;

    private static volatile AppUtil instance = null;

    private AppUtil(Context context) {
        mContext = context;
    }

    public static AppUtil getInstance(Context context) {
        if (instance == null) {
            synchronized (AppUtil.class) {
                if (instance == null) {
                    instance = new AppUtil(context);
                }
            }
        }
        return instance;
    }

    // app 登录后初始化
    public void appLoginInit() {
        // 初始化数据库
        Dao.getInstances(mContext).init();
        // 启动socket
        SocketManager.getInstance(mContext).startTcpConnection();
    }
}
