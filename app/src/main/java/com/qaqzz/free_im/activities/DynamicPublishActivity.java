package com.qaqzz.free_im.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qaqzz.framework.base.BaseBackActivity;
import com.qaqzz.framework.helper.FileHelper;
import com.qaqzz.framework.utils.LogUtils;
import com.qaqzz.framework.view.LodingView;
import com.qaqzz.free_im.R;
import com.qaqzz.free_im.api.DynamicPublishApi;
import com.qaqzz.free_im.api.QiniuUploadTokenApi;
import com.qaqzz.free_im.http.api.ApiListener;
import com.qaqzz.free_im.http.api.ApiUtil;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;

/**
 * FileName: DynamicPublishActivity
 * Founder: LiuGuiLin
 * Profile: 发布动态
 */
public class DynamicPublishActivity extends BaseBackActivity implements View.OnClickListener {
    //输入框
    private EditText et_content;
    //文字数量
    private TextView tv_content_size;
    //清空
    private ImageView iv_error;
    //媒体路径
    private TextView tv_media_path;
    //已存媒体
    private LinearLayout ll_media;
    //相机
    private LinearLayout ll_camera;
    //相册
    private LinearLayout ll_ablum;
    //视频
    private LinearLayout ll_video;
    //媒体类型
    private LinearLayout ll_media_type;

    //要上传的文件
    private File uploadFile = null;
    // 上传文件到服务器后的路径
    private String uploadServerPath = null;
    // 视频封面图
    private File uploadVideoCoverFile = null;
    // 上传视频封面图到服务器后的路径
    private String uploadVideoCoverServerPath = null;
    // 视频封面宽
    private String videoCoverWidth = "0";
    // 视频封面高
    private String videoCoverHeight = "0";
    //媒体类型
    private String MediaType = "common";        // common || video

