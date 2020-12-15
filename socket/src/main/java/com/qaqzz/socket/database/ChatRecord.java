package com.qaqzz.socket.database;

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

    private Long last_open_time;     // 上次打开聊天室时间
    @NotNull
    private int chat_type;       // 聊天室类型, 1:普通(单聊), 2:群组(群聊)
    private String avatar;          // 聊天室头像
    private String name;            // 聊天室名称
    private int is_top;             // 是否置顶 1:是, 0:否
    private Long deleted_at;      // 删除时间
    private Long created_at;      // 创建时间
    @Generated(hash = 1701893954)
    public ChatRecord(Long _id, @NotNull String chatroom_id, Long last_open_time,
            int chat_type, String avatar, String name, int is_top, Long deleted_at,
            Long created_at) {
        this._id = _id;
        this.chatroom_id = chatroom_id;
        this.last_open_time = last_open_time;
        this.chat_type = chat_type;
        this.avatar = avatar;
        this.name = name;
        this.is_top = is_top;
        this.deleted_at = deleted_at;
        this.created_at = created_at;
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
    public Long getLast_open_time() {
        return this.last_open_time;
    }
    public void setLast_open_time(Long last_open_time) {
        this.last_open_time = last_open_time;
    }
    public int getChat_type() {
        return this.chat_type;
    }
    public void setChat_type(int chat_type) {
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
    public int getIs_top() {
        return this.is_top;
    }
    public void setIs_top(int is_top) {
        this.is_top = is_top;
    }
    public Long getDeleted_at() {
        return this.deleted_at;
    }
    public void setDeleted_at(Long deleted_at) {
        this.deleted_at = deleted_at;
    }
    public Long getCreated_at() {
        return this.created_at;
    }
    public void setCreated_at(Long created_at) {
        this.created_at = created_at;
    }
}
