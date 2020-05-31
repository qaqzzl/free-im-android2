package com.qaqzz.socket.logic;

import android.content.Context;
import android.util.Log;

import com.qaqzz.framework.entity.Constants;
import com.qaqzz.framework.event.MessageEvent;
import com.qaqzz.framework.utils.SpUtils;
import com.qaqzz.socket.database.ChatRecord;
import com.qaqzz.socket.database.ChatRecordDao;
import com.qaqzz.socket.database.Dao;
import com.qaqzz.socket.database.Message;
import com.qaqzz.socket.database.MessageDao;
import com.qaqzz.socket.database.model.ChatRecordModel;
import com.qaqzz.socket.socket.SocketManager;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * @author qaqzz
 * @description TODO
 * @date 2020/3/17 10:09
 */
public class SocketLogic {
    private Context mContext;
    private static volatile SocketLogic instance = null;

    private SocketLogic(Context context) {
        mContext = context;
    }

    public static SocketLogic getInstance(Context context) {
        if (instance == null) {
            synchronized (SocketLogic.class) {
                if (instance == null) {
                    instance = new SocketLogic(context);
                }
            }
        }
        return instance;
    }

    // 消息接受处理
    public void MessageHandle(int action, String message)
    {
        String chatroom_id = "";
        String message_id = "";
        String content = "";
        int message_send_time = 0;
        int message_code = 0;
        String user_id = "";
        String me_uid = SpUtils.getInstance().getString(Constants.SP_USERID, "");
        JSONObject message_json;
        try {
            message_json = new JSONObject(message);
            chatroom_id = message_json.optString("chatroom_id");
            message_id = message_json.optString("message_id");
            content = message_json.optString("content");
            message_send_time = message_json.optInt("message_send_time");
            message_code = message_json.optInt("code");
            user_id = message_json.optString("user_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MessageDao messageDao = Dao.getInstances(mContext).getDaoSession().getMessageDao();
        switch (action) {
            case 5:      // 消息
                // 判断消息是否存在
                Message mMessage = messageDao.queryBuilder().where(MessageDao.Properties.Message_id.eq(message_id)).build().unique();
                if (mMessage != null) {
                    // 消息回执
                    SocketManager.getInstance(mContext).sendTcpMessage(7,message.getBytes());
                    return;
                }

                // 写入消息数据库
                messageDao.insert(new Message(null, chatroom_id, user_id, message_id, content, message_code, message_send_time, "success",0) );
                // 记录聊天会话
                Long timestamp = System.currentTimeMillis();//获取系统的当前时间戳
                ChatRecordModel.getInstance(mContext).record(new ChatRecord(
                        null, chatroom_id, timestamp, 0, null, null,0,null,timestamp
                ));

                // 消息事件
                MessageEvent mMessageEvent = new MessageEvent(message_code);
                mMessageEvent.setContent(content);
                mMessageEvent.setUserId(user_id);
                EventBus.getDefault().post(mMessageEvent);

                // 消息回执
                Log.d("SOCKET", "消息回执");
                SocketManager.getInstance(mContext).sendTcpMessage(7,message.getBytes());
                break;
            case 6:      //服务端消息回执
                Log.d("SOCKET", "服务端消息回执");
                // 更新消息数据库
                mMessage = messageDao.queryBuilder().where(MessageDao.Properties.Message_id.eq(message_id)).build().unique();
                if (mMessage != null) {
                    Long ts = new Date().getTime() / 1000;
                    mMessage.setMessage_send_time(ts.intValue());
                    mMessage.setMessage_status("success");
                    messageDao.update(mMessage);
                }
                break;
            case 7:     // 客户端消息回执

                break;
        }
    }
}
