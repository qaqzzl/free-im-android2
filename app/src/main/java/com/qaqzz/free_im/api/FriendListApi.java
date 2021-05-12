package com.qaqzz.free_im.api;


import com.qaqzz.framework.entity.Constants;
import com.qaqzz.framework.utils.SpUtils;
import com.qaqzz.free_im.CardContants;
import com.qaqzz.free_im.bean.FriendListBean;
import com.qaqzz.free_im.http.api.ApiUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * FileName: AllFriendFragment
 * Founder: LiuGuiLin
 * Profile: 获取全部好友
 */
public class FriendListApi extends ApiUtil {
    public FriendListBean mInfo = new FriendListBean();
    public FriendListApi(){
    }

    @Override
    protected String getUrl() {
        return CardContants.URL+"/user/friend.list";
    }

    @Override
    protected void parseData(JSONObject jsonObject) throws Exception {
        try {
            JSONObject dataInfo = (JSONObject) jsonObject.get("data");
            JSONArray friend_list = (JSONArray) dataInfo.getJSONArray("friend_list");
            List<FriendListBean.FriendBean> mFriendListBean = new ArrayList<>();
            for (int i = 0; i < friend_list.length(); i++) {
                //循环遍历，获取json数据中data数组里的内容
                JSONObject Object = friend_list.getJSONObject(i);
                FriendListBean.FriendBean model = new FriendListBean.FriendBean();
                try {
                    String avatar = Object.getString("avatar");
                    String gender = Object.getString("gender");
                    String member_id = Object.getString("member_id");
                    String nickname = Object.getString("nickname");
                    String signature = Object.getString("signature");

                    model.setAvatar(avatar);
                    model.setGender(gender);
                    model.setMember_id(member_id);
                    model.setNickname(nickname);
                    model.setSignature(signature);

                    //保存到ArrayList集合中
                    mFriendListBean.add(model);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            mInfo.setFriend_list(mFriendListBean);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadLocalData(JSONObject jsonObject) throws Exception {
        try {
            JSONObject dataInfo = (JSONObject) jsonObject.get("data");
            JSONObject friend_list = (JSONObject) dataInfo.get("friend_list");

            List<FriendListBean.FriendBean> mFriendListBean = new ArrayList<>();
            for (int i = 0; i < friend_list.length(); i++) {
                //循环遍历，获取json数据中data数组里的内容
                JSONObject Object = friend_list.getJSONObject(Integer.toString(i));
                FriendListBean.FriendBean model = new FriendListBean.FriendBean();
                try {
                    String avatar = Object.getString("avatar");
                    String gender = Object.getString("gender");
                    String member_id = Object.getString("member_id");
                    String nickname = Object.getString("nickname");
                    String signature = Object.getString("signature");

                    model.setAvatar(avatar);
                    model.setGender(gender);
                    model.setMember_id(member_id);
                    model.setNickname(nickname);
                    model.setSignature(signature);

                    //保存到ArrayList集合中
                    mFriendListBean.add(model);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mInfo.setFriend_list(mFriendListBean);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected boolean isBackInMainThread() {
        return true;
    }
}
