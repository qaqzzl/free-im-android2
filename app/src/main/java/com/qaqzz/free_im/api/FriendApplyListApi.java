package com.qaqzz.free_im.api;


import com.qaqzz.framework.entity.Constants;
import com.qaqzz.framework.utils.SpUtils;
import com.qaqzz.free_im.CardContants;
import com.qaqzz.free_im.bean.FriendApplyListBean;
import com.qaqzz.free_im.http.api.ApiUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * FileName: AllFriendFragment
 * Founder: LiuGuiLin
 * Profile: 获取好友申请(新朋友)
 */
public class FriendApplyListApi extends ApiUtil {
    public FriendApplyListBean mInfo = new FriendApplyListBean();
    public FriendApplyListApi(){
        String token = SpUtils.getInstance().getString(Constants.SP_TOKEN, "");
        String uid = SpUtils.getInstance().getString(Constants.SP_USERID, "");
        addParam("uid",uid);
        addParam("access_token",token);
    }

    @Override
    protected String getUrl() {
        return CardContants.URL+"/user/friend.apply.list";
    }

    @Override
    protected void parseData(JSONObject jsonObject) throws Exception {
        try {
            JSONObject dataInfo = (JSONObject) jsonObject.get("data");
            JSONArray apply_list = (JSONArray) dataInfo.getJSONArray("apply_list");
            List<FriendApplyListBean.ApplyListBean> mFriendApplyListBean = new ArrayList<>();
            for (int i = 0; i < apply_list.length(); i++) {
                //循环遍历，获取json数据中data数组里的内容
                JSONObject Object = apply_list.getJSONObject(i);
                FriendApplyListBean.ApplyListBean model = new FriendApplyListBean.ApplyListBean();
                try {
                    String avatar = Object.getString("avatar");
                    String gender = Object.getString("gender");
                    String member_id = Object.getString("member_id");
                    String nickname = Object.getString("nickname");
                    String signature = Object.getString("signature");
                    String remark = Object.getString("remark");
                    String status = Object.getString("status");
                    String id = Object.getString("id");

                    model.setAvatar(avatar);
                    model.setGender(gender);
                    model.setMember_id(member_id);
                    model.setNickname(nickname);
                    model.setSignature(signature);
                    model.setRemark(remark);
                    model.setStatus(status);
                    model.setId(id);

                    //保存到ArrayList集合中
                    mFriendApplyListBean.add(model);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            mInfo.setApply_list(mFriendApplyListBean);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadLocalData(JSONObject jsonObject) throws Exception {
        try {
            JSONObject dataInfo = (JSONObject) jsonObject.get("data");
            JSONArray apply_list = (JSONArray) dataInfo.getJSONArray("apply_list");
            List<FriendApplyListBean.ApplyListBean> mFriendApplyListBean = new ArrayList<>();
            for (int i = 0; i < apply_list.length(); i++) {
                //循环遍历，获取json数据中data数组里的内容
                JSONObject Object = apply_list.getJSONObject(i);
                FriendApplyListBean.ApplyListBean model = new FriendApplyListBean.ApplyListBean();
                try {
                    String avatar = Object.getString("avatar");
                    String gender = Object.getString("gender");
                    String member_id = Object.getString("member_id");
                    String nickname = Object.getString("nickname");
                    String signature = Object.getString("signature");
                    String remark = Object.getString("remark");
                    String status = Object.getString("status");
                    String id = Object.getString("id");

                    model.setAvatar(avatar);
                    model.setGender(gender);
                    model.setMember_id(member_id);
                    model.setNickname(nickname);
                    model.setSignature(signature);
                    model.setRemark(remark);
                    model.setStatus(status);
                    model.setId(id);

                    //保存到ArrayList集合中
                    mFriendApplyListBean.add(model);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            mInfo.setApply_list(mFriendApplyListBean);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected boolean isBackInMainThread() {
        return true;
    }
}
