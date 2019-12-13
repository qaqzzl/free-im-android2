package com.qaqzz.common.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @description TODO
 * @author qaqzz
 * @date 2019/12/13 2:16
 */
public abstract class Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在页面未初始化之前调用的初始化数据
        initWidows();

        if (initArgs(getIntent().getExtras())) {
            // 得到见面ID并设置到activity页面中
            int layId = getContentLayoutId();
            setContentView(layId);

            initWidget();
            initData();
        } else {
            finish();
        }
    }

    /**
     * 初始化窗口
     */
    protected void initWidows() {

    }

    /**
     * 初始化相关参数
     * @param bundle
     * @return 如果参数正确返回 true , 错误返回 false
     */
    protected boolean initArgs(Bundle bundle) {

        return true;
    }

    /**
     *  得到当前页面的资源文件ID
     * @return 资源文件ID
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected void initWidget() {
        ButterKnife.bind(this);
    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    @Override
    public boolean onSupportNavigateUp() {
        //当点击页面导航返回时 finish当前页面
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        @SuppressLint("RestrictedApi")
        // 得到当前activity下的所有Fragment
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();

        // 判断是否为空
        if ( fragmentList != null && fragmentList.size() > 0 ) {
            for (Fragment fragment : fragmentList) {
                // 判断是否为我们能够处理的 fragment 类型
                if (fragment instanceof com.qaqzz.common.app.Fragment) {
                    // 判断是否拦截了返回按钮
                    if (((com.qaqzz.common.app.Fragment) fragment).onBackPressed()) {
                        // 如果有直接 return
                        return;
                    }
                }
            }
        }

        super.onBackPressed();
        finish();
    }
}
