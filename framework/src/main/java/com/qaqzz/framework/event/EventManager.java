package com.qaqzz.framework.event;

import org.greenrobot.eventbus.EventBus;

/**
 * FileName: EventManager
 * Founder: LiuGuiLin
 * Profile: EventBus调度类
 */
public class EventManager {

    //更新好友列表
    public static final int FLAG_UPDATE_FRIEND_LIST = 1000;

    //发送文本数据
    public static final int FLAG_SEND_TEXT = 1001;
    //发送图片数据
    public static final int FLAG_SEND_IMAGE = 1002;
    //发送位置数据
    public static final int FLAG_SEND_LOCATION = 1003;

    //相机数据
    public static final int FLAG_SEND_CAMERA_VIEW = 1004;

    //更新语言
    public static final int EVENT_RUPDATE_LANGUAUE = 1005;
    //刷新个人信息
    public static final int EVENT_REFRE_ME_INFO = 1006;
    //更新Token状态
    public static final int EVENT_REFRE_TOKEN_STATUS = 1007;

    //融云服务器连接状态
    public static final int EVENT_SERVER_CONNECT_STATUS = 1008;

    //退出登录页
    public static final int EVENT_EXIT_LOGIN_UI = 1009;


    /**
     * 注册
     * @param subscriber
     */
    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    /**
     * 反注册
     * @param subscriber
     */
    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    public static void post(int type){
        EventBus.getDefault().post(new MessageEvent(type));
    }

    public static void post(MessageEvent event){
        EventBus.getDefault().post(event);
    }
}
