package com.qaqzz.framework.view;

/**
 * @author qaqzz
 * @description TODO
 * @date 2020/3/31 0:29
 */

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import com.qaqzz.framework.R;
import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.indicators.BallClipRotatePulseIndicator;

/**
 * 加载中Dialog
 *
 * @author hzb
 */
public class LoadingDialog extends AlertDialog {

    private static LoadingDialog loadingDialog;
    private AVLoadingIndicatorView avi;

    public static LoadingDialog getInstance(Context context) {
        loadingDialog = new LoadingDialog(context, R.style.TransparentDialog); //设置AlertDialog背景透明
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);
        return loadingDialog;
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context,themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_loading);
        avi = (AVLoadingIndicatorView)this.findViewById(R.id.avi);
    }

    @Override
    public void show() {
        super.show();
        avi.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        avi.hide();
    }

//    @Override
//    public void hide() {
//        super.hide();
//        avi.hide();
//    }
}