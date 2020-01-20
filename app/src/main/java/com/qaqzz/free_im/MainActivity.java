package com.qaqzz.free_im;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qaqzz.framework.base.BaseActivity;
import com.qaqzz.framework.entity.Constants;
import com.qaqzz.framework.utils.LogUtils;
import com.qaqzz.framework.utils.SpUtils;
import com.qaqzz.free_im.activities.LoginActivity;
import com.qaqzz.free_im.fragment.ChatRecordFragment;
import com.qaqzz.free_im.fragment.FriendFragment;
import com.qaqzz.free_im.fragment.MeFragment;
import com.qaqzz.free_im.fragment.SquareFragment;
import com.qaqzz.free_im.http.Util.Util;
import com.qaqzz.free_im.im.SocketIm;

import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    //聊天
    private ImageView iv_chat;
    private TextView tv_chat;
    private LinearLayout ll_chat;
    private ChatRecordFragment mChatRecordFragment = null;
    private FragmentTransaction mChatRecordTransaction = null;

    //好友
    private ImageView iv_friend;
    private TextView tv_friend;
    private LinearLayout ll_friend;
    private FriendFragment mFriendFragment = null;
    private FragmentTransaction mFriendTransaction = null;

    //广场
    private ImageView iv_square;
    private TextView tv_square;

    private LinearLayout ll_square;
    private SquareFragment mSquareFragment = null;
    private FragmentTransaction mSquareTransaction = null;

    //我的
    private ImageView iv_me;
    private TextView tv_me;
    private LinearLayout ll_me;
    private MeFragment mMeFragment = null;
    private FragmentTransaction mMeTransaction = null;

//    private Disposable disposable;
    /**
     * 1.初始化Frahment
     * 2.显示Fragment
     * 3.隐藏所有的Fragment
     * 4.恢复Fragment
     * 优化的手段
     */

//    private DialogView mUploadView;


