package com.qaqzz.free_im.model;

/**
 * FileName: ChatRecordModel
 * Founder: LiuGuiLin
 * Profile: 会话管理的数据模型
 */
public class ChatRecordModel {
    public ChatRecordModel() {
    }

    public ChatRecordModel(String chatroomId, String avatar, String name, String endMsg, String endMsgTime, int unReadSize, int type) {
        this.chatroomId = chatroomId;
        this.avatar = avatar;
        this.name = name;
        this.endMsg = endMsg;
        this.endMsgTime = endMsgTime;
        this.unReadSize = unReadSize;
        Type = type;
    }

    private String chatroomId;      // 聊天室ID
    private String avatar;          // 聊天室头像
    private String name;            // 聊天室名称
    private String endMsg;          // 最后一条消息
    private String endMsgTime;      // 最后一条消息时间
    private int unReadSize;         // 未读数量
    private int Type;            // 聊天室类型


    public int getType() {
        return Type;
    }
    public void setType(int type) {
        Type = type;
    }
    public String getChatroomId() {
        return chatroomId;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getName() {
        return name;
    }

    public String getEndMsg() {
        return endMsg;
    }

    public String getEndMsgTime() {
        return endMsgTime;
    }

    public int getUnReadSize() {
        return unReadSize;
    }

    public void setChatroomId(String chatroomId) {
        this.chatroomId = chatroomId;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEndMsg(String endMsg) {
        this.endMsg = endMsg;
    }

    public void setEndMsgTime(String endMsgTime) {
        this.endMsgTime = endMsgTime;
    }

    public void setUnReadSize(int unReadSize) {
        this.unReadSize = unReadSize;
    }
}
