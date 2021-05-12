package com.qaqzz.free_im.api;

import com.qaqzz.framework.entity.Constants;
import com.qaqzz.framework.utils.SpUtils;
import com.qaqzz.free_im.CardContants;
import com.qaqzz.free_im.http.api.ApiUtil;

import org.json.JSONObject;

/**
 * @author qaqzz
 * @description TODO
 * @date 2020/3/19 10:26
 */
public class DynamicPublishApi extends ApiUtil {

    /**
     * uid : 10
     * access_token : abc
     * content : 我是内容呀
     * type : common
     * video_url :
     * video_cover :
     * video_cover_width : 0
     * video_cover_height : 0
     * image_url :
     */

    private String content;
    private String type;
    private String video_url;
    private String video_cover;
    private String video_cover_width;
    private String video_cover_height;
    private String image_url;

    public DynamicPublishApi(){

    }

    @Override
    protected String getUrl() {
        return CardContants.URL+"/dynamic/publish";
    }

    @Override
    protected void parseData(JSONObject jsonObject) throws Exception {

    }

    public void loadLocalData(JSONObject jsonObject) throws Exception {

    }

    @Override
    protected boolean isBackInMainThread() {
        return true;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        addParam("content",content);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        addParam("type",type);
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
        addParam("video_url",video_url);
    }

    public String getVideo_cover() {
        return video_cover;
    }

    public void setVideo_cover(String video_cover) {
        this.video_cover = video_cover;
        addParam("video_cover",video_cover);
    }

    public String getVideo_cover_width() {
        return video_cover_width;
    }

    public void setVideo_cover_width(String video_cover_width) {
        this.video_cover_width = video_cover_width;
        addParam("video_cover_width",video_cover_width);
    }

    public String getVideo_cover_height() {
        return video_cover_height;
    }

    public void setVideo_cover_height(String video_cover_height) {
        this.video_cover_height = video_cover_height;
        addParam("video_cover_height",video_cover_height);
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
        addParam("image_url",image_url);
    }
}
