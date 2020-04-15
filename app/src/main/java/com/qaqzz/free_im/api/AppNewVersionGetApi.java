package com.qaqzz.free_im.api;

import com.qaqzz.framework.entity.Constants;
import com.qaqzz.framework.utils.SpUtils;
import com.qaqzz.free_im.CardContants;
import com.qaqzz.free_im.bean.AppNewVersionGetBean;
import com.qaqzz.free_im.bean.BaseBean;
import com.qaqzz.free_im.http.api.ApiUtil;

import org.json.JSONObject;

/**
 * @author qaqzz
 * @description TODO
 * @date 2020/3/19 10:26
 */
public class AppNewVersionGetApi extends ApiUtil {
    public AppNewVersionGetBean mInfo = new AppNewVersionGetBean();
    public AppNewVersionGetApi(){
        addParam("client_type","android");
    }

    @Override
    protected String getUrl() {
        return CardContants.URL+"/app/new.version.get";
    }

    @Override
    protected void parseData(JSONObject jsonObject) throws Exception {
        try {
            JSONObject dataInfo = (JSONObject) jsonObject.get("data");
            mInfo.setVersion_code(dataInfo.getInt("version_code"));
            mInfo.setVersion_name(dataInfo.optString("version_name"));
            mInfo.setVersion_description(dataInfo.optString("version_description"));
            mInfo.setVersion_download(dataInfo.optString("version_download"));
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadLocalData(JSONObject jsonObject) throws Exception {
        try {
            JSONObject dataInfo = (JSONObject) jsonObject.get("data");
            mInfo.setVersion_code(dataInfo.getInt("version_code"));
            mInfo.setVersion_name(dataInfo.optString("version_name"));
            mInfo.setVersion_description(dataInfo.optString("version_description"));
            mInfo.setVersion_download(dataInfo.optString("version_download"));
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected boolean isBackInMainThread() {
        return true;
    }
}
