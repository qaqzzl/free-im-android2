package com.qaqzz.framework.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.qaqzz.framework.R;
import com.qaqzz.framework.manager.DialogManager;
import com.qaqzz.framework.utils.AnimUtils;

/**
 * FileName: LodingView
 * Founder: LiuGuiLin
 * Profile: 加载提示框
 */
public class LodingView {

    private DialogView mLodingView;
    private ImageView iv_loding;
    private TextView tv_loding_text;
    private ObjectAnimator mAnim;

    public LodingView(Context mContext) {
        mLodingView = DialogManager.getInstance().initView(mContext, R.layout.dialog_loding);
        iv_loding = mLodingView.findViewById(R.id.iv_loding);
        tv_loding_text = mLodingView.findViewById(R.id.tv_loding_text);
        mAnim = AnimUtils.rotation(iv_loding);
    }

    /**
     * 设置加载的提示文本
     *
     * @param text
     */
    public void setLodingText(String text) {
        if (!TextUtils.isEmpty(text)) {
            tv_loding_text.setText(text);
        }
    }

    public void show() {
        mAnim.start();
        DialogManager.getInstance().show(mLodingView);
    }

    public void show(String text) {
        mAnim.start();
        setLodingText(text);
        DialogManager.getInstance().show(mLodingView);
    }

    public void hide() {
        mAnim.pause();
        DialogManager.getInstance().hide(mLodingView);
    }

    /**
     * 外部是否可以点击消失
     *
     * @param flag
     */
    public void setCancelable(boolean flag) {
        mLodingView.setCancelable(flag);
    }
}
