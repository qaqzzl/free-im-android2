package com.qaqzz.free_im.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.qaqzz.framework.base.BaseBackActivity;
import com.qaqzz.framework.entity.Constants;
import com.qaqzz.framework.event.EventManager;
import com.qaqzz.framework.helper.UpdateHelper;
import com.qaqzz.framework.manager.DialogManager;
import com.qaqzz.framework.utils.LanguaueUtils;
import com.qaqzz.framework.utils.PackageUtils;
import com.qaqzz.framework.utils.SpUtils;
import com.qaqzz.framework.view.DialogView;
import com.qaqzz.framework.view.UpdataDialog;
import com.qaqzz.free_im.R;
import com.qaqzz.free_im.api.AppNewVersionGetApi;
import com.qaqzz.free_im.http.api.ApiListener;
import com.qaqzz.free_im.http.api.ApiUtil;
import com.qaqzz.socket.socket.SocketManager;

import org.json.JSONObject;

/**
 * FileName: SettingActivity
 * Founder: LiuGuiLin
 * Profile: 设置
 */
public class SettingActivity extends BaseBackActivity implements View.OnClickListener {
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_setting;
    }

    private Switch sw_app_tips;
    private RelativeLayout rl_app_tips;
    private TextView tv_cache_size;
    private RelativeLayout rl_clear_cache;
    private TextView tv_current_languaue;
    private RelativeLayout rl_update_languaue;
    private RelativeLayout rl_check_permissions;
    private TextView tv_app_version;
    private Button btn_logout;
    private TextView tv_new_version;
    private RelativeLayout rl_check_version;
    private RelativeLayout rl_app_show;
    private RelativeLayout rl_chat_theme;

    private DialogView mLanguaueDialog;
    private TextView tv_zh;
    private TextView tv_en;
    private TextView tv_cancel;

    private boolean isTips;

    private UpdateHelper mUpdateHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initWidget() {

        sw_app_tips = (Switch) findViewById(R.id.sw_app_tips);
        rl_app_tips = (RelativeLayout) findViewById(R.id.rl_app_tips);

        tv_cache_size = (TextView) findViewById(R.id.tv_cache_size);
        rl_clear_cache = (RelativeLayout) findViewById(R.id.rl_clear_cache);

        tv_current_languaue = (TextView) findViewById(R.id.tv_current_languaue);
        rl_update_languaue = (RelativeLayout) findViewById(R.id.rl_update_languaue);

        rl_check_permissions = (RelativeLayout) findViewById(R.id.rl_check_permissions);
        tv_new_version = findViewById(R.id.tv_new_version);

        rl_app_show = (RelativeLayout) findViewById(R.id.rl_app_show);
        rl_app_show.setOnClickListener(this);

        tv_app_version = (TextView) findViewById(R.id.tv_app_version);
        rl_check_version = findViewById(R.id.rl_check_version);

        rl_chat_theme = findViewById(R.id.rl_chat_theme);

        btn_logout = (Button) findViewById(R.id.btn_logout);

        btn_logout.setOnClickListener(this);
        rl_clear_cache.setOnClickListener(this);
        rl_app_tips.setOnClickListener(this);
        sw_app_tips.setOnClickListener(this);
        rl_update_languaue.setOnClickListener(this);
        rl_check_permissions.setOnClickListener(this);
        rl_check_version.setOnClickListener(this);
        rl_chat_theme.setOnClickListener(this);

        try {
            tv_app_version.setText(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        isTips = SpUtils.getInstance().getBoolean("isTips", true);
        sw_app_tips.setChecked(isTips);

        tv_cache_size.setText("0.0 MB");

        int languaue = SpUtils.getInstance().getInt(Constants.SP_LANGUAUE, 0);
        tv_current_languaue.setText(languaue == 1 ? getString(R.string.text_setting_en) : getString(R.string.text_setting_zh));
        initLanguaueDialog();

        mUpdateHelper = new UpdateHelper(this);
        updateApp();
    }

    private void updateApp() {
        Context mContext = this;
        try {
            AppNewVersionGetApi apiBase = new AppNewVersionGetApi();
            apiBase.post(new ApiListener() {
                @Override
                public void success(ApiUtil api, JSONObject response) {
                    int version_code = PackageUtils.getVersionCode(mContext);
                    if (version_code < apiBase.mInfo.getVersion_code()) {
                        mUpdateHelper.updateApp(apiBase.mInfo.getVersion_name(),
                                apiBase.mInfo.getVersion_download(),
                                apiBase.mInfo.getVersion_description());
                    }
                }
                @Override
                public void error(ApiUtil api, JSONObject response) {

                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    private void initLanguaueDialog() {
        mLanguaueDialog = DialogManager.getInstance().initView(this, R.layout.dialog_select_photo, Gravity.BOTTOM);
        tv_zh = (TextView) mLanguaueDialog.findViewById(R.id.tv_camera);
        tv_en = (TextView) mLanguaueDialog.findViewById(R.id.tv_ablum);
        tv_cancel = (TextView) mLanguaueDialog.findViewById(R.id.tv_cancel);

        tv_zh.setText(getString(R.string.text_setting_zh));
        tv_en.setText(getString(R.string.text_setting_en));

        tv_zh.setOnClickListener(view -> {
            selectLanguaue(0);
            DialogManager.getInstance().hide(mLanguaueDialog);
        });

        tv_en.setOnClickListener(view -> {
            selectLanguaue(1);
            DialogManager.getInstance().hide(mLanguaueDialog);
        });

        tv_cancel.setOnClickListener(view -> DialogManager.getInstance().hide(mLanguaueDialog));
    }

    /**
     * @param index
     */
    private void selectLanguaue(int index) {
        if (LanguaueUtils.SYS_LANGUAGE == index) {
            return;
        }
        SpUtils.getInstance().putInt(Constants.SP_LANGUAUE, index);
        EventManager.post(EventManager.EVENT_RUPDATE_LANGUAUE);
        Toast.makeText(this, "Test Model , Reboot App ", Toast.LENGTH_SHORT).show();
        //暂时先重启处理
        finishAffinity();
        System.exit(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sw_app_tips:
            case R.id.rl_app_tips:
                isTips = !isTips;
                sw_app_tips.setChecked(isTips);
                SpUtils.getInstance().putBoolean("isTips", isTips);
                break;
            case R.id.rl_chat_theme:
                //startActivity(new Intent(this,ChatThemeActivity.class));
                Toast.makeText(this, getString(R.string.app_not_yet_developed), Toast.LENGTH_LONG).show();
                break;
            case R.id.rl_clear_cache:
                Toast.makeText(this, getString(R.string.cache_clear_success), Toast.LENGTH_LONG).show();
                break;
            case R.id.rl_update_languaue:
                DialogManager.getInstance().show(mLanguaueDialog);
                break;
            case R.id.rl_check_permissions:
                openWindow();
                break;
            case R.id.rl_check_version:
                updateApp();
                break;
            case R.id.btn_logout:
                /**
                 * 退出登录的逻辑
                 * 1.通过一个管理类管理好所有的Activity
                 * 2.清空Token
                 * 4.断开服务连接
                 * 5.跳转至登录页
                 * 7.停止云服务
                 */
                logout();
                break;
            case R.id.rl_app_show:
                Uri uri = Uri.parse("https://freeim.qaqzz.com/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
        }
    }

    /**
     * 退出登录
     */
    private void logout() {

        //删除Token
        SpUtils.getInstance().deleteKey(Constants.SP_TOKEN);
        //删除uid
        SpUtils.getInstance().deleteKey(Constants.SP_USERID);
        //删除用户昵称
        SpUtils.getInstance().deleteKey(Constants.SP_USER_NAME);
        //删除用户头像 || 手机号
        SpUtils.getInstance().deleteKey(Constants.SP_USER_AVATAR);
        SpUtils.getInstance().deleteKey(Constants.SP_PHONE);

        // 关闭socket
        SocketManager.getInstance(this).stopSocket();

        //跳转到登录页
        Intent intent_login = new Intent();
        intent_login.setClass(SettingActivity.this, LoginActivity.class);
        intent_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent_login);
        finish();
    }

    private void openWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION
                            , Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "已授予窗口权限", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "已授予窗口权限", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}


