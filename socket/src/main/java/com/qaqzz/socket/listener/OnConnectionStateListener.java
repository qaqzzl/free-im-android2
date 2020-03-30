package com.qaqzz.socket.listener;

/**
 * Created by melo on 2017/11/29.
 */

public interface OnConnectionStateListener {
    void onSuccess();

    void onFailed(int errorCode);
}
