package com.qaqzz.framework.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.qaqzz.framework.entity.Constants;

import java.util.Locale;

/**
 * FileName: LanguaueUtils
 * Founder: LiuGuiLin
 * Profile: 语言工具类
 */
public class LanguaueUtils {

    public static int SYS_LANGUAGE = 0;

    /**
     * 更新系统语言
     * @param mContext
     */
    public static void updateLanguaue(Context mContext) {
        Resources resources = mContext.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        //读取配置
        int languaue = SpUtils.getInstance().getInt(Constants.SP_LANGUAUE, 0);
        SYS_LANGUAGE = languaue;
        if (languaue == 0) {
            config.locale = Locale.SIMPLIFIED_CHINESE;
        } else if (languaue == 1) {
            config.locale = Locale.ENGLISH;
        } else {
            config.locale = Locale.SIMPLIFIED_CHINESE;
        }
        resources.updateConfiguration(config, dm);
    }
}
