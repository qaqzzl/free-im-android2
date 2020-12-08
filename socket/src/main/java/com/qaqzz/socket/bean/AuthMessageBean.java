package com.qaqzz.socket.bean;

/**
 * 用户socket 认证结构体
 * @author qaqzz
 * @description TODO
 * @date 2020/1/15 16:53
 */
public class AuthMessageBean {
    private String DeviceId;
    private String UserId;
    private String AccessToken;
    private String DeviceType;
    private String ClientType;

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getAccessToken() {
        return AccessToken;
    }

    public void setAccessToken(String accessToken) {
        AccessToken = accessToken;
    }

    public String getDeviceType() {
        return DeviceType;
    }

    public void setDeviceType(String deviceType) {
        DeviceType = deviceType;
    }

    public String getClientType() {
        return ClientType;
    }

    public void setClientType(String clientType) {
        ClientType = clientType;
    }
}
