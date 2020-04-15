package com.qaqzz.free_im.bean;

/**
 * @author qaqzz
 * @description TODO
 * @date 2020/4/3 0:59
 */
public class AppNewVersionGetBean {
    /**
     * version_code : 1
     * version_description : 修复已知bug
     修复部分机型无法接受消息
     * version_download : http://freeim.qaqzz.com
     * version_name : 1.0.0
     */

    private int version_code;
    private String version_description;
    private String version_download;
    private String version_name;

    public int getVersion_code() {
        return version_code;
    }

    public void setVersion_code(int version_code) {
        this.version_code = version_code;
    }

    public String getVersion_description() {
        return version_description;
    }

    public void setVersion_description(String version_description) {
        this.version_description = version_description;
    }

    public String getVersion_download() {
        return version_download;
    }

    public void setVersion_download(String version_download) {
        this.version_download = version_download;
    }

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }
}
