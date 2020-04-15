package com.qaqzz.free_im.api;

import com.qaqzz.framework.entity.Constants;
import com.qaqzz.framework.utils.SpUtils;
import com.qaqzz.free_im.CardContants;
import com.qaqzz.free_im.bean.QiniuUploadTokenBase;
import com.qaqzz.free_im.http.api.ApiUtil;

import org.json.JSONObject;

/**
 * @author qaqzz
 * @description TODO
 * @date 2020/3/19 10:26
 */
public class QiniuUploadTokenApi extends ApiUtil {
    public QiniuUploadTokenBase mInfo = new QiniuUploadTokenBase();
    public QiniuUploadTokenApi(){
        String token = SpUtils.getInstance().getString(Constants.SP_TOKEN, "");
        String uid = SpUtils.getInstance().getString(Constants.SP_USERID, "");
        addParam("uid",uid);
        addParam("access_token",token);
    }

    @Override
    protected String getUrl() {
        return CardContants.URL+"/common/get.qiniu.upload.token";
    }

    @Override
    protected void parseData(JSONObject jsonObject) throws Exception {
        try {
            JSONObject dataInfo = (JSONObject) jsonObject.get("data");
            mInfo.setCode(dataInfo.optString("code"));
            mInfo.setMessage(dataInfo.optString("message"));
            mInfo.setToken(dataInfo.optString("token"));
            mInfo.setExpires(dataInfo.optString("expires"));
            mInfo.setDomain(dataInfo.optString("domain"));
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadLocalData(JSONObject jsonObject) throws Exception {
        try {
            JSONObject dataInfo = (JSONObject) jsonObject.get("data");
            mInfo.setCode(dataInfo.optString("code"));
            mInfo.setMessage(dataInfo.optString("message"));
            mInfo.setToken(dataInfo.optString("token"));
            mInfo.setExpires(dataInfo.optString("expires"));
            mInfo.setDomain(dataInfo.optString("domain"));
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected boolean isBackInMainThread() {
        return true;
    }
}
