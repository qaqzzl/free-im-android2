package com.qaqzz.free_im.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qaqzz.framework.base.BaseBackActivity;
import com.qaqzz.framework.helper.FileHelper;
import com.qaqzz.framework.helper.GlideHelper;
import com.qaqzz.framework.utils.QRCodeUtils;
import com.qaqzz.framework.view.LodingView;
import com.qaqzz.free_im.R;
import com.qaqzz.free_im.api.MemberInfoApi;
import com.qaqzz.free_im.http.api.ApiListener;
import com.qaqzz.free_im.http.api.ApiUtil;

import org.json.JSONObject;

/**
 * FileName: ShareImgActivity
 * Founder: LiuGuiLin
 * Profile:
 */
public class ShareImgActivity extends BaseBackActivity implements View.OnClickListener {
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_share_img;
    }

    //头像
    private ImageView iv_photo;
    //昵称
    private TextView tv_name;
    //性别
    private ImageView iv_sex;
    //二维码
    private ImageView iv_qrcode;
    //根布局
    private LinearLayout ll_content;
    //下载
    private LinearLayout ll_download;

    private LodingView mLodingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initWidget() {

        mLodingView = new LodingView(this);
        mLodingView.setLodingText(getString(R.string.text_shar_save_ing));

        iv_photo = (ImageView) findViewById(R.id.iv_photo);
        tv_name = (TextView) findViewById(R.id.tv_name);
        iv_sex = (ImageView) findViewById(R.id.iv_sex);
        iv_qrcode = (ImageView) findViewById(R.id.iv_qrcode);
        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        ll_download = (LinearLayout) findViewById(R.id.ll_download);

        ll_download.setOnClickListener(this);

        loadInfo();
    }

    /**
     * 加载个人信息
     */
    public void loadInfo() {
        Context context = this;
        try{
            MemberInfoApi apiBase = new MemberInfoApi();
            apiBase.post(new ApiListener() {
                @Override
                public void success(ApiUtil api, JSONObject response) {
                    // 展示个人信息
                    GlideHelper.loadUrl(context, apiBase.mInfo.getAvatar(), iv_photo);
                    tv_name.setText(apiBase.mInfo.getNickname());
                    iv_sex.setImageResource(apiBase.mInfo.getAge().equals("m") ? R.drawable.img_boy_icon : R.drawable.img_girl_icon );
                    createQRCode(apiBase.mInfo.getMember_id());
                }
                @Override
                public void error(ApiUtil api, JSONObject response) {

                }
            });

        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 创建二维码
     */
    private void createQRCode(String userId) {

        /**
         * View的绘制
         */

        iv_qrcode.post(new Runnable() {
            @Override
            public void run() {
                String textContent = "Meet#" + userId;
                Bitmap mBitmap = QRCodeUtils.createQRCodeBitmap(textContent,
                        iv_qrcode.getWidth(), iv_qrcode.getHeight(),
                        "UTF-8", "H", "1", Color.BLACK, Color.WHITE);
                iv_qrcode.setImageBitmap(mBitmap);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_download:

                /**
                 * 1.View截图
                 * 2.创建一个Bitmap
                 * 3.保存到相册
                 */

                mLodingView.show();

                /**
                 * setDrawingCacheEnabled
                 * 保留我们的绘制副本
                 * 1.重新测量
                 * 2.重新布局
                 * 3.得到我们的DrawingCache
                 * 4.转换成Bitmap
                 */
                ll_content.setDrawingCacheEnabled(true);

                ll_content.measure(
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

                ll_content.layout(0, 0, ll_content.getMeasuredWidth(),
                        ll_content.getMeasuredHeight());

                Bitmap mBitmap = ll_content.getDrawingCache();

                if (mBitmap != null) {
                    FileHelper.getInstance().saveBitmapToAlbum(this, mBitmap);
                    mLodingView.hide();
                }
                break;
        }
    }
}
