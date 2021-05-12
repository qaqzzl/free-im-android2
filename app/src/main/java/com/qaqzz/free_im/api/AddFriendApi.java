package com.qaqzz.free_im.api;

import com.qaqzz.framework.entity.Constants;
import com.qaqzz.framework.utils.SpUtils;
import com.qaqzz.free_im.CardContants;
import com.qaqzz.free_im.bean.BaseBean;
import com.qaqzz.free_im.http.api.ApiUtil;

import org.json.JSONObject;

/**
 * @author qaqzz
 * @description TODO
 * @date 2020/3/19 10:26
 */
public class AddFriendApi extends ApiUtil {
    public BaseBean mInfo = new BaseBean();
    public AddFriendApi(String friend_id, String remark){
        addParam("friend_id",friend_id);
        addParam("remark",remark);
    }

    @Override
    protected String getUrl() {
        return CardContants.URL+"/user/add.friend";
    }

    @Override
    protected void parseData(JSONObject jsonObject) throws Exception {
        try {
            JSONObject dataInfo = (JSONObject) jsonObject.get("data");
            mInfo.setCode(dataInfo.optString("code"));
            mInfo.setMessage(dataInfo.optString("message"));
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadLocalData(JSONObject jsonObject) throws Exception {
        try {
            JSONObject dataInfo = (JSONObject) jsonObject.get("data");
            mInfo.setCode(dataInfo.optString("code"));
            mInfo.setMessage(dataInfo.optString("message"));
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected boolean isBackInMainThread() {
        return true;
    }
}
