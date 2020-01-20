package com.qaqzz.free_im.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.qaqzz.free_im.http.api.ApiListener;
import com.qaqzz.free_im.http.api.ApiUtil;
import com.qaqzz.framework.adapter.CommonAdapter;
import com.qaqzz.framework.adapter.CommonViewHolder;
import com.qaqzz.framework.base.BaseFragment;
import com.qaqzz.framework.entity.Constants;
import com.qaqzz.framework.event.EventManager;
import com.qaqzz.framework.event.MessageEvent;
import com.qaqzz.framework.utils.CommonUtils;
import com.qaqzz.framework.utils.SpUtils;
import com.qaqzz.free_im.R;
import com.qaqzz.free_im.activities.UserInfoActivity;
import com.qaqzz.free_im.api.FriendListApi;
import com.qaqzz.free_im.bean.FriendListBean;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * FileName: AllFriendFragment
 * Founder: LiuGuiLin
 * Profile: 所有联系人
 */
public class FriendFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private View item_empty_view;
    private RecyclerView mAllFriendView;
    private SwipeRefreshLayout mAllFriendRefreshLayout;

    private CommonAdapter<FriendListBean.FriendBean> mAllFriendAdapter;
    private List<FriendListBean.FriendBean> mList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_friend_record, null);
        initView(view);
        return view;
    }

    private void initView(final View view) {

        item_empty_view = view.findViewById(R.id.item_empty_view);
        mAllFriendView = view.findViewById(R.id.mAllFriendView);
        mAllFriendRefreshLayout = view.findViewById(R.id.mAllFriendRefreshLayout);

        mAllFriendRefreshLayout.setOnRefreshListener(this);

        mAllFriendView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAllFriendView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));

        mAllFriendAdapter = new CommonAdapter<>(mList, new CommonAdapter.OnBindDataListener<FriendListBean.FriendBean>() {
            @Override
            public void onBindViewHolder(final FriendListBean.FriendBean model, CommonViewHolder viewHolder, int type, int position) {
                viewHolder.setImageUrl(getActivity(), R.id.iv_photo, model.getAvatar());
                viewHolder.setText(R.id.tv_nickname, model.getNickname());
                viewHolder.setImageResource(R.id.iv_sex,
                        model.isSex() ? R.drawable.img_boy_icon : R.drawable.img_girl_icon );
                viewHolder.setText(R.id.tv_desc, model.getSignature());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {       // 点击详情
                    @Override
                    public void onClick(View v) {
                        UserInfoActivity.startActivity(getActivity(),model.getMember_id());
                    }
                });
            }

            @Override
            public int getLayoutId(int type) {
                return R.layout.layout_all_friend_item;
            }
        });
        mAllFriendView.setAdapter(mAllFriendAdapter);

        queryMyFriends();
    }

    /**
     * 查询所有好友
     */
    private void queryMyFriends() {
        mAllFriendRefreshLayout.setRefreshing(true);        // 加载按钮

        try{
            String token = SpUtils.getInstance().getString(Constants.SP_TOKEN, "");
            String uid = SpUtils.getInstance().getString(Constants.SP_USERID, "");
            FriendListApi apiBase = new FriendListApi(uid, token);
            apiBase.post(new ApiListener() {
                @Override
                public void success(ApiUtil api, JSONObject response) {
//                    Log.d("friend.list", "friend_list+++" + CommonUtils.isEmpty(apiBase.mInfo.getFriend_list()));
                    mAllFriendRefreshLayout.setRefreshing(false);   // 关闭加载按钮
                    if (CommonUtils.isEmpty(apiBase.mInfo.getFriend_list())) {
                        item_empty_view.setVisibility(View.GONE);
                        mAllFriendView.setVisibility(View.VISIBLE);
                        if (mList.size() > 0) {
                            mList.clear();
                        }
                        List<FriendListBean.FriendBean> list = apiBase.mInfo.getFriend_list();
                        for (int i = 0; i < list.size(); i++) {
                            FriendListBean.FriendBean FriendBean = list.get(i);
                            FriendListBean.FriendBean model = new FriendListBean.FriendBean();
                            model.setSignature(FriendBean.getSignature());
                            model.setNickname(FriendBean.getNickname());
                            model.setMember_id(FriendBean.getMember_id());
                            model.setGender(FriendBean.getGender());
                            model.setAvatar(FriendBean.getAvatar());
                            mList.add(model);
                            mAllFriendAdapter.notifyDataSetChanged();
                        }
//                        mList = apiBase.mInfo.getFriend_list();
//                        mAllFriendAdapter.notifyDataSetChanged();
                    } else {
                        item_empty_view.setVisibility(View.VISIBLE);
                        mAllFriendView.setVisibility(View.GONE);
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

    @Override
    public void onRefresh() {
        if (mAllFriendRefreshLayout.isRefreshing()) {
            queryMyFriends();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getType()) {
            case EventManager.FLAG_UPDATE_FRIEND_LIST:
                if (!mAllFriendRefreshLayout.isRefreshing()) {
                    queryMyFriends();
                }
                break;
        }
    }
}
