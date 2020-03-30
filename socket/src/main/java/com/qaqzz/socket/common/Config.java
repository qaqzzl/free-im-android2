package com.qaqzz.socket.common;

/**
 * Created by melo on 2017/11/27.
 */

public class Config {

    public static final String MSG = "msg";
    public static final String HEARTBREAK = "heartbreak";
    public static final String PING = "99";         // 心跳

    public static final String TCP_IP = "192.168.2.2";
    public static final String TCP_PORT = "1208";

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
