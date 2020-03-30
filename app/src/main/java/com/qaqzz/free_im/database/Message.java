package com.qaqzz.free_im.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * @author qaqzz
 * @description TODO
 * @date 2020/3/16 19:26
 */
@Entity
public class Message {
    @Id(autoincrement = true)
    private Long _id;
    @NotNull
    private String chatroom_id;  // 聊天室ID
    @NotNull
    private String user_id;      // 发送方用户ID
    @NotNull
    private String client_message_id;       // 客户端消息ID

    private String server_message_id;       // 服务端消息ID
    @NotNull
    private String content;      // 内容
    @NotNull
    private int message_code;      // 消息类型 code
    @NotNull
    private int message_send_time;      // 发送时间
    @NotNull
    private String message_status;      // 消息状态
    @NotNull
    private int is_read;      // 是否已读 , 0:否, 1:是
    @Generated(hash = 1784875691)
    public Message(Long _id, @NotNull String chatroom_id, @NotNull String user_id,
            @NotNull String client_message_id, String server_message_id,
            @NotNull String content, int message_code, int message_send_time,
            @NotNull String message_status, int is_read) {
        this._id = _id;
        this.chatroom_id = chatroom_id;
        this.user_id = user_id;
        this.client_message_id = client_message_id;
        this.server_message_id = server_message_id;
        this.content = content;
        this.message_code = message_code;
        this.message_send_time = message_send_time;
        this.message_status = message_status;
        this.is_read = is_read;
    }
    @Generated(hash = 637306882)
    public Message() {
    }
    public Long get_id() {
        return this._id;
    }
    public void set_id(Long _id) {
        this._id = _id;
    }
    public String getChatroom_id() {
        return this.chatroom_id;
    }
    public void setChatroom_id(String chatroom_id) {
        this.chatroom_id = chatroom_id;
    }
    public String getUser_id() {
        return this.user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public String getClient_message_id() {
        return this.client_message_id;
    }
    public void setClient_message_id(String client_message_id) {
        this.client_message_id = client_message_id;
    }
    public String getServer_message_id() {
        return this.server_message_id;
    }
    public void setServer_message_id(String server_message_id) {
        this.server_message_id = server_message_id;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public int getMessage_code() {
        return this.message_code;
    }
    public void setMessage_code(int message_code) {
        this.message_code = message_code;
    }
    public int getMessage_send_time() {
        return this.message_send_time;
    }
    public void setMessage_send_time(int message_send_time) {
        this.message_send_time = message_send_time;
    }
    public String getMessage_status() {
        return this.message_status;
    }
    public void setMessage_status(String message_status) {
        this.message_status = message_status;
    }
    public int getIs_read() {
        return this.is_read;
    }
    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }
}
