package com.qaqzz.free_im.api;



import com.qaqzz.framework.entity.Constants;
import com.qaqzz.free_im.http.api.ApiUtil;
import com.qaqzz.framework.utils.SpUtils;
import com.qaqzz.free_im.CardContants;
import com.qaqzz.free_im.bean.MemberInfoBean;

import org.json.JSONObject;
/**
 * FileName: AllFriendFragment
 * Founder: LiuGuiLin
 * Profile: 用户基本信息
 */
public class MemberInfoApi extends ApiUtil {
    public MemberInfoBean mInfo = new MemberInfoBean();
    public MemberInfoApi(){
        String token = SpUtils.getInstance().getString(Constants.SP_TOKEN, "");
        String uid = SpUtils.getInstance().getString(Constants.SP_USERID, "");
        addParam("uid",uid);
        addParam("access_token",token);
    }

    @Override
    protected String getUrl() {
        return CardContants.URL+"/user/member.info";
    }

    @Override
    protected void parseData(JSONObject jsonObject) throws Exception {
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
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected boolean isBackInMainThread() {
        return true;
    }
}
