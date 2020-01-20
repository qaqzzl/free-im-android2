package com.qaqzz.free_im.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.qaqzz.framework.base.BaseFragment;
import com.qaqzz.free_im.R;

/**
 * FileName: ChatRecordFragment
 * Founder: LiuGuiLin
 * Profile: 聊天记录
 */
public class ChatRecordFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private View item_empty_view;
    private RecyclerView mChatRecordView;
    private SwipeRefreshLayout mChatRecordRefreshLayout;

//    private CommonAdapter<ChatRecordModel> mChatRecordAdapter;
//    private List<ChatRecordModel> mList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_record, null);
        initView(view);
        return view;
    }

    private void initView(final View view) {

        item_empty_view = view.findViewById(R.id.item_empty_view);
        mChatRecordRefreshLayout = view.findViewById(R.id.mChatRecordRefreshLayout);
        mChatRecordView = view.findViewById(R.id.mChatRecordView);

        mChatRecordRefreshLayout.setOnRefreshListener(this);

        mChatRecordView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mChatRecordView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
//        mChatRecordAdapter = new CommonAdapter<>(mList, new CommonAdapter.OnBindDataListener<ChatRecordModel>() {
//            @Override
//            public void onBindViewHolder(final ChatRecordModel model, CommonViewHolder viewHolder, int type, int position) {
//                viewHolder.setImageUrl(getActivity(), R.id.iv_photo, model.getUrl());
//                viewHolder.setText(R.id.tv_nickname, model.getNickName());
//                viewHolder.setText(R.id.tv_content, model.getEndMsg());
//                viewHolder.setText(R.id.tv_time, model.getTime());
//
//                if(model.getUnReadSize() == 0){
//                    viewHolder.getView(R.id.tv_un_read).setVisibility(View.GONE);
//                }else{
//                    viewHolder.getView(R.id.tv_un_read).setVisibility(View.VISIBLE);
//                    viewHolder.setText(R.id.tv_un_read, model.getUnReadSize() + "");
//                }
//
//                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ChatActivity.startActivity(getActivity(),
//                                model.getUserId(),model.getNickName(),model.getUrl());
//                    }
//                });
//            }
//
//            @Override
//            public int getLayoutId(int type) {
//                return R.layout.layout_chat_record_item;
//            }
//        });
//        mChatRecordView.setAdapter(mChatRecordAdapter);

        //避免重复
        //queryChatRecord();
    }

    /**
     * 查询聊天记录
     */
    private void queryChatRecord() {

    }

    @Override
    public void onRefresh() {
        if (mChatRecordRefreshLayout.isRefreshing()) {
            queryChatRecord();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        queryChatRecord();
    }

}
