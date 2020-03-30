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
public class ChatRecord {
    @Id(autoincrement = true)
    private Long _id;
    @NotNull
    private String chatroom_id;  // 聊天室ID

    private int last_open_time;     // 上次打开聊天室时间
    @NotNull
    private String chat_type;       // 聊天室类型, group:群组, common:普通(单聊)
    private String avatar;          // 聊天室头像
    private String name;            // 聊天室名称
    @Generated(hash = 436498312)
    public ChatRecord(Long _id, @NotNull String chatroom_id, int last_open_time,
            @NotNull String chat_type, String avatar, String name) {
        this._id = _id;
        this.chatroom_id = chatroom_id;
        this.last_open_time = last_open_time;
        this.chat_type = chat_type;
        this.avatar = avatar;
        this.name = name;
    }
    @Generated(hash = 1442974643)
    public ChatRecord() {
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
    public int getLast_open_time() {
        return this.last_open_time;
    }
    public void setLast_open_time(int last_open_time) {
        this.last_open_time = last_open_time;
    }
    public String getChat_type() {
        return this.chat_type;
    }
    public void setChat_type(String chat_type) {
        this.chat_type = chat_type;
    }
    public String getAvatar() {
        return this.avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
