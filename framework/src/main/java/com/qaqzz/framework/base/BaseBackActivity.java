package com.qaqzz.framework.base;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;


/**
 * FileName: BaseBackActivity
 * Founder: LiuGuiLin
 * Profile: 有返回键的基类
 */
public class BaseBackActivity extends BaseActivity {
    @Override
    protected int getContentLayoutId() {
        return 0;
    }

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //显示返回键
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //清除阴影
            getSupportActionBar().setElevation(0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
