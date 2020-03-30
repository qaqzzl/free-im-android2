package com.qaqzz.free_im.im;

/**
 * 消息结构体
 * @author qaqzz
 * @description TODO
 * @date 2020/1/15 16:30
 */
public class MessageStruct {
    private int code;
    private String chatroom_id;
    private String content;
    private String message_id;

    public int getCode() {
        return code;
    }

    public String getChatroom_id() {
        return chatroom_id;
    }

    public String getContent() {
        return content;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setChatroom_id(String chatroom_id) {
        this.chatroom_id = chatroom_id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }
}
