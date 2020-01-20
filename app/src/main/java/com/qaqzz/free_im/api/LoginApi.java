package com.qaqzz.free_im.api;



import com.qaqzz.free_im.http.api.ApiUtil;
import com.qaqzz.free_im.CardContants;
import com.qaqzz.free_im.bean.LoginBean;

import org.json.JSONObject;

public class LoginApi extends ApiUtil {
    public LoginBean mInfo = new LoginBean();
    public LoginApi(String phone, String verify_code){
        addParam("phone",phone);
        addParam("verify_code",verify_code);
    }

    @Override
    protected String getUrl() {
        return CardContants.URL+"/login";
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
