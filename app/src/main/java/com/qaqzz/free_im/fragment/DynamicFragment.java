package com.qaqzz.free_im.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.qaqzz.framework.view.LoadingDialog;
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
     * .实现列表 并且实现我们的文本和图片的发送
     */
    private LoadingDialog loadingDialog;


    private static final int REQUEST_CODE = 1000;

    private ImageView iv_push;
    private RecyclerView mDynamicView;
    private SwipeRefreshLayout mSquareSwipeLayout;
    private View item_empty_view;

    //悬浮按钮
    private FloatingActionButton fb_squaue_top;

    private List<DynamicListBean.ListBean> mList = new ArrayList<>();
    private CommonAdapter<DynamicListBean.ListBean> mDynamicAdapter;

    // 分页
    private int current_page = 1;
    private final int per_page = 10;
    // 判断是否加载中
    private boolean isLoading = false;
    // 最后一个条目位置
    private LinearLayoutManager mLayoutManager;
    private static int lastVisibleItem = 0;
    // 若是上拉加载更多的网络请求 则不需要删除数据
    private boolean isLoadingMore = false;

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
        loadingDialog = LoadingDialog.getInstance(this.getContext());

        iv_push = view.findViewById(R.id.iv_push);
        mDynamicView = view.findViewById(R.id.mSquareView);
        mSquareSwipeLayout = view.findViewById(R.id.mSquareSwipeLayout);
        item_empty_view = view.findViewById(R.id.item_empty_view);
        fb_squaue_top = view.findViewById(R.id.fb_squaue_top);

        iv_push.setOnClickListener(this);
        fb_squaue_top.setOnClickListener(this);
        mSquareSwipeLayout.setOnRefreshListener(this);

        mDynamicView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDynamicView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        //取消动画
        ((SimpleItemAnimator) mDynamicView.getItemAnimator()).setSupportsChangeAnimations(false);

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
                viewHolder.setText(R.id.tv_square_age, model.getAge() + getString(R.string.text_search_age));
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
        mDynamicView.setAdapter(mDynamicAdapter);

        //监听列表滑动
        mDynamicView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            //滚动监听
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    //判断是当前layoutManager是否为LinearLayoutManager
                    // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                    if (layoutManager instanceof LinearLayoutManager) {
                        mLayoutManager = (LinearLayoutManager) layoutManager;
                        //获取最后一个可见view的位置
                        lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                        System.out.println(lastVisibleItem);
                    }
            }

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
                // 分页
                if(!isLoading){        // 若不是加载更多 才 加载
                    // 在newState为滑到底部时
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        int index = 3;      // 正确值应该是2  改成3 实现拉到底部的上一条就加载
                        // 如果没有隐藏footView，那么最后一个条目的位置(带数据）就比我们的getItemCount少1
                        if (!mDynamicAdapter.isFadeTips() && lastVisibleItem + index - 1 >= mDynamicAdapter.getItemCount()) {
                            // 然后调用updateRecyclerview方法更新RecyclerView
                            loadSquare();
                        }
                        // 如果隐藏了提示条，我们又上拉加载时，那么最后一个条目(带数据）就要比getItemCount要少2
                        if (mDynamicAdapter.isFadeTips() && lastVisibleItem + index >= mDynamicAdapter.getItemCount()) {
                            // 然后调用updateRecyclerview方法更新RecyclerView
                            loadSquare();    // 要调
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
                mDynamicView.smoothScrollToPosition(0);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                //刷新
                if (mList.size() > 0) {
                    mList.clear();
                }
                current_page = 1;
                loadSquare();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 加载数据
     */
    private void loadSquare() {
        isLoading = true;
        Log.d("进入loadSquare","page: " + String.valueOf(current_page));
        Context context = this.getContext();
        //发布列表
        try{
            DynamicListApi apiBase = new DynamicListApi(current_page, per_page);

            apiBase.post(new ApiListener() {
                @Override
                public void success(ApiUtil api, JSONObject response) {
                    mSquareSwipeLayout.setRefreshing(false);
                    List<DynamicListBean.ListBean> list = apiBase.mInfo.getList();
                    if (isLoadingMore && mList.size() > 0) {
                        mList.clear();
                    }
                    if (CommonUtils.isEmpty(list)) {
                        mDynamicView.setVisibility(View.VISIBLE);
                        item_empty_view.setVisibility(View.GONE);
                        mList.addAll(list);
                        mDynamicAdapter.notifyDataSetChanged();
                        isLoadingMore = false;
                        isLoading = false;
                        current_page++;
                    } else {
                        if (mList.size() == 0) {
                            mDynamicView.setVisibility(View.GONE);
                            item_empty_view.setVisibility(View.VISIBLE);
                        } else {
                            // 没有跟多数据了
                        }
                    }
                }
                public void error(ApiUtil api, JSONObject response) {
                    try {
                        Log.i("response", response.getString("msg"));
                    }catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    mSquareSwipeLayout.setRefreshing(false);
                    mDynamicView.setVisibility(View.GONE);
                    item_empty_view.setVisibility(View.VISIBLE);
                    Toast.makeText(context, "加载失败" , Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    // 上拉刷新
    public void onRefresh() {
        isLoadingMore = true;
        current_page = 1;
        loadSquare();
    }

}