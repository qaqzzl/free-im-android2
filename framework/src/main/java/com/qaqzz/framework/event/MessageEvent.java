package com.qaqzz.framework.event;

import android.view.SurfaceView;

/**
 * FileName: MessageEvent
 * Founder: LiuGuiLin
 * Profile: 事件
 */
public class MessageEvent {

    private int type;

    //文本消息
    private String userId;  //每个消息都应该持有userId
    private String content;

    //图片消息
    private String imgUrl;

    //位置消息
    private double la;
    private double lo;
    private String address;

    //相机
    private SurfaceView mSurfaceView;

    //服务器连接状态
    private boolean connectStatus;

    public boolean isConnectStatus() {
        return connectStatus;
    }

    public void setConnectStatus(boolean connectStatus) {
        this.connectStatus = connectStatus;
    }

    public SurfaceView getmSurfaceView() {
        return mSurfaceView;
    }

    public void setmSurfaceView(SurfaceView mSurfaceView) {
        this.mSurfaceView = mSurfaceView;
    }

    public double getLa() {
        return la;
    }

    public void setLa(double la) {
        this.la = la;
    }

    public double getLo() {
        return lo;
    }

    public void setLo(double lo) {
        this.lo = lo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return "MessageEvent{" +
                "type=" + type +
                ", userId='" + userId + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
