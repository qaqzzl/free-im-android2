package com.qaqzz.free_im.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qaqzz.framework.adapter.CommonAdapter;
import com.qaqzz.framework.adapter.CommonViewHolder;
import com.qaqzz.framework.base.BaseUIActivity;
import com.qaqzz.framework.entity.Constants;
import com.qaqzz.framework.helper.GlideHelper;
import com.qaqzz.framework.manager.DialogManager;
import com.qaqzz.framework.utils.SpUtils;
import com.qaqzz.framework.view.DialogView;
import com.qaqzz.framework.view.LoadingDialog;
import com.qaqzz.free_im.R;
import com.qaqzz.free_im.api.AddFriendApi;
import com.qaqzz.free_im.api.OthersHomeInfoApi;
import com.qaqzz.free_im.bean.OthersHomeInfoBean;
import com.qaqzz.free_im.http.api.ApiListener;
import com.qaqzz.free_im.http.api.ApiUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * FileName: UserInfoActivity
 * Founder: LiuGuiLin
 * Profile: 用户信息
 */
public class UserInfoActivity extends BaseUIActivity implements View.OnClickListener {

    private DialogView mAddFriendDialogView;
    private EditText et_msg;
    private TextView tv_cancel;
    private TextView tv_add_friend;
    private ImageView img_user_info_bg;

    /**
     * 1.根据传递过来的ID 查询用户信息 并且显示
     *   - 普通的信息
     *   - 构建一个RecyclerView 宫格
     * 2.建立好友关系模型
     *   与我有关系的是好友，
     *   1.在我的好友列表中
     *   2.同意了我的好友申请 BmobObject 建表
     *   3.查询所有的Friend表，其中user对应自己的列都是我的好友
     * 3.实现添加好友的提示框
     * 4.发送添加好友的消息
     *   1.自定义消息类型
     *   2.自定义协议
     *   发送文本消息 Content, 我们对文本进行处理：增加Json 定义一个标记来显示了
     *   点击提示框的发送按钮去发送
     * 5.接收好友的消息
     */

    /**
     * 跳转
     *
     * @param mContext
     * @param userId
     */
    public static void startActivity(Context mContext, String userId) {
        Intent intent = new Intent(mContext, UserInfoActivity.class);
        intent.putExtra(Constants.INTENT_USER_ID, userId);
        mContext.startActivity(intent);
    }

    private RelativeLayout ll_back;

    private CircleImageView iv_user_photo;
    private TextView tv_nickname;
    private TextView tv_desc;

    private RecyclerView mUserInfoView;
    private CommonAdapter<UserInfoModel> mUserInfoAdapter;
    private List<UserInfoModel> mUserInfoList = new ArrayList<>();

    private Button btn_add_friend;
    private Button btn_chat;

    private LinearLayout ll_is_friend;

    //个人信息颜色
    private int[] mColor = {0x881E90FF, 0x8800FF7F, 0x88FFD700, 0x88FF6347, 0x88F08080, 0x8840E0D0};

    //用户ID
    private String userId = "";

    private OthersHomeInfoBean mInfo;

    private static class UserInfoModel {
        //背景颜色
        private int bgColor;
        //标题
        private String title;
        //内容
        private String content;

        public int getBgColor() {
            return bgColor;
        }

        public void setBgColor(int bgColor) {
            this.bgColor = bgColor;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_friend_info;
    }

    protected void initWidget() {
        initAddFriendDialog();

        //获取用户ID
        userId = getIntent().getStringExtra(Constants.INTENT_USER_ID);

        ll_back = (RelativeLayout) findViewById(R.id.ll_back);
        iv_user_photo = (CircleImageView) findViewById(R.id.iv_user_photo);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        img_user_info_bg = (ImageView) findViewById(R.id.img_user_info_bg);
        mUserInfoView = (RecyclerView) findViewById(R.id.mUserInfoView);
        btn_add_friend = (Button) findViewById(R.id.btn_add_friend);
        btn_chat = (Button) findViewById(R.id.btn_chat);
        ll_is_friend = (LinearLayout) findViewById(R.id.ll_is_friend);


        ll_back.setOnClickListener(this);
        btn_add_friend.setOnClickListener(this);
        btn_chat.setOnClickListener(this);
        iv_user_photo.setOnClickListener(this);

        //列表
        mUserInfoAdapter = new CommonAdapter<>(mUserInfoList, new CommonAdapter.OnBindDataListener<UserInfoModel>() {
            @Override
            public void onBindViewHolder(UserInfoModel model, CommonViewHolder viewHolder, int type, int position) {
                //viewHolder.setBackgroundColor(R.id.ll_bg, model.getBgColor());
                viewHolder.getView(R.id.ll_bg).setBackgroundColor(model.getBgColor());
                viewHolder.setText(R.id.tv_type, model.getTitle());
                viewHolder.setText(R.id.tv_content, model.getContent());
            }

            @Override
            public int getLayoutId(int type) {
                return R.layout.layout_user_info_item;
            }
        });
        mUserInfoView.setLayoutManager(new GridLayoutManager(this, 3));
        mUserInfoView.setAdapter(mUserInfoAdapter);

        queryUserInfo();
    }

