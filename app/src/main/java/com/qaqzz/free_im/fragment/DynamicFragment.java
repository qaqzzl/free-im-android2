package com.qaqzz.free_im.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.qaqzz.framework.adapter.CommonAdapter;
import com.qaqzz.framework.adapter.CommonViewHolder;
import com.qaqzz.framework.base.BaseFragment;
import com.qaqzz.framework.utils.CommonUtils;
import com.qaqzz.framework.view.VideoJzvdStd;
import com.qaqzz.free_im.R;
import com.qaqzz.free_im.activities.DynamicPublishActivity;
import com.qaqzz.free_im.activities.UserInfoActivity;
import com.qaqzz.free_im.api.DynamicListApi;
import com.qaqzz.free_im.bean.DynamicListBean;
import com.qaqzz.free_im.http.api.ApiListener;
import com.qaqzz.free_im.http.api.ApiUtil;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import cn.jzvd.Jzvd;


/**
 * FileName: DynamicFragment
 * Founder: LiuGuiLin
 * Profile: 动态
 */
public class DynamicFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    /**
     * 1.设计并且实现云数据库 SquareSet
     * 2.实现我们的媒体发送 PsuhSquareActivity
     * 3.实现列表 并且实现我们的文本和图片的发送
     */

    private static final int REQUEST_CODE = 1000;

    private ImageView iv_push;
    private RecyclerView mSquareView;
    private SwipeRefreshLayout mSquareSwipeLayout;
    private View item_empty_view;

    //悬浮按钮
    private FloatingActionButton fb_squaue_top;

    private List<DynamicListBean.ListBean> mList = new ArrayList<>();
    private CommonAdapter<DynamicListBean.ListBean> mDynamicAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_square, null);
        initView(view);
        return view;
    }

    /**
     * 初始化View
     *
     * @param view
     */
    private void initView(final View view) {
        iv_push = view.findViewById(R.id.iv_push);
        mSquareView = view.findViewById(R.id.mSquareView);
        mSquareSwipeLayout = view.findViewById(R.id.mSquareSwipeLayout);
        item_empty_view = view.findViewById(R.id.item_empty_view);
        fb_squaue_top = view.findViewById(R.id.fb_squaue_top);

        iv_push.setOnClickListener(this);
        fb_squaue_top.setOnClickListener(this);
        mSquareSwipeLayout.setOnRefreshListener(this);

        mSquareView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSquareView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        //取消动画
        ((SimpleItemAnimator) mSquareView.getItemAnimator()).setSupportsChangeAnimations(false);

        Context context = this.getContext();

        mDynamicAdapter = new CommonAdapter<>(mList, new CommonAdapter.OnMoreBindDataListener<DynamicListBean.ListBean>() {
            @Override
            public int getItemType(int position) {
                return position;
            }

            @Override
            public void onBindViewHolder(final DynamicListBean.ListBean model, CommonViewHolder viewHolder, int type, int position) {
                //加载个人信息
                //loadMeInfo(model.getUserId(), viewHolder);
                viewHolder.setImageUrl(getActivity(), R.id.iv_photo, model.getAvatar(), 50, 50);
                viewHolder.setText(R.id.tv_nickname, model.getNickname());
//                viewHolder.setText(R.id.tv_square_age, imUser.getAge() + getString(R.string.text_search_age));
//                viewHolder.setText(R.id.tv_square_constellation, constellation);
//                viewHolder.setVisibility(R.id.tv_square_constellation, View.VISIBLE);
//                viewHolder.setText(R.id.tv_square_hobby, getString(R.string.text_squate_love) + hobby);
//                viewHolder.setVisibility(R.id.tv_square_hobby, View.VISIBLE);
//                viewHolder.setText(R.id.tv_square_status, imUser.getStatus());
//                viewHolder.setVisibility(R.id.tv_square_status, View.VISIBLE);

                //设置时间
                viewHolder.setText(R.id.tv_time, model.getCreated_at());

                //设置头像点击事件
                viewHolder.getView(R.id.iv_photo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserInfoActivity.startActivity(getActivity(), model.getMember_id());
                    }
                });
                if (!TextUtils.isEmpty(model.getContent())) {
                    viewHolder.setText(R.id.tv_text, model.getContent());
                } else {
                    viewHolder.setVisibility(R.id.tv_text, View.GONE);
                }

                //多媒体
                switch (model.getType()) {
                    case "common":
                        if (model.getImage_url().isEmpty()) {
                            goneItemView(viewHolder, false, false);
                        } else {
                            goneItemView(viewHolder, true, false);
                            viewHolder.setImageUrl(getActivity(), R.id.iv_img, model.getImage_url());
                            viewHolder.getView(R.id.iv_img).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                                    ImagePreviewActivity.startActivity(getActivity(), true, model.getMediaUrl());
                                }
                            });
                        }
                        break;
                    case "video":
                        goneItemView(viewHolder, false, true);
                        //实现我们的视频
                        final VideoJzvdStd jzvdStd = viewHolder.getView(R.id.jz_video);
                        jzvdStd.setUp(model.getVideo_url(), "");        // title 是视频标题
                        Glide.with(context).load(model.getVideo_cover()).into(jzvdStd.thumbImageView);   //推荐使用Glide

                        break;
                }

            }

            @Override
            public int getLayoutId(int type) {
                return R.layout.layou_square_item;
            }
        });
        mSquareView.setAdapter(mDynamicAdapter);

        //监听列表滑动
        mSquareView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                        int position = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                        if(position > 5 ){
                            fb_squaue_top.setVisibility(View.VISIBLE);
                        }else {
                            fb_squaue_top.setVisibility(View.GONE);
                        }
                    }
                }

                // RecyclerView划出屏幕释放JZ，同时也是不开启列表划出显示小窗
                recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
                    @Override
                    public void onChildViewAttachedToWindow(View view) {

                    }

                    @Override
                    public void onChildViewDetachedFromWindow(View view) {
//                        Jzvd jzvd = view.findViewById(R.id.jz_video);
                        Jzvd.releaseAllVideos();
//                        if (jzvd != null && jzvd.jzDataSource.containsTheUrl(jzvd.JZMediaManager.getCurrentUrl())) {
//                            Jzvd currentJzvd = JzvdMgr.getCurrentJzvd();
//                            if (currentJzvd != null && currentJzvd.currentScreen != Jzvd.SCREEN_WINDOW_FULLSCREEN) {
//                                Jzvd.releaseAllVideos();
//                            }
//                        }
                    }
                });

            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            if (!CommonUtils.isEmpty(mList)) {
                loadSquare();
            }
        }
    }

    /**
     * 隐藏View
     *
     * @param viewHolder
     * @param img
     * @param video
     */
    private void goneItemView(CommonViewHolder viewHolder,
                              boolean img, boolean video) {
        viewHolder.getView(R.id.tv_text).setVisibility(View.VISIBLE);
        viewHolder.getView(R.id.iv_img).setVisibility(img ? View.VISIBLE : View.GONE);
        viewHolder.getView(R.id.ll_video).setVisibility(video ? View.VISIBLE : View.GONE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_push:
                Intent intent = new Intent(getActivity(), DynamicPublishActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.fb_squaue_top:
                mSquareView.smoothScrollToPosition(0);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                //刷新
                loadSquare();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 加载数据
     */
    private void loadSquare() {
        mSquareSwipeLayout.setRefreshing(true);
        Context context = this.getContext();
        //发布列表
        try{
            DynamicListApi apiBase = new DynamicListApi("1","10");
            apiBase.post(new ApiListener() {
                @Override
                public void success(ApiUtil api, JSONObject response) {
                    mSquareSwipeLayout.setRefreshing(false);
                    List<DynamicListBean.ListBean> list = apiBase.mInfo.getList();
                    if (CommonUtils.isEmpty(list)) {
                        mSquareView.setVisibility(View.VISIBLE);
                        item_empty_view.setVisibility(View.GONE);
                        if (mList.size() > 0) {
                            mList.clear();
                        }
                        mList.addAll(list);
                        mDynamicAdapter.notifyDataSetChanged();
                    } else {
                        mSquareView.setVisibility(View.GONE);
                        item_empty_view.setVisibility(View.VISIBLE);
                    }
                }
                public void error(ApiUtil api, JSONObject response) {
                    try {
                        Log.i("response", response.getString("msg"));
                    }catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    mSquareSwipeLayout.setRefreshing(false);
                    mSquareView.setVisibility(View.GONE);
                    item_empty_view.setVisibility(View.VISIBLE);
                    Toast.makeText(context, "加载失败" , Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        loadSquare();
    }
}