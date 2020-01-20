package com.qaqzz.framework.base;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.qaqzz.framework.event.EventManager;
import com.qaqzz.framework.event.MessageEvent;
import com.qaqzz.framework.helper.ActivityHelper;
import com.qaqzz.framework.utils.LanguaueUtils;
import com.qaqzz.framework.utils.SystemUI;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


/**
 * FileName: BaseActivity
 * Founder: LiuGuiLin
 * Profile: 基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    //申请运行时权限的Code
    private static final int PERMISSION_REQUEST_CODE = 1000;
    //申请窗口权限的Code
    public static final int PERMISSION_WINDOW_REQUEST_CODE = 1001;

    //申明所需权限
    private String[] mStrPermission = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_FINE_LOCATION,


    };

    //保存没有同意的权限
    private List<String> mPerList = new ArrayList<>();
    //保存没有同意的失败权限
    private List<String> mPerNoList = new ArrayList<>();

    private OnPermissionsResult permissionsResult;

    /**
     * 一个方法请求权限
     *
     * @param permissionsResult
     */
    protected void request(OnPermissionsResult permissionsResult) {
        if (!checkPermissionsAll()) {
            requestPermissionAll(permissionsResult);
        }
    }

    /**
     * 判断单个权限
     *
     * @param permissions
     * @return
     */
    protected boolean checkPermissions(String permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int check = checkSelfPermission(permissions);
            return check == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    /**
     * 判断是否需要申请权限
     *
     * @return
     */
    protected boolean checkPermissionsAll() {
        mPerList.clear();
        for (int i = 0; i < mStrPermission.length; i++) {
            boolean check = checkPermissions(mStrPermission[i]);
            //如果不同意则请求
            if (!check) {
                mPerList.add(mStrPermission[i]);
            }
        }
        return mPerList.size() > 0 ? false : true;
    }

    /**
     * 请求权限
     *
     * @param mPermissions
     */
    protected void requestPermission(String[] mPermissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(mPermissions, PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * 申请所有权限
     *
     * @param permissionsResult
     */
    protected void requestPermissionAll(OnPermissionsResult permissionsResult) {
        this.permissionsResult = permissionsResult;
        requestPermission((String[]) mPerList.toArray(new String[mPerList.size()]));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        mPerNoList.clear();
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        //你有失败的权限
                        mPerNoList.add(permissions[i]);
                    }
                }
                if (permissionsResult != null) {
                    if (mPerNoList.size() == 0) {
                        permissionsResult.OnSuccess();
                    } else {
                        permissionsResult.OnFail(mPerNoList);
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected interface OnPermissionsResult {
        void OnSuccess();

        void OnFail(List<String> noPermissions);
    }

    /**
     * 判断窗口权限
     *
     * @return
     */
    protected boolean checkWindowPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(this);
        }
        return true;
    }

    /**
     * 请求窗口权限
     */
    protected void requestWindowPermissions() {
        Toast.makeText(this, "申请窗口权限，暂时没做UI交互", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION
                , Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, PERMISSION_WINDOW_REQUEST_CODE);
    }

    /**
     * EventBus的步骤：
     * 1.注册
     * 2.声明注册方法 onEvent
     * 3.发送事件
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SystemUI.fixSystemUI(this);
        // 在页面未初始化之前调用的初始化数据
        initWidows();

        if (initArgs(getIntent().getExtras())) {
            ActivityHelper.getInstance().addActivity(this);
            LanguaueUtils.updateLanguaue(this);
            EventManager.register(this);

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
    protected void onDestroy() {
        super.onDestroy();
        EventManager.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getType()) {
            case EventManager.EVENT_RUPDATE_LANGUAUE:
                LanguaueUtils.updateLanguaue(this);
                recreate();
                break;
        }
    }

}