//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initWidget() {
        super.initWidget();

        // 请求权限
        requestPermiss();

        // 获取IMEI，国际移动设备标志
        SpUtils.getInstance().putString(Constants.SP_DEVICEID, Util.getImei(this));
        Log.d("SOCKET", "获取IMEI，国际移动设备标志: " + Util.getImei(this));

        // im
        SocketIm socketIm = new SocketIm();
        socketIm.start();

        iv_friend = (ImageView) findViewById(R.id.iv_friend);
        tv_friend = (TextView) findViewById(R.id.tv_friend);
        ll_friend = (LinearLayout) findViewById(R.id.ll_friend);

        iv_square = (ImageView) findViewById(R.id.iv_square);
        tv_square = (TextView) findViewById(R.id.tv_square);
        ll_square = (LinearLayout) findViewById(R.id.ll_square);

        iv_chat = (ImageView) findViewById(R.id.iv_chat);
        tv_chat = (TextView) findViewById(R.id.tv_chat);
        ll_chat = (LinearLayout) findViewById(R.id.ll_chat);

        iv_me = (ImageView) findViewById(R.id.iv_me);
        tv_me = (TextView) findViewById(R.id.tv_me);
        ll_me = (LinearLayout) findViewById(R.id.ll_me);

        ll_friend.setOnClickListener(this);
        ll_square.setOnClickListener(this);
        ll_chat.setOnClickListener(this);
        ll_me.setOnClickListener(this);

        //设置文本
        tv_friend.setText(getString(R.string.text_main_friend));
        tv_square.setText(getString(R.string.text_main_square));
        tv_chat.setText(getString(R.string.text_main_chat));
        tv_me.setText(getString(R.string.text_main_me));

        initFragment();

        //切换默认的选项卡
        checkMainTab(0);

        //检查TOKEN
        checkToken();
    }


    /**
     * 初始化Fragment
     */
    private void initFragment() {

        //聊天
        if (mChatRecordFragment == null) {
            mChatRecordFragment = new ChatRecordFragment();
            mChatRecordTransaction = getSupportFragmentManager().beginTransaction();
            mChatRecordTransaction.add(R.id.mMainLayout, mChatRecordFragment);
            mChatRecordTransaction.commit();
        }

        //好友
        if (mFriendFragment == null) {
            mFriendFragment = new FriendFragment();
            mFriendTransaction = getSupportFragmentManager().beginTransaction();
            mFriendTransaction.add(R.id.mMainLayout, mFriendFragment);
            mFriendTransaction.commit();
        }

        //广场
        if (mSquareFragment == null) {
            mSquareFragment = new SquareFragment();
            mSquareTransaction = getSupportFragmentManager().beginTransaction();
            mSquareTransaction.add(R.id.mMainLayout, mSquareFragment);
            mSquareTransaction.commit();
        }


        //我的
        if (mMeFragment == null) {
            mMeFragment = new MeFragment();
            mMeTransaction = getSupportFragmentManager().beginTransaction();
            mMeTransaction.add(R.id.mMainLayout, mMeFragment);
            mMeTransaction.commit();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_chat:
                checkMainTab(0);
                break;
            case R.id.ll_friend:
                checkMainTab(1);
                break;
            case R.id.ll_square:
                checkMainTab(2);
                break;
            case R.id.ll_me:
                checkMainTab(3);
                break;

        }
    }

    /**
     * 显示Fragment
     *
     * @param fragment
     */
    private void showFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            hideAllFragment(transaction);
            transaction.show(fragment);
            transaction.commitAllowingStateLoss();
        }
    }

    /**
     * 隐藏所有的Fragment
     *
     * @param transaction
     */
    private void hideAllFragment(FragmentTransaction transaction) {
        if (mChatRecordFragment != null) {
            transaction.hide(mChatRecordFragment);
        }
        if (mFriendFragment != null) {
            transaction.hide(mFriendFragment);
        }
        if (mSquareFragment != null) {
            transaction.hide(mSquareFragment);
        }
        if (mMeFragment != null) {
            transaction.hide(mMeFragment);
        }
    }

    /**
     * 切换主页选项卡
     *
     * @param index 0：聊天记录
     *              1：好友
     *              2：广场
     *              3：我的
     */
    private void checkMainTab(int index) {
        switch (index) {
            case 0:
                showFragment(mChatRecordFragment);

                iv_chat.setImageResource(R.drawable.img_chat_p);
                iv_friend.setImageResource(R.drawable.img_star);
                iv_square.setImageResource(R.drawable.img_square);
                iv_me.setImageResource(R.drawable.img_me);

                tv_friend.setTextColor(Color.BLACK);
                tv_square.setTextColor(Color.BLACK);
                tv_chat.setTextColor(getResources().getColor(R.color.colorAccent));
                tv_me.setTextColor(Color.BLACK);

                break;
            case 1:
                showFragment(mFriendFragment);

                iv_chat.setImageResource(R.drawable.img_chat);
                iv_friend.setImageResource(R.drawable.img_star_p);
                iv_square.setImageResource(R.drawable.img_square);
                iv_me.setImageResource(R.drawable.img_me);

                tv_friend.setTextColor(getResources().getColor(R.color.colorAccent));
                tv_square.setTextColor(Color.BLACK);
                tv_chat.setTextColor(Color.BLACK);
                tv_me.setTextColor(Color.BLACK);
                break;
            case 2:
                showFragment(mSquareFragment);

                iv_friend.setImageResource(R.drawable.img_star);
                iv_square.setImageResource(R.drawable.img_square_p);
                iv_chat.setImageResource(R.drawable.img_chat);
                iv_me.setImageResource(R.drawable.img_me);

                tv_friend.setTextColor(Color.BLACK);
                tv_square.setTextColor(getResources().getColor(R.color.colorAccent));
                tv_chat.setTextColor(Color.BLACK);
                tv_me.setTextColor(Color.BLACK);

                break;
            case 3:
                showFragment(mMeFragment);

                iv_chat.setImageResource(R.drawable.img_chat);
                iv_friend.setImageResource(R.drawable.img_star);
                iv_square.setImageResource(R.drawable.img_square);
                iv_me.setImageResource(R.drawable.img_me_p);

                tv_friend.setTextColor(Color.BLACK);
                tv_square.setTextColor(Color.BLACK);
                tv_chat.setTextColor(Color.BLACK);
                tv_me.setTextColor(getResources().getColor(R.color.colorAccent));

                break;
        }
    }

    /**
     * 检查TOKEN
     */
    private void checkToken() {
        LogUtils.i("checkToken");
        //获取TOKEN , USERID
        String token = SpUtils.getInstance().getString(Constants.SP_TOKEN, "");
        String uid = SpUtils.getInstance().getString(Constants.SP_USERID, "");
//        Toast.makeText(this, "token:" + token + ", uid:" + uid, Toast.LENGTH_SHORT).show();
        if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(uid)) {

        } else {
            Toast.makeText(this,"请登录",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }

    /**
     * 请求权限
     */
    private void requestPermiss() {
        //危险权限
        request(new OnPermissionsResult() {
            @Override
            public void OnSuccess() {

            }

            @Override
            public void OnFail(List<String> noPermissions) {
                LogUtils.i("noPermissions:" + noPermissions.toString());
            }
        });
    }
}
