package com.qaqzz.framework.manager;


import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * FileName: KeyWordManager
 * Founder: LiuGuiLin
 * Create Date: 2019/11/6 14:29
 * Email: lgl@szokl.com.cn
 * Profile: 软键盘管理类
 */
public class KeyWordManager {

    private Context mContext;
    private InputMethodManager imm;

    private static volatile KeyWordManager mInstance = null;

    private KeyWordManager() {

    }

    public static KeyWordManager getInstance() {
        if (mInstance == null) {
            synchronized (KeyWordManager.class) {
                if (mInstance == null) {
                    mInstance = new KeyWordManager();
                }
            }
        }
        return mInstance;
    }

    public void initManager(Context mContext) {
        this.mContext = mContext;
        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    /**
     * 隐藏软键盘
     *
     * @param mActivity
     */
    public void hideKeyWord(Activity mActivity) {
        if (mActivity != null && !mActivity.isDestroyed()) {
            if (imm != null) {
                imm.hideSoftInputFromWindow(mActivity.getWindow().getDecorView().getWindowToken(), 0);
            }
        }
    }
}
