package com.qaqzz.free_im.bean;

/**
 * @author qaqzz
 * @description TODO
 * @date 2020/3/20 15:59
 */
public class QiniuUploadTokenBase {
    /**
     * code : 0
     * expires : 1584695040
     * domain  : https://blog.cdn.qaqzz.com/
     * message : 获取成功
     * token : qW7rPngWLk8Nl3MQfehQ_G5ELAZaH47Dej50Dj7k:VzyMqAQdn7CFZpp-pqeS2-7R39M=:eyJzY29wZSI6ImNkbjEiLCJkZWFkbGluZSI6MTU4NDY5NTA0MCwiZGV0ZWN0TWltZSI6MSwiZnNpemVMaW1pdCI6MjA5NzE1MiwiZm9yY2VTYXZlS2V5Ijp0cnVlLCJzYXZlS2V5IjoiJChrZXkpJChleHQpIiwiY2FsbGJhY2tCb2R5Ijoie1wia2V5XCI6XCIkKGtleSlcIixcImhhc2hcIjpcIiQoZXRhZylcIixcIm1pbWVUeXBlXCI6XCIkKG1pbWVUeXBlKVwiLFwiaW1hZ2VJbmZvXCI6JChpbWFnZUluZm8pLFwiZXh0XCI6XCIkKGV4dClcIn0iLCJjYWxsYmFja0JvZHlUeXBlIjoiYXBwbGljYXRpb24vanNvbiJ9
     */

    private String code;
    private String expires;
    private String domain;
    private String message;
    private String token;

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDomain() {
        return domain;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
