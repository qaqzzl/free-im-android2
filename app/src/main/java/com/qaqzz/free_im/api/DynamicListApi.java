package com.qaqzz.free_im.api;


import com.qaqzz.framework.entity.Constants;
import com.qaqzz.framework.utils.SpUtils;
import com.qaqzz.free_im.CardContants;
import com.qaqzz.free_im.bean.DynamicListBean;
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
public class DynamicListApi extends ApiUtil {
    public DynamicListBean mInfo = new DynamicListBean();
    public DynamicListApi(String current_page, String perpage){
        addParam("page",current_page);
        addParam("perpage",perpage);
    }

    @Override
    protected String getUrl() {
        return CardContants.URL+"/dynamic/list";
    }

    @Override
    protected void parseData(JSONObject jsonObject) throws Exception {
        try {
            JSONObject dataInfo = (JSONObject) jsonObject.get("data");
            JSONArray list = (JSONArray) dataInfo.getJSONArray("list");
            List<DynamicListBean.ListBean> mDynamicListBean = new ArrayList<>();
            for (int i = 0; i < list.length(); i++) {
                //循环遍历，获取json数据中data数组里的内容
                JSONObject Object = list.getJSONObject(i);
                DynamicListBean.ListBean model = new DynamicListBean.ListBean();
                try {
                    model.setAvatar(Object.getString("avatar"));
                    model.setGender(Object.getString("gender"));
                    model.setMember_id(Object.getString("member_id"));
                    model.setNickname(Object.getString("nickname"));
                    model.setBirthdate(Object.getString("birthdate"));
                    model.setContent(Object.getString("content"));
                    model.setCreated_at(Object.getString("created_at"));
                    model.setDynamic_id(Object.getString("dynamic_id"));
                    model.setImage_url(Object.getString("image_url"));
                    model.setType(Object.getString("type"));
                    model.setVideo_cover(Object.getString("video_cover"));
                    model.setVideo_cover_width(Object.getString("video_cover_width"));
                    model.setVideo_cover_height(Object.getString("video_cover_height"));
                    model.setVideo_url(Object.getString("video_url"));

                    //保存到ArrayList集合中
                    mDynamicListBean.add(model);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mInfo.setTotal(dataInfo.getInt("total"));
            mInfo.setList(mDynamicListBean);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadLocalData(JSONObject jsonObject) throws Exception {
        try {
            JSONObject dataInfo = (JSONObject) jsonObject.get("data");
            JSONArray list = (JSONArray) dataInfo.getJSONArray("list");
            List<DynamicListBean.ListBean> mDynamicListBean = new ArrayList<>();
            for (int i = 0; i < list.length(); i++) {
                //循环遍历，获取json数据中data数组里的内容
                JSONObject Object = list.getJSONObject(i);
                DynamicListBean.ListBean model = new DynamicListBean.ListBean();
                try {
                    model.setAvatar(Object.getString("avatar"));
                    model.setGender(Object.getString("gender"));
                    model.setMember_id(Object.getString("member_id"));
                    model.setNickname(Object.getString("nickname"));
                    model.setBirthdate(Object.getString("birthdate"));
                    model.setContent(Object.getString("content"));
                    model.setCreated_at(Object.getString("created_at"));
                    model.setDynamic_id(Object.getString("dynamic_id"));
                    model.setImage_url(Object.getString("image_url"));
                    model.setType(Object.getString("type"));
                    model.setVideo_cover(Object.getString("video_cover"));
                    model.setVideo_cover_width(Object.getString("video_cover_width"));
                    model.setVideo_cover_height(Object.getString("video_cover_height"));
                    model.setVideo_url(Object.getString("video_url"));

                    //保存到ArrayList集合中
                    mDynamicListBean.add(model);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mInfo.setTotal(dataInfo.getInt("total"));
            mInfo.setList(mDynamicListBean);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected boolean isBackInMainThread() {
        return true;
    }
}
