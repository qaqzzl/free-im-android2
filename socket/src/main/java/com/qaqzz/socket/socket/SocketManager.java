package com.qaqzz.socket.socket;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.qaqzz.framework.entity.Constants;
import com.qaqzz.framework.utils.SpUtils;
import com.qaqzz.socket.bean.AuthMessageBean;
import com.qaqzz.socket.common.Config;
import com.qaqzz.socket.listener.OnConnectionStateListener;
import com.qaqzz.socket.listener.OnMessageReceiveListener;
import com.qaqzz.socket.socket.tcp.TCPSocket;
import com.qaqzz.socket.socket.udp.UDPSocket;
import com.qaqzz.socket.utils.DeviceUtil;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by melo on 2017/11/27.
 */

public class SocketManager {

    private static volatile SocketManager instance = null;
    private UDPSocket udpSocket;
    private TCPSocket tcpSocket;
    private Context mContext;

    private SocketManager(Context context) {
        mContext = context.getApplicationContext();
    }

    public static SocketManager getInstance(Context context) {
        // if already inited, no need to get lock everytime
        if (instance == null) {
            synchronized (SocketManager.class) {
                if (instance == null) {
                    instance = new SocketManager(context);
                }
            }
        }

        return instance;
    }

    public void startUdpConnection() {
        if (udpSocket == null) {
            udpSocket = new UDPSocket(mContext);
        }

        // 注册接收消息的接口
        udpSocket.addOnMessageReceiveListener(new OnMessageReceiveListener() {
            @Override
            public void onMessageReceived(String message) {
                handleUdpMessage(message);
            }
        });

        udpSocket.startUDPSocket();

    }

    /**
     * 处理 udp 收到的消息
     *
     * @param message
     */
    private void handleUdpMessage(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始 TCP 连接
     */
    public void startTcpConnection() {
        String ip = Config.TCP_IP;
        String port = Config.TCP_PORT;
        if (tcpSocket == null) {// 保证只创建一次
            tcpSocket = new TCPSocket(mContext);
            tcpSocket.startTcpSocket(ip, port);

            tcpSocket.setOnConnectionStateListener(new OnConnectionStateListener() {
                @Override
                public void onSuccess() {// tcp 创建成功
                    // 获取用户ID, access_token, 设备ID
                    String token = SpUtils.getInstance().getString(Constants.SP_TOKEN, "");
                    String uid = SpUtils.getInstance().getString(Constants.SP_USERID, "");
                    String device_id = DeviceUtil.getDeviceId(mContext);
                    AuthMessageBean AuthMessage = new AuthMessageBean();
                    AuthMessage.setAccess_token(token);
                    AuthMessage.setUser_id(uid);
                    AuthMessage.setDevice_id(device_id);
                    AuthMessage.setClient_type("android");
                    AuthMessage.setDevice_type("mobile");

                    // 连接认证
                    Gson gson = new Gson();
                    String jsonStr = gson.toJson(AuthMessage);
                    System.out.println(jsonStr);
                    tcpSocket.sendTcpMessage("10"+jsonStr);

                    Log.d("SOCKET", "socket已连接");
                }

                @Override

                public void onFailed(int errorCode) { // tcp 异常处理
                    switch (errorCode) {
                        case Config.ErrorCode.CREATE_TCP_ERROR:
                            stopSocket();
                            startTcpConnection();
                            break;
                        case Config.ErrorCode.PING_TCP_TIMEOUT:
                            tcpSocket = null;
                            break;
                    }
                }
            });
        }
    }
    // 发送tcp消息
    public void sendTcpMessage(String str) {
        if (tcpSocket != null ) {
            tcpSocket.sendTcpMessage(str);
        } else {
            startTcpConnection();
        }

    }

    public void stopSocket() {
//        udpSocket.stopUDPSocket();
        tcpSocket.stopTcpConnection();

        if (udpSocket != null) {
            udpSocket = null;
        }
        if (tcpSocket != null) {
            tcpSocket = null;
        }
        Log.d("SOCKET", "socket已断开连接");
    }

}