    private LodingView mLodingView;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_publish_dynamic;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initWidget() {

        mLodingView = new LodingView(this);
        mLodingView.setLodingText(getString(R.string.text_push_ing));

        et_content = (EditText) findViewById(R.id.et_content);
        tv_content_size = (TextView) findViewById(R.id.tv_content_size);
        iv_error = (ImageView) findViewById(R.id.iv_error);
        tv_media_path = (TextView) findViewById(R.id.tv_media_path);
        ll_media = (LinearLayout) findViewById(R.id.ll_media);
        ll_camera = (LinearLayout) findViewById(R.id.ll_camera);
        ll_ablum = (LinearLayout) findViewById(R.id.ll_ablum);
        ll_video = (LinearLayout) findViewById(R.id.ll_video);
        ll_media_type = (LinearLayout) findViewById(R.id.ll_media_type);

        iv_error.setOnClickListener(this);
        ll_camera.setOnClickListener(this);
        ll_ablum.setOnClickListener(this);
        ll_video.setOnClickListener(this);

        //输入框监听
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_content_size.setText(s.length() + "/140");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_error:
                ll_media_type.setVisibility(View.VISIBLE);
                ll_media.setVisibility(View.GONE);
                uploadFile = null;
                MediaType = "common";
                break;
            case R.id.ll_camera:
                FileHelper.getInstance().toCamera(this);
                break;
            case R.id.ll_ablum:
                FileHelper.getInstance().toAlbum(this);
                break;
            case R.id.ll_video:
                FileHelper.getInstance().toVideo(this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case FileHelper.CAMEAR_REQUEST_CODE:
                    uploadFile = FileHelper.getInstance().getTempFile();
                    ll_media_type.setVisibility(View.GONE);
                    ll_media.setVisibility(View.VISIBLE);
                    break;
                case FileHelper.ALBUM_REQUEST_CODE:
                    if (data != null) {
                        Uri uri = data.getData();
                        String path = FileHelper.getInstance().
                                getRealPathFromURI(DynamicPublishActivity.this, uri);
                        if (!TextUtils.isEmpty(path)) {
                            MediaType = "common";
                            //图片
                            tv_media_path.setText(getString(R.string.text_push_type_img));
                        }
                        uploadFile = new File(path);
                        ll_media_type.setVisibility(View.GONE);
                        ll_media.setVisibility(View.VISIBLE);
                    }
                    break;
//                case FileHelper.MUSIC_REQUEST_CODE:
                case FileHelper.VIDEO_REQUEST_CODE:
                    if (data != null) {
                        Uri uri = data.getData();
                        String path = FileHelper.getInstance().getPath(DynamicPublishActivity.this, uri);
                        // 这个获取不到路径
//                        String path = FileHelper.getInstance().
//                                getRealPathFromURI(DynamicPublishActivity.this, uri);
                        if (!TextUtils.isEmpty(path)) {
                            MediaType = "video";
                            //视频
                            tv_media_path.setText(getString(R.string.text_push_type_video));
                            uploadFile = new File(path);
                            // 视频封面
                            Bitmap bitmap = FileHelper.getVideoThumbnail(path);
                            File picFile = FileHelper.BitmapToFile(bitmap);//这里是将bitmap转为File，很多地方代码都能拷贝这一块
                            uploadVideoCoverFile = picFile;
                            videoCoverWidth = String.valueOf(bitmap.getWidth());
                            videoCoverHeight = String.valueOf(bitmap.getHeight());
//                            LogUtils.i(String.valueOf(bitmap.getHeight()));
                            ll_media_type.setVisibility(View.GONE);
                            ll_media.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.input_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_input:
                inputSquare();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 发布到广场
     */
    private void inputSquare() {
        final String content = et_content.getText().toString().trim();
        if (TextUtils.isEmpty(content) && uploadFile == null) {
            Toast.makeText(this, getString(R.string.text_push_ed_null), Toast.LENGTH_SHORT).show();
            return;
        }
        mLodingView.show();
        if (uploadFile != null) {
            //上传文件
            try{
                // 获取七牛上传token
                QiniuUploadTokenApi apiBase = new QiniuUploadTokenApi("public");
                apiBase.post(new ApiListener() {
                    @Override
                    public void success(ApiUtil api, JSONObject response) {
                        if (apiBase.mInfo.getCode().equals("0")) {
                            Configuration config = new Configuration.Builder().build();
                            UploadManager uploadManager = new UploadManager(config, 3);
                            String key = null;
                            String token = apiBase.mInfo.getToken();
                            // 上传文件
                            uploadManager.put(uploadFile, key, token,
                                new UpCompletionHandler() {
                                    @Override
                                    public void complete(String key, ResponseInfo info, JSONObject res) {
                                        mLodingView.hide();
                                        //res包含hash、key等信息，具体字段取决于上传策略的设置
                                        if(info.isOK()) {
                                            uploadServerPath = apiBase.mInfo.getDomain()+res.optString("key");
                                            // 上传视频封面
                                            if (MediaType.equals("video")) {
                                                uploadManager.put(uploadVideoCoverFile, key, token,
                                                        new UpCompletionHandler() {
                                                            @Override
                                                            public void complete(String key, ResponseInfo info, JSONObject res) {
                                                                mLodingView.hide();
                                                                //res包含hash、key等信息，具体字段取决于上传策略的设置
                                                                if(info.isOK()) {
                                                                    uploadVideoCoverServerPath = apiBase.mInfo.getDomain()+res.optString("key");
                                                                    push(content);
                                                                } else {
                                                                    Toast.makeText(DynamicPublishActivity.this, "文件上传失败", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        }, null);
                                            } else {
                                                push(content);
                                            }

                                        } else {
                                            Toast.makeText(DynamicPublishActivity.this, "文件上传失败", Toast.LENGTH_SHORT).show();
                                        }
                                        Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);
                                    }
                                }, null);
                        } else {
                            Toast.makeText(DynamicPublishActivity.this, apiBase.mInfo.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            push(content);
        }
    }

    /**
     * 发表
     *
     * @param content
     */
    private void push(String content) {
        mLodingView.hide();
        LogUtils.i("push ----------------------------");
        LogUtils.i(content);
        LogUtils.i(uploadServerPath);
        LogUtils.i(uploadVideoCoverServerPath);
        LogUtils.i(videoCoverWidth);
        LogUtils.i(videoCoverHeight);

        //发布动态
        try{
            DynamicPublishApi apiBase = new DynamicPublishApi();
            apiBase.setContent(content);
            if (MediaType.equals("common")) {
                apiBase.setType("common");
                apiBase.setImage_url(uploadServerPath);
            } else {
                apiBase.setType("video");
                apiBase.setVideo_url(uploadServerPath);
                apiBase.setVideo_cover(uploadVideoCoverServerPath);
                apiBase.setVideo_cover_height(videoCoverHeight);
                apiBase.setVideo_cover_width(videoCoverWidth);
            }
            apiBase.post(new ApiListener() {
                @Override
                public void success(ApiUtil api, JSONObject response) {
                    setResult(Activity.RESULT_OK);
                    finish();
                }
                public void error(ApiUtil api, JSONObject response) {
                    Toast.makeText(DynamicPublishActivity.this, getString(R.string.text_push_fail) , Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
