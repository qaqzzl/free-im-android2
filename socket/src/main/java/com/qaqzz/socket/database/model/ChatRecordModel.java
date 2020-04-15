package com.qaqzz.socket.database.model;

import android.content.Context;
import com.qaqzz.socket.database.ChatRecord;
import com.qaqzz.socket.database.ChatRecordDao;
import com.qaqzz.socket.database.Dao;


/**
 * @author qaqzz
 * @description TODO
 * @date 2020/3/31 14:18
 */
public class ChatRecordModel {
    private static volatile ChatRecordModel instance = null;
    private Context mContext;

    private ChatRecordModel(Context context) {
        mContext = context;
    }

    public static ChatRecordModel getInstance(Context context) {
        // if already inited, no need to get lock everytime
        if (instance == null) {
            synchronized (ChatRecordModel.class) {
                if (instance == null) {
                    instance = new ChatRecordModel(context);
                }
            }
        }
        return instance;
    }


    // 记录聊天记录
    public void record( ChatRecord mChatRecord) {
        // 记录聊天会话
        Long timestamp = System.currentTimeMillis();//获取系统的当前时间戳
        // 判断是否存在
        ChatRecordDao chatRecordDao = Dao.getInstances(mContext).getDaoSession().getChatRecordDao();
        ChatRecord chatRecord = chatRecordDao.queryBuilder()
                .where(ChatRecordDao.Properties.Chatroom_id.eq(mChatRecord.getChatroom_id()))
                .build().unique();
        if (chatRecord != null) {
            chatRecord.setLast_open_time(timestamp);
            if (mChatRecord.getAvatar() == null || mChatRecord.getAvatar().equals("")) {
                chatRecord.setAvatar(mChatRecord.getAvatar());
            }
            if (mChatRecord.getAvatar() == null || mChatRecord.getName().equals("")) {
                chatRecord.setName(mChatRecord.getName());
            }
            chatRecordDao.update(chatRecord);
        } else {
//            if (mChatRecord.getAvatar().equals("") || mChatRecord.getName().equals("")) {   // 如果头像跟名称为空
//
//            } else {
//
//            }
            chatRecordDao.insert(mChatRecord);
        }
    }
}
