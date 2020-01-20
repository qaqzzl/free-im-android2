package com.qaqzz.free_im.im;

/**
 * 用户socket 认证结构体
 * @author qaqzz
 * @description TODO
 * @date 2020/1/15 16:53
 */
public class AuthMessageStruct {
    private String device_id;
    private String user_id;
    private String access_token;

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
