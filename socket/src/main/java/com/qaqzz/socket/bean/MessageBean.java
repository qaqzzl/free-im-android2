package com.qaqzz.socket.bean;

/**
 * 消息结构体
 * @author qaqzz
 * @description TODO
 * @date 2020/1/15 16:30
 */
public class MessageBean {
    private int Code;
    private String ChatroomId;
    private String Content;
    private String MessageId;

    public int getCode() {
        return Code;
    }

    public String getChatroomId() {
        return ChatroomId;
    }

    public String getContent() {
        return Content;
    }

    public String getMessageId() {
        return MessageId;
    }

    public void setCode(int code) {
        Code = code;
    }

    public void setChatroomId(String chatroomId) {
        ChatroomId = chatroomId;
    }

    public void setContent(String content) {
        Content = content;
    }

    public void setMessageId(String messageId) {
        MessageId = messageId;
    }
}
