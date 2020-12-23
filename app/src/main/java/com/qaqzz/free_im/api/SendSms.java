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
public class SendSms extends ApiUtil {
    public BaseBean mInfo = new BaseBean();
    public SendSms(String phone, String type){
        String token = SpUtils.getInstance().getString(Constants.SP_TOKEN, "");
        String uid = SpUtils.getInstance().getString(Constants.SP_USERID, "");
        addParam("uid",uid);
        addParam("access_token",token);
        addParam("phone",phone);
        addParam("type",type);
    }

    @Override
    protected String getUrl() {
        return CardContants.URL+"/common/send.sms";
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
}


/**
 *         MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
 *         String requestBody = "{\"phone\": \"18016276521\"}";
 *         Request request = new Request.Builder()
 *                 .url("http://127.0.0.1:8066/login/send.login.sms")
 *                 .post(RequestBody.create(mediaType, requestBody))
 *                 .build();
 *         OkHttpClient okHttpClient = new OkHttpClient();
 *         okHttpClient.newCall(request).enqueue(new Callback() {
 *             @Override
 *             public void onFailure(Call call, IOException e) {
 *                 Log.d("TAG", "onFailure: " + e.getMessage());
 *             }
 *
 *             @Override
 *             public void onResponse(Call call, Response response) throws IOException {
 *                 Log.d("TAG", response.protocol() + " " +response.code() + " " + response.message());
 *                 Headers headers = response.headers();
 *                 for (int i = 0; i < headers.size(); i++) {
 *                     Log.d("TAG", headers.name(i) + ":" + headers.value(i));
 *                 }
 *                 Log.d("TAG", "onResponse: " + response.body().string());
 *             }
 *         });
 *
 */