    /**
     * 添加好友的提示框
     */
    private void initAddFriendDialog() {
        mAddFriendDialogView = DialogManager.getInstance().initView(this, R.layout.dialog_send_friend);

        et_msg = (EditText) mAddFriendDialogView.findViewById(R.id.et_msg);
        tv_cancel = (TextView) mAddFriendDialogView.findViewById(R.id.tv_cancel);
        tv_add_friend = (TextView) mAddFriendDialogView.findViewById(R.id.tv_add_friend);

        String user_name = SpUtils.getInstance().getString(Constants.SP_USER_NAME, "");
        et_msg.setText(getString(R.string.text_me_info_tips) + user_name);

        tv_cancel.setOnClickListener(this);
        tv_add_friend.setOnClickListener(this);
    }

    /**
     * 查询用户信息
     */
    private void queryUserInfo() {
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        LoadingDialog loadingDialog = LoadingDialog.getInstance(this);
        loadingDialog.show();

        Context mContext = this;
        //查询他人主页用户信息
        try{
            OthersHomeInfoApi apiBase = new OthersHomeInfoApi(userId);
            apiBase.post(new ApiListener() {
                @Override
                public void success(ApiUtil api, JSONObject response) {
                    mInfo = apiBase.mInfo;
                    updateUserInfo(apiBase.mInfo);
                    //判断好友关系
                    if (apiBase.mInfo.getIs_friend().equals("yes")) {
                        //你们是好友关系
                        btn_add_friend.setVisibility(View.GONE);
                        ll_is_friend.setVisibility(View.VISIBLE);
                    }
                    loadingDialog.dismiss();
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
     * 添加好友请求
     */
    private void addFriend(String msg) {
        Context context = this;
        //添加好友请求
        try{
            AddFriendApi apiBase = new AddFriendApi(userId, msg);
            apiBase.post(new ApiListener() {
                @Override
                public void success(ApiUtil api, JSONObject response) {
                    DialogManager.getInstance().hide(mAddFriendDialogView);
                    Toast.makeText(context, apiBase.mInfo.getMessage(), Toast.LENGTH_SHORT).show();
                    //判断是否添加成功
                    if (apiBase.mInfo.getCode().equals("1")) {

                    } else {
                        // 跳转聊天页面
                    }
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
     * 更新用户信息
     *
     */
    private void updateUserInfo(OthersHomeInfoBean mInfo) {
        //设置基本属性
        GlideHelper.loadUrl(UserInfoActivity.this, mInfo.getAvatar(),
                iv_user_photo);
        tv_nickname.setText(mInfo.getNickname());
        tv_desc.setText(mInfo.getSignature());
        new Thread(new Runnable(){
            Drawable drawable = null;
            @Override
            public void run() {
                try {
                    drawable = Drawable.createFromStream(
                            new URL(mInfo.getAvatar()).openStream(), "image.jpg");
                } catch (IOException e) {
                    Log.d("test", e.getMessage());
                }
                if (drawable == null) {
                    Log.d("test", "null drawable");
                } else {
                    Log.d("test", "not null drawable");
                }
                // post() 特别关键，就是到UI主线程去更新图片
                img_user_info_bg.post(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        img_user_info_bg.setBackground(drawable);
                    }}) ;
            }

        }).start()  ;

        //性别 年龄 生日 星座
        addUserInfoModel(mColor[0], getString(R.string.text_me_info_sex), mInfo.isSex() ? getString(R.string.text_me_info_boy) : getString(R.string.text_me_info_girl));
        addUserInfoModel(mColor[1], getString(R.string.text_me_info_age), mInfo.getAge() + getString(R.string.text_search_age));
        addUserInfoModel(mColor[2], getString(R.string.text_me_info_constellation), mInfo.getConstellation());
        //刷新数据
        mUserInfoAdapter.notifyDataSetChanged();
    }

    /**
     * 添加数据
     *
     * @param color
     * @param title
     * @param content
     */
    private void addUserInfoModel(int color, String title, String content) {
        UserInfoModel model = new UserInfoModel();
        model.setBgColor(color);
        model.setTitle(title);
        model.setContent(content);
        mUserInfoList.add(model);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add_friend:
                String msg = et_msg.getText().toString().trim();
                if (TextUtils.isEmpty(msg)) {
                    msg = getString(R.string.text_user_info_add_friend);
                    return;
                }
                addFriend(msg);
                break;
            case R.id.tv_cancel:
                DialogManager.getInstance().hide(mAddFriendDialogView);
                break;
            case R.id.ll_back:
                finish();
                break;
            case R.id.iv_user_photo:        // 点击头像
//                ImagePreviewActivity.startActivity(this, true, imUser.getPhoto());
                break;
            case R.id.btn_add_friend:
                DialogManager.getInstance().show(mAddFriendDialogView);
                break;
            case R.id.btn_chat:     // 点击聊天
                ChatActivity.startActivity(UserInfoActivity.this,
                        userId, mInfo.getNickname(), mInfo.getAvatar());
                break;
            case R.id.btn_delete_friend:   // 删除好友
//                if (!checkWindowPermissions()) {
//                    requestWindowPermissions();
//                } else {
//                    CloudManager.getInstance().startVideoCall(this, userId);
//                }
                break;
        }
    }


}
