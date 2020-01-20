package com.qaqzz.framework.utils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * FileName: AnimUtils
 * Founder: LiuGuiLin
 * Profile: 动画工具类
 */
public class AnimUtils {

    /**
     * 旋转动画
     * @param view
     * @return
     */
    public static ObjectAnimator rotation(View view) {
        ObjectAnimator mAnim = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
        mAnim.setDuration(2 * 1000);
        mAnim.setRepeatMode(ValueAnimator.RESTART);
        mAnim.setRepeatCount(ValueAnimator.INFINITE);
        mAnim.setInterpolator(new LinearInterpolator());
        return mAnim;
    }

}
