package com.qaqzz.free_im.api;


import com.qaqzz.framework.entity.Constants;
import com.qaqzz.framework.utils.SpUtils;
import com.qaqzz.free_im.CardContants;
import com.qaqzz.free_im.bean.OthersHomeInfoBean;
import com.qaqzz.free_im.http.api.ApiUtil;

import org.json.JSONObject;

/**
 * FileName: AllFriendFragment
 * Founder: LiuGuiLin
 * Profile: 获取他人主页基本信息
 */
public class OthersHomeInfoApi extends ApiUtil {
    public OthersHomeInfoBean mInfo = new OthersHomeInfoBean();
    public OthersHomeInfoApi(int member_id){
        addParam("member_id",member_id);
    }

    @Override
    protected String getUrl() {
        return CardContants.URL+"/user/others.home.info";
    }

    @Override
    protected void parseData(JSONObject jsonObject) throws Exception {
        try {
            JSONObject data = (JSONObject) jsonObject.get("data");
            JSONObject dataInfo = (JSONObject) data.getJSONObject("member_info");
            mInfo.setAvatar(dataInfo.optString("avatar"));
            mInfo.setBirthdate(dataInfo.optString("birthdate"));
            mInfo.setCity(dataInfo.optString("city"));
            mInfo.setGender(dataInfo.optString("gender"));
            mInfo.setMember_id(dataInfo.optString("member_id"));
            mInfo.setNickname(dataInfo.optString("nickname"));
            mInfo.setProvince(dataInfo.optString("province"));
            mInfo.setSignature(dataInfo.optString("signature"));
            mInfo.setIs_friend(data.optString("is_friend"));
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadLocalData(JSONObject jsonObject) throws Exception {
        try {
            JSONObject dataInfo = (JSONObject) jsonObject.get("data");
            mInfo.setAvatar(dataInfo.optString("avatar"));
            mInfo.setBirthdate(dataInfo.optString("birthdate"));
            mInfo.setCity(dataInfo.optString("city"));
            mInfo.setGender(dataInfo.optString("gender"));
            mInfo.setMember_id(dataInfo.optString("member_id"));
            mInfo.setNickname(dataInfo.optString("nickname"));
            mInfo.setProvince(dataInfo.optString("province"));
            mInfo.setSignature(dataInfo.optString("signature"));
            mInfo.setIs_friend(dataInfo.optString("is_friend"));
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected boolean isBackInMainThread() {
        return true;
    }
}
