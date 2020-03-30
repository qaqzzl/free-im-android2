package com.qaqzz.free_im.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.qaqzz.framework.adapter.CommonAdapter;
import com.qaqzz.framework.adapter.CommonViewHolder;
import com.qaqzz.framework.base.BaseFragment;
import com.qaqzz.framework.entity.Constants;
import com.qaqzz.framework.event.EventManager;
import com.qaqzz.framework.event.MessageEvent;
import com.qaqzz.framework.utils.LogUtils;
import com.qaqzz.framework.utils.SpUtils;
import com.qaqzz.free_im.R;
import com.qaqzz.free_im.activities.AddFriendActivity;
import com.qaqzz.free_im.activities.ChatActivity;
import com.qaqzz.free_im.database.ChatRecord;
import com.qaqzz.free_im.database.ChatRecordDao;
import com.qaqzz.free_im.database.DaoManager;
import com.qaqzz.free_im.database.Message;
import com.qaqzz.free_im.database.MessageDao;
import com.qaqzz.free_im.database.MessageDaoUtils;
import com.qaqzz.free_im.im.MessageStruct;
import com.qaqzz.free_im.im.SocketService;
import com.qaqzz.free_im.model.ChatRecordModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * FileName: ChatRecordFragment
 * Founder: LiuGuiLin
 * Profile: 聊天记录
 */
public class ChatRecordFragment extends BaseFragment  implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private View item_empty_view;
    private RecyclerView mChatRecordView;
    private SwipeRefreshLayout mChatRecordRefreshLayout;

    private ImageView iv_camera;        // 扫一扫
    private ImageView iv_add;

    private CommonAdapter<ChatRecordModel> mChatRecordAdapter;
    private List<ChatRecordModel> mList = new ArrayList<>();

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
//        iv_camera = view.findViewById(R.id.iv_camera);
        iv_add = view.findViewById(R.id.iv_add);

//        iv_camera.setOnClickListener(this);
        iv_add.setOnClickListener(this);
        mChatRecordRefreshLayout.setOnRefreshListener(this);

        mChatRecordView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mChatRecordView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        mChatRecordAdapter = new CommonAdapter<>(mList, new CommonAdapter.OnBindDataListener<ChatRecordModel>() {
            @Override
            public void onBindViewHolder(final ChatRecordModel model, CommonViewHolder viewHolder, int type, int position) {
                viewHolder.setImageUrl(getActivity(), R.id.iv_photo, model.getAvatar());
                viewHolder.setText(R.id.tv_nickname, model.getName());
                viewHolder.setText(R.id.tv_content, model.getEndMsg());
                viewHolder.setText(R.id.tv_time, model.getEndMsgTime());

                if(model.getUnReadSize() == 0){
                    viewHolder.getView(R.id.tv_un_read).setVisibility(View.GONE);
                }else{
                    viewHolder.getView(R.id.tv_un_read).setVisibility(View.VISIBLE);
                    viewHolder.setText(R.id.tv_un_read, model.getUnReadSize() + "");
                }

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {       // 点击跳转
                        switch (model.getType()) {
                            case "common":
                                ChatActivity.startChatRecordActivity(getActivity(),
                                        model.getChatroomId(),model.getName(),model.getAvatar());
                                break;
                        }
                    }
                });
            }

            @Override
            public int getLayoutId(int type) {
                return R.layout.layout_chat_record_item;
            }
        });
        mChatRecordView.setAdapter(mChatRecordAdapter);

        //避免重复
        queryChatRecord(true);
    }

    /**
     * 查询聊天记录
     */
    private void queryChatRecord(boolean setRefreshing ) {
        //mChatRecordRefreshLayout.setRefreshing(setRefreshing);

        LogUtils.i("onSuccess");

        if (mList.size() > 0) {
            mList.clear();
        }

        // 查询聊天会话数据
        DaoManager mManager = new DaoManager();
        mManager.init(this.getContext(), ChatRecordDao.TABLENAME);
        List<ChatRecord> chatRecordList = mManager.getDaoSession().getChatRecordDao().queryBuilder().list();
        if (chatRecordList.size() > 0) {
            for (int i = 0; i < chatRecordList.size(); i++) {
                final ChatRecord c = chatRecordList.get(i);
                ChatRecordModel chatRecordModel = new ChatRecordModel();
                chatRecordModel.setChatroomId(c.getChatroom_id());
                chatRecordModel.setAvatar(c.getAvatar());
                chatRecordModel.setName(c.getName());
                chatRecordModel.setType(c.getChat_type());
                //未读消息数量
                DaoManager mManagers = new DaoManager();
                mManagers.init(this.getContext(), MessageDao.TABLENAME);
                Long UnReadSize = mManagers.getDaoSession().getMessageDao().queryBuilder()
                        .where(MessageDao.Properties.Chatroom_id.eq(c.getChatroom_id()))
                        .where(MessageDao.Properties.Is_read.eq(0))
                        .count();
                chatRecordModel.setUnReadSize(UnReadSize.intValue());

                // 查询聊天消息

                List<Message> messageList = mManagers.getDaoSession().getMessageDao().queryBuilder()
                        .where(MessageDao.Properties.Chatroom_id.eq(c.getChatroom_id()))
                        .orderDesc(MessageDao.Properties._id)
                        .limit(1).list();
                if (messageList.size() > 0) {
                    Message message = messageList.get(0);
                    String getMessage_send_time = message.getMessage_send_time() + "000";
                    Date date = new Date( Long.parseLong( getMessage_send_time ) );
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    chatRecordModel.setEndMsgTime(format.format(date));

                    // 判断消息类型
                    switch (message.getMessage_code()) {
                        case 1:
                            chatRecordModel.setEndMsg(message.getContent());
                            break;
                        case 2:
                            chatRecordModel.setEndMsg(getString(R.string.text_chat_record_img));
                            break;
                        case 3:
                            chatRecordModel.setEndMsg(getString(R.string.text_chat_record_location));
                            break;
                    }
                }
                mList.add(chatRecordModel);
                mChatRecordAdapter.notifyDataSetChanged();

                if(mList.size() > 0){
                    item_empty_view.setVisibility(View.GONE);
                    mChatRecordView.setVisibility(View.VISIBLE);
                }else{
                    item_empty_view.setVisibility(View.VISIBLE);
                    mChatRecordView.setVisibility(View.GONE);
                }
            }
            mChatRecordRefreshLayout.setRefreshing(false);
        } else {    // 如果数据为空
            mChatRecordRefreshLayout.setRefreshing(false);
            item_empty_view.setVisibility(View.VISIBLE);
            mChatRecordView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.iv_camera:
                //扫描
//                Intent intent = new Intent(getActivity(), QrCodeActivity.class);
//                startActivityForResult(intent, REQUEST_CODE);
//                break;
            case R.id.iv_add:
                //添加好友
                startActivity(new Intent(getActivity(), AddFriendActivity.class));
                break;
        }
    }

    @Override
    public void onRefresh() {
        if (mChatRecordRefreshLayout.isRefreshing()) {
            queryChatRecord(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        queryChatRecord(true);
    }

    // 消息事件
    // 事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        queryChatRecord(false);
    }

}
