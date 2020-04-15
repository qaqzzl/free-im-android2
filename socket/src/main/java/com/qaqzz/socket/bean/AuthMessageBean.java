package com.qaqzz.socket.bean;

/**
 * 用户socket 认证结构体
 * @author qaqzz
 * @description TODO
 * @date 2020/1/15 16:53
 */
public class AuthMessageBean {
    private String device_id;
    private String user_id;
    private String access_token;
    private String device_type;
    private String client_type;

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getClient_type() {
        return client_type;
    }

    public void setClient_type(String client_type) {
        this.client_type = client_type;
    }

    public String getDevice_id() {
        return device_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
