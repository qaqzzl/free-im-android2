package com.qaqzz.framework.helper;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.qaqzz.framework.R;
import com.qaqzz.framework.manager.DialogManager;
import com.qaqzz.framework.manager.HttpManager;
import com.qaqzz.framework.utils.LogUtils;
import com.qaqzz.framework.view.DialogView;

import java.io.File;


/**
 * FileName: UpdateHelper
 * Founder: LiuGuiLin
 * Profile: App更新帮助类
 * <p>
 * 如何更新？
 * 1.将Apk上传至Bmob获得Url
 * 2.修改UpdateSet中的属性
 * versionCode +1
 */
public class UpdateHelper {

    private Context mContext;

    private DialogView mUpdateView;
    private TextView tv_desc;
    private TextView tv_confirm;
    private TextView tv_cancel;

    private ProgressDialog mProgressDialog;

    public UpdateHelper(Context mContext) {
        this.mContext = mContext;
    }

    public void updateApp(String version_name, String version_download, String version_description) {
        createUpdateDialog(version_name, version_download, version_description);
    }

    /**
     * 更新提示框
     */
    private void createUpdateDialog(final String version_name, final String version_download, final String version_description) {
        mUpdateView = DialogManager.getInstance().initView(mContext, R.layout.dialog_update_app);
        tv_desc = mUpdateView.findViewById(R.id.tv_update_desc);
        tv_confirm = mUpdateView.findViewById(R.id.tv_confirm);
        tv_cancel = mUpdateView.findViewById(R.id.tv_cancel);

        tv_desc.setText(version_description);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogManager.getInstance().hide(mUpdateView);
                downloadApk(version_download);
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogManager.getInstance().hide(mUpdateView);
            }
        });
        DialogManager.getInstance().show(mUpdateView);

        initProgress();
    }

    private void initProgress() {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
    }

    /**
     *
     * @param path 文件夹路径
     */
    public static void isExist(String path) {
        File file = new File(path);
        //判断文件夹是否存在,如果不存在则创建文件夹
        if (!file.exists()) {
            file.mkdir();
        }
    }

    /**
     * 下载
     */
    public void downloadApk(String version_download) {
//        String getPath = Environment.getDataDirectory().getPath();
//        String getPath = mContext.getFilesDir().getPath();
        String sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/free_im";
        isExist(sdcardPath);
        final String filePath = sdcardPath +"/"+ System.currentTimeMillis() + ".apk";

        if (mProgressDialog != null) {
            mProgressDialog.show();
        }

        //开始下载：
        LogUtils.i("onDownload version_download:" + version_download);
        HttpManager.getInstance().download(version_download, filePath, new HttpManager.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(String path) {
                mProgressDialog.dismiss();
                LogUtils.i("onDownloadSuccess:" + path);
                if (!TextUtils.isEmpty(path)) {
                    installApk(path);
                }
            }

            @Override
            public void onDownloading(int progress) {
                mProgressDialog.setProgress(progress);
                LogUtils.i("onDownloading:" + progress);
            }

            @Override
            public void onDownloadFailed(Exception e) {
                mProgressDialog.dismiss();
                LogUtils.i("onDownloadFailed:" + e.toString());
            }
        });
    }

    /**
     * 安装Apk
     *
     * @param filePath
     * @return
     */
    public void installApk(String filePath) {
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
            mContext.startActivity(intent);
        }catch (Exception e){
            LogUtils.e("installApk:" + e.toString());
            e.toString();
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        File apkFile = new File(filePath);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".fileprovider", apkFile);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        mContext.startActivity(intent);
    }
}
