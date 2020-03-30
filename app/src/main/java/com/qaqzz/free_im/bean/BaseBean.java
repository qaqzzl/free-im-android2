package com.qaqzz.free_im.bean;

/**
 * @author qaqzz
 * @description TODO
 * @date 2020/3/19 10:23
 */
public class BaseBean {
    /**
     * code : 1
     * message : 已发送, 等待对方同意
     */

    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
