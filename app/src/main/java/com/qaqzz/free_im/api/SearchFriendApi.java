package com.qaqzz.free_im.api;


import com.qaqzz.framework.entity.Constants;
import com.qaqzz.framework.utils.SpUtils;
import com.qaqzz.free_im.CardContants;
import com.qaqzz.free_im.bean.SearchFriendBean;
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
public class SearchFriendApi extends ApiUtil {
    public SearchFriendBean mInfo = new SearchFriendBean();
    public SearchFriendApi(String search){
        addParam("search",search);
    }

    @Override
    protected String getUrl() {
        return CardContants.URL+"/search/friend";
    }

    @Override
    protected void parseData(JSONObject jsonObject) throws Exception {
        try {
            JSONObject dataInfo = (JSONObject) jsonObject.get("data");
            JSONArray search_list = (JSONArray) dataInfo.getJSONArray("search_list");
            List<SearchFriendBean.SearchListBean> mSearchListBean = new ArrayList<>();
            for (int i = 0; i < search_list.length(); i++) {
                //循环遍历，获取json数据中data数组里的内容
                JSONObject Object = search_list.getJSONObject(i);
                SearchFriendBean.SearchListBean model = new SearchFriendBean.SearchListBean();
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
                    model.setBirthdate(Object.getString("birthdate"));

                    //保存到ArrayList集合中
                    mSearchListBean.add(model);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            mInfo.setSearch_list(mSearchListBean);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadLocalData(JSONObject jsonObject) throws Exception {
        try {
            JSONObject dataInfo = (JSONObject) jsonObject.get("data");
            JSONArray search_list = (JSONArray) dataInfo.getJSONArray("search_list");
            List<SearchFriendBean.SearchListBean> mSearchListBean = new ArrayList<>();
            for (int i = 0; i < search_list.length(); i++) {
                //循环遍历，获取json数据中data数组里的内容
                JSONObject Object = search_list.getJSONObject(i);
                SearchFriendBean.SearchListBean model = new SearchFriendBean.SearchListBean();
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
                    model.setBirthdate(Object.getString("birthdate"));
                    //保存到ArrayList集合中
                    mSearchListBean.add(model);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            mInfo.setSearch_list(mSearchListBean);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected boolean isBackInMainThread() {
        return true;
    }
}
