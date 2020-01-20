package com.qaqzz.free_im.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.qaqzz.framework.base.BaseActivity;
import com.qaqzz.free_im.http.api.ApiListener;
import com.qaqzz.free_im.http.api.ApiUtil;
import com.qaqzz.framework.entity.Constants;
import com.qaqzz.framework.manager.DialogManager;
import com.qaqzz.framework.utils.SpUtils;
import com.qaqzz.framework.view.DialogView;
import com.qaqzz.framework.view.LodingView;
import com.qaqzz.framework.view.TouchPictureV;
import com.qaqzz.free_im.MainActivity;
import com.qaqzz.free_im.R;
import com.qaqzz.free_im.api.LoginApi;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * FileName: LoginActivity
 * Founder: LiuGuiLin
 * Profile: 登录页
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 1.点击发送的按钮，弹出一个提示框，图片验证码，验证通过之后
     * 2.!发送验证码，@同时按钮变成不可点击，@按钮开始倒计时，倒计时结束，@按钮可点击，@文字变成“发送”
     * 3.通过手机号码和验证码进行登录
     * 4.登录成功之后获取本地对象
     */

    private EditText et_phone;
    private EditText et_code;
    private Button btn_send_code;
    private Button btn_login;

    private DialogView mCodeView;
    private TouchPictureV mPictureV;

    private TextView tv_password_login;

    private LodingView mLodingView;

    private static final int H_TIME = 1001;
    //60s倒计时
    private static int TIME = 60;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case H_TIME:
                    TIME--;
                    btn_send_code.setText(TIME + "s");
                    if (TIME > 0) {
                        mHandler.sendEmptyMessageDelayed(H_TIME, 1000);
                    } else {
                        btn_send_code.setEnabled(true);
                        btn_send_code.setText(getString(R.string.text_login_send));
                    }
                    break;
            }
            return false;
        }
    });

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_login;
    }

    protected void initWidget() {

        initDialogView();

        tv_password_login = findViewById(R.id.tv_password_login);
        tv_password_login.setOnClickListener(this);

        et_phone = (EditText) findViewById(R.id.et_phone);
        et_code = (EditText) findViewById(R.id.et_code);
        btn_send_code = (Button) findViewById(R.id.btn_send_code);
        btn_login = (Button) findViewById(R.id.btn_login);

        btn_send_code.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        String phone = SpUtils.getInstance().getString(Constants.SP_PHONE, "");
        if (!TextUtils.isEmpty(phone)) {
            et_phone.setText(phone);
        }
    }

    private void initDialogView() {

        mLodingView = new LodingView(this);

        mCodeView = DialogManager.getInstance().initView(this, R.layout.dialog_code_view);
        mPictureV = mCodeView.findViewById(R.id.mPictureV);
        mPictureV.setViewResultListener(() -> {
            DialogManager.getInstance().hide(mCodeView);
            sendSMS();
        });
    }

    /**
     * 登陆
     */
    private void login() {
        //1.判断手机号码和验证码不为空
        final String phone = et_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, getString(R.string.text_login_phone_null),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String code = et_code.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(this, getString(R.string.text_login_code_null),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        //显示LodingView
        mLodingView.show(getString(R.string.text_login_now_login_text));
        try{
            LoginApi apiBase = new LoginApi(phone, code);
            apiBase.post(new ApiListener() {
                @Override
                public void success(ApiUtil api, JSONObject response) {
                    //把用户ID跟token||手机号保存下来
                    SpUtils.getInstance().putString(Constants.SP_PHONE, phone);
                    SpUtils.getInstance().putString(Constants.SP_TOKEN, apiBase.mInfo.getAccess_token());
                    SpUtils.getInstance().putString(Constants.SP_USERID, apiBase.mInfo.getUid());
                    mLodingView.hide();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
//                    super.success(api, response);
                }
                @Override
                public void error(ApiUtil api, JSONObject response) {
                    mLodingView.hide();
                    Toast.makeText(LoginActivity.this,response.optString("msg"),Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 发送短信验证码
     */
    private void sendSMS() {
        //1.获取手机号码
        String phone = et_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, getString(R.string.text_login_phone_null),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        btn_send_code.setEnabled(false);
        mHandler.sendEmptyMessage(H_TIME);
        Toast.makeText(LoginActivity.this, getString(R.string.text_user_resuest_succeed),
        Toast.LENGTH_SHORT).show();
        //2.请求短信验证码
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        String requestBody = "{\"phone\": \"18016276521\"}";
        Request request = new Request.Builder()
                .url("http://127.0.0.1:8066/login/send.login.sms")
                .post(RequestBody.create(mediaType, requestBody))
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TAG", "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("TAG", response.protocol() + " " +response.code() + " " + response.message());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    Log.d("TAG", headers.name(i) + ":" + headers.value(i));
                }
                Log.d("TAG", "onResponse: " + response.body().string());
            }
        });

//        BmobManager.getInstance().requestSMS(phone, new QueryListener<Integer>() {
//            @Override
//            public void done(Integer integer, BmobException e) {
//                if (e == null) {
//                    btn_send_code.setEnabled(false);
//                    mHandler.sendEmptyMessage(H_TIME);
//                    Toast.makeText(LoginActivity.this, getString(R.string.text_user_resuest_succeed),
//                            Toast.LENGTH_SHORT).show();
//                } else {
//                    LogUtils.e("SMS:" + e.toString());
//                    Toast.makeText(LoginActivity.this, getString(R.string.text_user_resuest_fail),
//                            Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_code:
                //图形验证码
                //DialogManager.getInstance().show(mCodeView);
                sendSMS();
                break;
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_password_login:
                //startActivity(new Intent(this, TestLoginActivity.class));
                break;
        }
    }
}
