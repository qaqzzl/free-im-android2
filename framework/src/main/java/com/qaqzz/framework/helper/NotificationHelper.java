package com.qaqzz.framework.helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.qaqzz.framework.R;
import com.qaqzz.framework.utils.SpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * FileName: NotificationHelper
 * Founder: LiuGuiLin
 * Profile: 通知栏管理
 */
public class NotificationHelper {

    //添加好友
    public static final String CHANNEL_ADD_FRIEND = "add_friend";
    //同意好友
    public static final String CHANNEL_AGREED_FRIEND = "agreed_friend";
    //消息
    public static final String CHANNEL_MESSAGE = "message";

    private static NotificationHelper mInstance = null;

    private Context mContext;
    private NotificationManager notificationManager;

    private List<String> mIdList = new ArrayList<>();

    private NotificationHelper() {

    }

    public static NotificationHelper getInstance() {
        if (mInstance == null) {
            synchronized (NotificationHelper.class) {
                if (mInstance == null) {
                    mInstance = new NotificationHelper();
                }
            }
        }
        return mInstance;
    }

    public void createChannel(Context mContext) {
        this.mContext = mContext;
        notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String channelId = CHANNEL_ADD_FRIEND;
            String channelName = mContext.getString(R.string.text_chat_add_friend_channl);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);

            channelId = CHANNEL_AGREED_FRIEND;
            channelName = mContext.getString(R.string.text_chat_argeed_friend_channl);
            importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);

            channelId = CHANNEL_MESSAGE;
            channelName = mContext.getString(R.string.text_chat_friend_msg_channl);
            importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);
        }
    }

    private void createNotificationChannel(String channelId, String channelName, int importance) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * 发送通知
     *
     * @param objectid  发送人ID
     * @param channelId 渠道ID
     * @param title     标题
     * @param text      内容
     * @param mBitmap   头像
     * @param intent    跳转目标
     */
    private void pushNotification(String objectid, String channelId, String title, String text, Bitmap mBitmap, PendingIntent intent) {
        //对开关进行限制
        boolean isTips = SpUtils.getInstance().getBoolean("isTips", true);
        if (!isTips) {
            return;
        }

        Notification notification = new NotificationCompat.Builder(mContext, channelId)
                .setContentTitle(title)
                .setContentText(text)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.icon_message)
                .setLargeIcon(mBitmap)
                .setContentIntent(intent)
                .setAutoCancel(true)
                .build();

        if (!mIdList.contains(objectid)) {
            mIdList.add(objectid);
        }
        notificationManager.notify(mIdList.indexOf(objectid), notification);
    }

    /**
     * 发送添加好友的通知
     */
    public void pushAddFriendNotification(String objectid, String title, String text, Bitmap mBitmap, PendingIntent intent) {
        pushNotification(objectid, CHANNEL_ADD_FRIEND, title, text, mBitmap, intent);
    }

    /**
     * 发送同意好友的通知
     */
    public void pushArgeedFriendNotification(String objectid, String title, String text, Bitmap mBitmap, PendingIntent intent) {
        pushNotification(objectid, CHANNEL_AGREED_FRIEND, title, text, mBitmap, intent);
    }

    /**
     * 发送消息的通知
     */
    public void pushMessageNotification(String objectid, String title, String text, Bitmap mBitmap, PendingIntent intent) {
        pushNotification(objectid, CHANNEL_MESSAGE, title, text, mBitmap, intent);
    }
}
