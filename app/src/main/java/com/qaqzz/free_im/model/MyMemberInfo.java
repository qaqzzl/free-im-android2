package com.qaqzz.free_im.model;

import com.qaqzz.free_im.api.MemberInfoApi;
import com.qaqzz.free_im.bean.MemberInfoBean;
import com.qaqzz.free_im.http.api.ApiListener;
import com.qaqzz.free_im.http.api.ApiUtil;

import org.json.JSONObject;

/**
 * 我的个人信息
 * @author qaqzz
 * @description TODO
 * @date 2020/1/15 15:04
 */
public class MyMemberInfo {
    private MemberInfoBean mUserInfoBean = new MemberInfoBean();

    public MemberInfoBean getmUserInfoBean() {

        return mUserInfoBean;
    }

    public void setmUserInfoBean(MemberInfoBean mUserInfoBean) {
        this.mUserInfoBean = mUserInfoBean;
    }

    public void loadmUserInfoBean() {
        try{
            MemberInfoApi apiBase = new MemberInfoApi();
            apiBase.post(new ApiListener() {
                @Override
                public void success(ApiUtil api, JSONObject response) {
                    setmUserInfoBean(apiBase.mInfo);
                }
                @Override
                public void error(ApiUtil api, JSONObject response) {

                }
            });

        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
