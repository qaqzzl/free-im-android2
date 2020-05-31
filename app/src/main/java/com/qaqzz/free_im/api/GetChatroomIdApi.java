package com.qaqzz.free_im.api;


import com.qaqzz.free_im.CardContants;
import com.qaqzz.free_im.bean.GetChatroomIdBean;
import com.qaqzz.free_im.bean.LoginBean;
import com.qaqzz.free_im.http.api.ApiUtil;

import org.json.JSONObject;

public class GetChatroomIdApi extends ApiUtil {
    public GetChatroomIdBean mInfo = new GetChatroomIdBean();
    public GetChatroomIdApi(String chatroom_id){
        addParam("chatroom_id",chatroom_id);
    }

    @Override
    protected String getUrl() {
        return CardContants.URL+"/common/get.message.id";
    }

    @Override
    protected void parseData(JSONObject jsonObject) throws Exception {
        try {
            JSONObject dataInfo = (JSONObject) jsonObject.get("data");
            mInfo.setMessage_id(dataInfo.optString("message_id"));
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadLocalData(JSONObject jsonObject) throws Exception {
        try {
            JSONObject dataInfo = (JSONObject) jsonObject.get("data");
            mInfo.setMessage_id(dataInfo.optString("message_id"));
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected boolean isBackInMainThread() {
        return true;
    }
}
