package com.qaqzz.free_im.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qaqzz.framework.adapter.CommonAdapter;
import com.qaqzz.framework.adapter.CommonViewHolder;
import com.qaqzz.framework.base.BaseBackActivity;
import com.qaqzz.framework.utils.CommonUtils;
import com.qaqzz.framework.utils.LogUtils;
import com.qaqzz.free_im.R;
import com.qaqzz.free_im.api.FriendApplyActionApi;
import com.qaqzz.free_im.api.FriendApplyListApi;
import com.qaqzz.free_im.bean.FriendApplyListBean;
import com.qaqzz.free_im.http.api.ApiListener;
import com.qaqzz.free_im.http.api.ApiUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * FileName: NewFriendActivity
 * Founder: LiuGuiLin
 * Profile: 新朋友
 */
public class NewFriendActivity extends BaseBackActivity {
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_new_friend;
    }

    private ViewStub item_empty_view;
    private RecyclerView mNewFriendView;

    private CommonAdapter<FriendApplyListBean.ApplyListBean> mNewFriendAdapter;
    private List<FriendApplyListBean.ApplyListBean> mList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initWidget() {

        mNewFriendView = (RecyclerView) findViewById(R.id.mNewFriendView);

        mNewFriendView.setLayoutManager(new LinearLayoutManager(this));
        mNewFriendView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        mNewFriendAdapter = new CommonAdapter<>(mList, new CommonAdapter.OnBindDataListener<FriendApplyListBean.ApplyListBean>() {
            @Override
            public void onBindViewHolder(final FriendApplyListBean.ApplyListBean model, final CommonViewHolder viewHolder, int type, final int position) {
                //填充具体属性
                viewHolder.setImageUrl(NewFriendActivity.this, R.id.iv_photo,
                        model.getAvatar());
                viewHolder.setImageResource(R.id.iv_sex, model.getGender()=="w" ?
                        R.drawable.img_girl_icon : R.drawable.img_boy_icon );
                viewHolder.setText(R.id.tv_nickname, model.getNickname());
                viewHolder.setText(R.id.tv_age, "");            // 年龄
                viewHolder.setText(R.id.tv_desc, model.getSignature());
                viewHolder.setText(R.id.tv_msg, "验证信息: "+model.getRemark());

                if (model.getStatus().equals("1")) {
                    viewHolder.getView(R.id.ll_agree).setVisibility(View.GONE);
                    viewHolder.getView(R.id.tv_result).setVisibility(View.VISIBLE);
                    viewHolder.setText(R.id.tv_result, getString(R.string.text_new_friend_agree));
                } else if (model.getStatus().equals("2")) {
                    viewHolder.getView(R.id.ll_agree).setVisibility(View.GONE);
                    viewHolder.getView(R.id.tv_result).setVisibility(View.VISIBLE);
                    viewHolder.setText(R.id.tv_result, getString(R.string.text_new_friend_no_agree));
                }

                //同意
                viewHolder.getView(R.id.ll_yes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        friendApplyAction(model.getId(),"1");
                    }
                });

                //拒绝
                viewHolder.getView(R.id.ll_no).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        friendApplyAction(model.getId(),"2");
                    }
                });
            }

            @Override
            public int getLayoutId(int type) {
                return R.layout.layout_new_friend_item;
            }
        });
        mNewFriendView.setAdapter(mNewFriendAdapter);

        queryNewFriend();
    }

    /**
     * 查询新朋友
     */
    private void queryNewFriend() {
        try{
            FriendApplyListApi apiBase = new FriendApplyListApi();
            apiBase.post(new ApiListener() {
                @Override
                public void success(ApiUtil api, JSONObject response) {
                    if (CommonUtils.isEmpty(apiBase.mInfo.getApply_list())) {
                        if (mList.size() > 0) {
                            mList.clear();
                        }
                        mList.addAll(apiBase.mInfo.getApply_list());
                        mNewFriendAdapter.notifyDataSetChanged();
                    } else {
                        showViewStub();
                        mNewFriendView.setVisibility(View.GONE);
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

    // 好友申请操作, 同意|拒绝
    private void friendApplyAction(String id, String action) {
        Context context = this;
        try{
            FriendApplyActionApi apiBase = new FriendApplyActionApi(id, action);
            apiBase.post(new ApiListener() {
                @Override
                public void success(ApiUtil api, JSONObject response) {
                    LogUtils.i("请求成功");
                    if (apiBase.mInfo.getCode().equals("0")) {
                        queryNewFriend();
                        mNewFriendAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, apiBase.mInfo.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void error(ApiUtil api, JSONObject response) {
                    LogUtils.i("请求失败");
                }
            });

        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 显示懒加载布局
     */
    private void showViewStub(){
        item_empty_view = findViewById(R.id.item_empty_view);
        item_empty_view.inflate();
    }
}
