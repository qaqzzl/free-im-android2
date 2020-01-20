package com.qaqzz.framework.base;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.qaqzz.framework.event.EventManager;
import com.qaqzz.framework.event.MessageEvent;
import com.qaqzz.framework.utils.LanguaueUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * FileName: BaseFragment
 * Founder: LiuGuiLin
 * Profile: Fragment 基类
 */
public class BaseFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventManager.register(this);
    }

    /**
     * 判断窗口权限
     *
     * @return
     */
    protected boolean checkWindowPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(getActivity());
        }
        return true;
    }

    /**
     * 请求窗口权限
     */
    protected void requestWindowPermissions() {
        Toast.makeText(getActivity(), "申请窗口权限，暂时没做UI交互", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION
                , Uri.parse("package:" + getActivity().getPackageName()));
        startActivityForResult(intent, BaseActivity.PERMISSION_WINDOW_REQUEST_CODE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventManager.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getType()) {
            case EventManager.EVENT_RUPDATE_LANGUAUE:
                LanguaueUtils.updateLanguaue(getActivity());
                getActivity().recreate();
                break;
        }
    }
}
