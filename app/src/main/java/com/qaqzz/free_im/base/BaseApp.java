package com.qaqzz.free_im.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.qaqzz.free_im.http.Util.CrashHandler;
import com.qaqzz.free_im.http.api.OkHttpUtil;
import com.qaqzz.framework.Framework;
import com.qaqzz.free_im.im.SocketIm;

/**
 * FileName: BaseApp
 * Founder: LiuGuiLin
 * Profile: App
 */
public class BaseApp extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * Application的优化
         * 1.必要的组件在程序主页去初始化
         * 2.如果组件一定要在App中初始化，那么尽可能的延时
         * 3.非必要的组件，子线程中初始化
         */

        //只在主进程中初始化
        if (getApplicationInfo().packageName.equals(
                getCurProcessName(getApplicationContext()))) {
            //获取渠道
            //String flavor = FlavorHelper.getFlavor(this);
            //Toast.makeText(this, "flavor:" + flavor, Toast.LENGTH_SHORT).show();
            Framework.getFramework().initFramework(this);
        }
        super.onCreate();

        // http
        OkHttpUtil.init();
        mContext = this;
        CrashHandler.getInstance().init(this);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    /**
     * 获取当前进程名
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess :
                activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
}
