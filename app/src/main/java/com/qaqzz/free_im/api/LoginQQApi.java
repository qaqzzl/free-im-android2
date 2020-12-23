package com.qaqzz.free_im.api;


import com.qaqzz.free_im.CardContants;
import com.qaqzz.free_im.bean.LoginBean;
import com.qaqzz.free_im.bean.LoginQQBean;
import com.qaqzz.free_im.http.api.ApiUtil;

import org.json.JSONObject;

public class LoginQQApi extends ApiUtil {
    public LoginQQBean mInfo = new LoginQQBean();
    public LoginQQApi(String openid, String access_token){
        addParam("openid",openid);
        addParam("access_token",access_token);
    }

    @Override
    protected String getUrl() {
        return CardContants.URL+"/login/qq";
    }

    @Override
    protected void parseData(JSONObject jsonObject) throws Exception {
        try {
            JSONObject dataInfo = (JSONObject) jsonObject.get("data");
            mInfo.setAccess_token(dataInfo.optString("access_token"));
            mInfo.setUid(dataInfo.optString("uid"));
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadLocalData(JSONObject jsonObject) throws Exception {
        try {
            JSONObject dataInfo = (JSONObject) jsonObject.get("data");
            mInfo.setAccess_token(dataInfo.optString("access_token"));
            mInfo.setUid(dataInfo.optString("uid"));
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected boolean isBackInMainThread() {
        return true;
    }
}
