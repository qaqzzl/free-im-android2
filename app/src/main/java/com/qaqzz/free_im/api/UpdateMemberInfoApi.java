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
public class UpdateMemberInfoApi extends ApiUtil {
    public UpdateMemberInfoApi() {
        String token = SpUtils.getInstance().getString(Constants.SP_TOKEN, "");
        String uid = SpUtils.getInstance().getString(Constants.SP_USERID, "");
        addParam("uid",uid);
        addParam("access_token",token);
    }

    public BaseBean mInfo = new BaseBean();
    /**
     * access_token : abc
     * uid : 10
     * nickname : 测试用户10-update
     * gender : w
     * birthdate : 0
     * signature : 作者联系方式:QQ395173209-update
     * avatar : https://blog.cdn.qaqzz.com/icon.png
     */

    private String access_token;
    private String uid;
    private String nickname;
    private String gender;
    private String birthdate;
    private String signature;
    private String avatar;


    @Override
    protected String getUrl() {
        return CardContants.URL+"/user/update.member.info";
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

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        addParam("nickname",nickname);
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        addParam("gender",gender);
        this.gender = gender;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        addParam("birthdate",birthdate);
        this.birthdate = birthdate;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        addParam("signature",signature);
        this.signature = signature;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        addParam("avatar",avatar);
        this.avatar = avatar;
    }
}
