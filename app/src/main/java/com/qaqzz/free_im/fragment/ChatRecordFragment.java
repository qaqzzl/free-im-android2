package com.qaqzz.free_im.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.qaqzz.framework.adapter.CommonAdapter;
import com.qaqzz.framework.adapter.CommonViewHolder;
import com.qaqzz.framework.base.BaseFragment;
import com.qaqzz.framework.event.MessageEvent;
import com.qaqzz.framework.manager.DialogManager;
import com.qaqzz.framework.utils.LogUtils;
import com.qaqzz.free_im.R;
import com.qaqzz.free_im.activities.AddFriendActivity;
import com.qaqzz.free_im.activities.ChatActivity;
import com.qaqzz.free_im.api.AddFriendApi;
import com.qaqzz.free_im.api.GetChatroomAvatarNameByChatroomIdApi;
import com.qaqzz.free_im.http.api.ApiListener;
import com.qaqzz.free_im.http.api.ApiUtil;
import com.qaqzz.free_im.model.ChatRecordModel;
import com.qaqzz.socket.database.ChatRecord;
import com.qaqzz.socket.database.ChatRecordDao;
import com.qaqzz.socket.database.Dao;
import com.qaqzz.socket.database.Message;
import com.qaqzz.socket.database.MessageDao;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
                            case "ordinary":
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

    }

    /**
     * 查询聊天记录
     */
    private void queryChatRecord(boolean setRefreshing ) {
        //mChatRecordRefreshLayout.setRefreshing(setRefreshing);
        Context mContext = this.getContext();
        LogUtils.i("查询聊天记录");

        if (mList.size() > 0) {
            mList.clear();
        }

        // 查询聊天会话数据
        ChatRecordDao chatRecordDao = Dao.getInstances(this.getContext()).getDaoSession().getChatRecordDao();
        List<ChatRecord> chatRecordList = chatRecordDao.queryBuilder().list();
        LogUtils.i(String.valueOf(chatRecordList.size()));
        if (chatRecordList.size() > 0) {
            for (int i = 0; i < chatRecordList.size(); i++) {
                final ChatRecord c = chatRecordList.get(i);
                int index = i;
                // 判断头像名称是否为空
                if (c.getAvatar()== null || c.getAvatar().equals("") || c.getName() == null || c.getName().equals("")) {
                    //获取头像|名称
                    try{
                        GetChatroomAvatarNameByChatroomIdApi apiBase = new GetChatroomAvatarNameByChatroomIdApi(c.getChatroom_id());
                        apiBase.post(new ApiListener() {
                            @Override
                            public void success(ApiUtil api, JSONObject response) {
                                c.setName(apiBase.mInfo.getName());
                                c.setAvatar(apiBase.mInfo.getAvatar());
                                // 更新数据库
                                com.qaqzz.socket.database.model.ChatRecordModel.getInstance(mContext).record(c);
                                // 重新加载
                                queryChatRecord(false);
                            }
                            @Override
                            public void error(ApiUtil api, JSONObject response) {

                            }
                        });
                    }catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                forList(c);
            }
            mChatRecordRefreshLayout.setRefreshing(false);
        } else {    // 如果数据为空
            mChatRecordRefreshLayout.setRefreshing(false);
            item_empty_view.setVisibility(View.VISIBLE);
            mChatRecordView.setVisibility(View.GONE);
        }
    }

    private void forList(ChatRecord c) {
        MessageDao messageDao = Dao.getInstances(this.getContext()).getDaoSession().getMessageDao();
        ChatRecordModel chatRecordModel = new ChatRecordModel();
        chatRecordModel.setChatroomId(c.getChatroom_id());
        chatRecordModel.setAvatar(c.getAvatar());
        chatRecordModel.setName(c.getName());
        chatRecordModel.setType(c.getChat_type());
        //未读消息数量
        Long UnReadSize = messageDao.queryBuilder()
                .where(MessageDao.Properties.Chatroom_id.eq(c.getChatroom_id()))
                .where(MessageDao.Properties.Is_read.eq(0))
                .count();
        chatRecordModel.setUnReadSize(UnReadSize.intValue());

        // 查询聊天消息
        List<Message> messageList = messageDao.queryBuilder()
                .where(MessageDao.Properties.Chatroom_id.eq(c.getChatroom_id()))
                .orderDesc(MessageDao.Properties._id)
                .limit(1)
                .list();
        if (messageList.size() > 0) {
            Message message = messageList.get(0);
            String getMessage_send_time = message.getMessage_send_time() + "000";
            Date date = new Date( Long.parseLong( getMessage_send_time ) );
            SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm:ss");
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
