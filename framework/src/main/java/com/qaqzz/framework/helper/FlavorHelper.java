package com.qaqzz.framework.helper;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

/**
 * FileName: FlavorHelper
 * Founder: LiuGuiLin
 * Profile: 多渠道帮助类
 */
public class FlavorHelper {

    /**
     * 获取渠道号
     *
     * @param mContext
     * @return
     */
    public static String getFlavor(Context mContext) {
        PackageManager pm = mContext.getPackageManager();
        try {
            ApplicationInfo info = pm.getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = info.metaData;
            return bundle.getString("APP_CHANNEL");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
