package com.qaqzz.free_im.api;


import com.qaqzz.framework.entity.Constants;
import com.qaqzz.framework.utils.SpUtils;
import com.qaqzz.free_im.CardContants;
import com.qaqzz.free_im.bean.FriendIdGetChatroomIdBean;
import com.qaqzz.free_im.bean.MemberInfoBean;
import com.qaqzz.free_im.http.api.ApiUtil;

import org.json.JSONObject;

/**
 * FileName: AllFriendFragment
 * Founder: LiuGuiLin
 * Profile: 通过好友ID获取聊天室ID
 */
public class FriendIdGetChatroomIdApi extends ApiUtil {
    public FriendIdGetChatroomIdBean mInfo = new FriendIdGetChatroomIdBean();
    public FriendIdGetChatroomIdApi(String friend_id){
        String token = SpUtils.getInstance().getString(Constants.SP_TOKEN, "");
        String uid = SpUtils.getInstance().getString(Constants.SP_USERID, "");
        addParam("uid",uid);
        addParam("access_token",token);
        addParam("friend_id",friend_id);
    }

    @Override
    protected String getUrl() {
        return CardContants.URL+"/chatroom/friend_id.get.chatroom_id";
    }

    @Override
    protected void parseData(JSONObject jsonObject) throws Exception {
        try {
            JSONObject dataInfo = (JSONObject) jsonObject.get("data");
            mInfo.setChatroom_id(dataInfo.optString("chatroom_id"));
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadLocalData(JSONObject jsonObject) throws Exception {
        try {
            JSONObject dataInfo = (JSONObject) jsonObject.get("data");
            mInfo.setChatroom_id(dataInfo.optString("chatroom_id"));
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected boolean isBackInMainThread() {
        return true;
    }
}
