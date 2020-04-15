package com.qaqzz.socket.common;

import com.qaqzz.socket.BuildConfig;

/**
 * Created by melo on 2017/11/27.
 */

public class Config {

    public static final String MSG = "msg";
    public static final String HEARTBREAK = "heartbreak";
    public static final String PING = "99";         // 心跳

    public static final String TCP_IP = BuildConfig.SOCKET_SERVER;
    public static final String TCP_PORT = BuildConfig.SOCKET_SERVER_PORT;
    // 单个CPU线程池大小
    public static final int POOL_SIZE = 5;

    /**
     * 错误处理
     */
    public static class ErrorCode {

        public static final int CREATE_TCP_ERROR = 1;

        public static final int PING_TCP_TIMEOUT = 2;
    }

}
