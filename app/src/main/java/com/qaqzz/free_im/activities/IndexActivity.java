package com.qaqzz.free_im.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.qaqzz.framework.base.BaseActivity;
import com.qaqzz.framework.entity.Constants;
import com.qaqzz.framework.utils.LogUtils;
import com.qaqzz.framework.utils.SpUtils;
import com.qaqzz.free_im.MainActivity;
import com.qaqzz.free_im.R;
import com.qaqzz.free_im.http.Util.Util;

import java.util.List;


/**
 * FileName: IndexActivity
 * Founder: LiuGuiLin
 * Profile: 启动页
 */
public class IndexActivity extends AppCompatActivity {

    /**
     * 1.把启动页全屏
     * 2.延迟进入主页
     * 3.根据具体逻辑是进入主页还是引导页还是登录页
     * 4.适配刘海屏
     */

    private static final int SKIP_MAIN = 1000;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case SKIP_MAIN:
                    startMain();
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        mHandler.sendEmptyMessageDelayed(SKIP_MAIN, 2 * 1000);
    }

    /**
     * 进入主页
     */
    private void startMain() {
        Intent intent = new Intent();
        //1.判断App是否第一次启动 install - first run
//        boolean isFirstApp = SpUtils.getInstance().getBoolean(Constants.SP_IS_FIRST_APP, true);
        boolean isFirstApp = false;
        if (isFirstApp) {
            //跳转到引导页
            intent.setClass(this, GuideActivity.class);
            //非第一次启动
            SpUtils.getInstance().putBoolean(Constants.SP_IS_FIRST_APP, false);
        } else {

            //2.如果非第一次启动，判断是否曾经登录过
            String token = SpUtils.getInstance().getString(Constants.SP_TOKEN, "");
            if (TextUtils.isEmpty(token)) {
                //3.判断Bmob是否登录
                if (checkToken()) {
                    //跳转到主页
                    intent.setClass(this, MainActivity.class);
                } else {
                    //跳转到登录页
                    intent.setClass(this, LoginActivity.class);
                }
            } else {
                //跳转到主页
                intent.setClass(this, MainActivity.class);
            }
        }
        startActivity(intent);
        finish();
    }

    /**
     * 优化
     * 冷启动经过的步骤：
     * 1.第一次安装，加载应用程序并且启动
     * 2.启动后显示一个空白的窗口 getWindow()
     * 3.启动/创建了我们的应用进程
     *
     * App内部：
     * 1.创建App对象/Application对象
     * 2.启动主线程(Main/UI Thread)
     * 3.创建应用入口/LAUNCHER
     * 4.填充ViewGroup中的View
     * 5.绘制View measure -> layout -> draw
     *
     * 优化手段：
     * 1.视图优化
     *   1.设置主题透明
     *   2.设置启动图片
     * 2.代码优化
     *   1.优化Application
     *   2.布局的优化，不需要繁琐的布局
     *   3.阻塞UI线程的操作
     *   4.加载Bitmap/大图
     *   5.其他的一个占用主线程的操作
     *
     *
     * 检测App Activity的启动时间
     * 1.Shell
     *   ActivityManager -> adb shell am start -S -W com.imooc.meet/com.imooc.meet.ui.IndexActivity
     *   ThisTime: 478ms 最后一个Activity的启动耗时
     *   TotalTime: 478ms 启动一连串Activity的总耗时
     *   WaitTime: 501ms 应用创建的时间 + TotalTime
     *   应用创建时间： WaitTime - TotalTime（501 - 478 = 23ms）
     * 2.Log
     *   Android 4.4 开始，ActivityManager增加了Log TAG = displayed
     */

    @Override
    public void onBackPressed() {
        //引导页无需退出
        //super.onBackPressed();
    }

    /**
     * 检查TOKEN
     */
    private boolean checkToken() {
        LogUtils.i("checkToken");
        //获取TOKEN , USERID
        String token = SpUtils.getInstance().getString(Constants.SP_TOKEN, "");
        String uid = SpUtils.getInstance().getString(Constants.SP_USERID, "");
        if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(uid)) {
            return true;
        } else {
            return false;
        }
    }
}
