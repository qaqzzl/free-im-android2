package com.qaqzz.framework.base;

import android.os.Bundle;

import com.qaqzz.framework.utils.SystemUI;

/**
 * FileName: BaseUIActivity
 * Founder: LiuGuiLin
 * Profile: UI 基类
 */
public class BaseUIActivity extends BaseActivity {
    @Override
    protected int getContentLayoutId() {
        return 0;
    }

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SystemUI.fixSystemUI(this);
    }
}
