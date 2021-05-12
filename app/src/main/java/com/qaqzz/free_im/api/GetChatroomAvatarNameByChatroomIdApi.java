package com.qaqzz.free_im.api;


import com.qaqzz.framework.entity.Constants;
import com.qaqzz.framework.utils.SpUtils;
import com.qaqzz.free_im.CardContants;
import com.qaqzz.free_im.bean.FriendIdGetChatroomIdBean;
import com.qaqzz.free_im.bean.GetChatroomAvatarNameByChatroomIdBean;
import com.qaqzz.free_im.http.api.ApiUtil;

import org.json.JSONObject;

/**
 * FileName: AllFriendFragment
 * Founder: LiuGuiLin
 * Profile: 通过好友ID获取聊天室ID
 */
public class GetChatroomAvatarNameByChatroomIdApi extends ApiUtil {
    public GetChatroomAvatarNameByChatroomIdBean mInfo = new GetChatroomAvatarNameByChatroomIdBean();
    public GetChatroomAvatarNameByChatroomIdApi(String chatroom_id){
        addParam("chatroom_id",chatroom_id);
    }

    @Override
    protected String getUrl() {
        return CardContants.URL+"/chatroom/get.chatroom.avatar.name.by.chatroom_id";
    }

    @Override
    protected void parseData(JSONObject jsonObject) throws Exception {
        try {
            JSONObject dataInfo = (JSONObject) jsonObject.get("data");
            mInfo.setAvatar(dataInfo.optString("avatar"));
            mInfo.setName(dataInfo.optString("name"));
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadLocalData(JSONObject jsonObject) throws Exception {
        try {
            JSONObject dataInfo = (JSONObject) jsonObject.get("data");
            mInfo.setAvatar(dataInfo.optString("avatar"));
            mInfo.setName(dataInfo.optString("name"));
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected boolean isBackInMainThread() {
        return true;
    }
}
