package com.qaqzz.common.app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @description TODO
 * @author qaqzz
 * @date 2019/12/13 2:17
 */
public abstract class Fragment extends androidx.fragment.app.Fragment {
    protected View mRoot;
    protected Unbinder mRootUnbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // 初始化参数
        initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot == null) {
            int layId = getContentLayoutId();
            // 初始化当前的根布局, 但是不在创建时就添加到container里面
            View root = inflater.inflate(layId, container, false);
            initWidget(root);
            mRoot = root;
        } else {
            if (mRoot.getParent()!=null) {
                // 把当前 Root 从父控件中移除
                ((ViewGroup)mRoot.getParent()).removeView(mRoot);
            }
        }

        return mRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 当View创建完成后初始化数据
        initData();
    }

    /**
     *  得到当前页面的资源文件ID
     * @return 资源文件ID
     */
    protected abstract int getContentLayoutId();


    /**
     * 初始化相关参数
     * @param bundle
     * @return 如果参数正确返回 true , 错误返回 false
     */
    protected void initArgs(Bundle bundle) {

    }

    /**
     * 初始化控件
     */
    protected void initWidget(View root) {
        mRootUnbinder = ButterKnife.bind(this, root);
    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 返回按键触发是调用
     * @return 返回 true 代表我已经处理返回逻辑, activity不用自己finish
     * 返回 false 代表我没有处理逻辑, activity自己走自己的逻辑
     */
    public boolean onBackPressed() {

        return false;
    }
}
