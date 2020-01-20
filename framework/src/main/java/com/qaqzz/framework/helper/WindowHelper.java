package com.qaqzz.framework.helper;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

/**
 * FileName: WindowHelper
 * Founder: LiuGuiLin
 * Profile: Window的辅助类
 */
public class WindowHelper {

    private static volatile WindowHelper mInstance = null;

    private Context mContext;
    private WindowManager wm;
    private WindowManager.LayoutParams lp;

    //Handler
    private Handler mHandler = new Handler();

    private WindowHelper() {
    }

    public static WindowHelper getInstance() {
        if (mInstance == null) {
            synchronized (WindowHelper.class) {
                if (mInstance == null) {
                    mInstance = new WindowHelper();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化Window
     *
     * @param mContext
     */
    public void initWindow(Context mContext) {
        this.mContext = mContext;

        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        lp = createLayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
    }

    /**
     * 创建LayoutParams
     *
     * @param width   宽
     * @param height  高
     * @param gravity 位置
     * @return
     */
    public WindowManager.LayoutParams createLayoutParams(int width, int height, int gravity) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        //设置宽高
        layoutParams.width = width;
        layoutParams.height = height;

        //设置标志位

        layoutParams.flags =
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;

        //设置格式
        layoutParams.format = PixelFormat.TRANSLUCENT;

        //设置位置
        layoutParams.gravity = gravity;

        //设置类型
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        return layoutParams;
    }

    /**
     * 创建View视图
     *
     * @param layoutId
     * @return
     */
    public View getView(int layoutId) {
        return View.inflate(mContext, layoutId, null);
    }

    /**
     * 显示窗口
     *
     * @param view
     */
    public void showView(final View view) {
        if (view != null) {
            if (view.getParent() == null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            wm.addView(view, lp);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    /**
     * 自定义属性
     *
     * @param view
     * @param layoutParams
     */
    public void showView(final View view, final WindowManager.LayoutParams layoutParams) {
        if (view != null) {
            if (view.getParent() == null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            wm.addView(view, layoutParams);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    /**
     * 隐藏视图
     *
     * @param view
     */
    public void hideView(final View view) {
        if (view != null) {
            if (view.getParent() != null) {
                try {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            wm.removeView(view);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 更新View的布局
     *
     * @param view
     * @param layoutParams
     */
    public void updateView(final View view, final WindowManager.LayoutParams layoutParams) {
        if (view != null && layoutParams != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    wm.updateViewLayout(view, layoutParams);
                }
            });
        }
    }
}
