package com.qaqzz.socket.socket.tcp;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.qaqzz.socket.common.Config;
import com.qaqzz.socket.listener.OnConnectionStateListener;
import com.qaqzz.socket.logic.SocketLogic;
import com.qaqzz.socket.utils.HeartbeatTimer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by melo on 2017/11/28.
 */

public class TCPSocket {

    private static final String TAG = "TCPSocket";

    private Context mContext;
    private ExecutorService mThreadPool;
    private Socket mSocket;
    private BufferedInputStream bis;
    private PrintWriter pw;
    private HeartbeatTimer timer;
    private long lastReceiveTime = 0;

    private OnConnectionStateListener mListener;

    private static final long TIME_OUT = 15 * 1000;
    private static final long HEARTBEAT_MESSAGE_DURATION = 2 * 5000;

    public TCPSocket(Context context) {
        this.mContext = context;

        int cpuNumbers = Runtime.getRuntime().availableProcessors();
        // 根据CPU数目初始化线程池
        mThreadPool = Executors.newFixedThreadPool(cpuNumbers * Config.POOL_SIZE);
        // 记录创建对象时的时间
        lastReceiveTime = System.currentTimeMillis();
    }

    public void startTcpSocket(final String ip, final String port) {
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                if (startTcpConnection(ip, Integer.valueOf(port))) {// 尝试建立 TCP 连接
                    if (mListener != null) {
                        mListener.onSuccess();
                    }
                    startReceiveTcpThread();
                    // 启动心跳
                    startHeartbeatTimer();
                } else {
                    if (mListener != null) {
                        mListener.onFailed(Config.ErrorCode.CREATE_TCP_ERROR);
                    }
                }
            }
        });
    }

    public void setOnConnectionStateListener(OnConnectionStateListener listener) {
        this.mListener = listener;
    }

    /**
     * 创建接收线程
     */
    private void startReceiveTcpThread() {
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if (bis == null ) {
                            continue;
                        }
                        byte buffer[] = new byte[1024];
                        int frameSize = 0;
                        byte[] mRecvBuffer = {0};
                        int mActualReadSize = 0;
                        int readSize = 0;
                        //接受服务端消息
                        while((readSize = bis.read(buffer)) != -1) {
                            mActualReadSize = 0;
                            if( readSize > 4 ){
                                frameSize = ( (buffer[0] & 0xff) << 24 | (buffer[1] & 0xff) << 16 | (buffer[2] & 0xff) << 8 | (buffer[3] & 0xff) );
                            }

                            while( readSize >= frameSize + 4 ){
                                mRecvBuffer = new byte[frameSize + 4];
                                System.arraycopy(buffer, mActualReadSize, mRecvBuffer, 0, frameSize + 4);
                                mActualReadSize += frameSize + 4;

                                int surplusLength = readSize - (frameSize + 4);
                                readSize -=  (frameSize + 4);
                                if( surplusLength >= 4 ){
                                    int head = ((buffer[mActualReadSize] & 0xff) << 4) | buffer[mActualReadSize + 1];
                                    if( head == 0xA66A ){
                                        frameSize = 0;
                                        frameSize = ( (buffer[mActualReadSize + 0] & 0xff) << 24 | (buffer[ mActualReadSize + 1] & 0xff) << 16 | (buffer[ mActualReadSize + 2] & 0xff) << 8 | (buffer[ mActualReadSize + 3] & 0xff) );
                                    }else{
                                        break;
                                    }
                                }
                            }

                            String str = new String(mRecvBuffer, 4, mActualReadSize-4);
                            // 消息处理
                            handleReceiveTcpMessage(str);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 处理 tcp 收到的消息
     *
     * @param line
     */
    private void handleReceiveTcpMessage(String line) {
        Log.d(TAG, "接收 tcp 消息：" + line);
        SocketLogic.getInstance(mContext).MessageHandle(line);
        lastReceiveTime = System.currentTimeMillis();
    }

    public void sendTcpMessage(final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OutputStream outputStream = mSocket.getOutputStream();
                    if (outputStream != null) {
                        Log.d("SOCKET msg", msg);
                        int mesLen = msg.getBytes().length;
                        byte[] array = new byte[4+mesLen];
                        array[0] = (byte)(mesLen>>24);
                        array[1] = (byte)(mesLen>>16);
                        array[2] = (byte)(mesLen>>8);
                        array[3] = (byte)mesLen;
                        System.arraycopy(msg.getBytes(), 0, array, 4, mesLen);
                        outputStream.write(array);
                        outputStream.flush();
                    }
                } catch (IOException e) {
//                    /*发送失败说明socket断开了或者出现了其他错误*/
                    Log.d("SOCKET", "连接断开，正在重连");
//                    /*重连*/
                    mListener.onFailed(Config.ErrorCode.CREATE_TCP_ERROR);
                    e.printStackTrace();
                }

            }
        }).start();

//        pw.println(str);
//        Log.d(TAG, "tcp 消息发送成功...");
    }

    /**
     * 启动心跳
     */
    private void startHeartbeatTimer() {
        if (timer == null) {
            timer = new HeartbeatTimer();
        }
        timer.setOnScheduleListener(new HeartbeatTimer.OnScheduleListener() {
            @Override
            public void onSchedule() {
                // 发送心跳消息
                sendTcpMessage(Config.PING);
            }

        });
        timer.startTimer(0, 1000 * 5);
    }

    public void stopHeartbeatTimer() {
        if (timer != null) {
            timer.exit();
            timer = null;
        }
    }

    /**
     * 尝试建立tcp连接
     *
     * @param ip
     * @param port
     */
    private boolean startTcpConnection(final String ip, final int port) {
        try {
            if (mSocket == null) {
                mSocket = new Socket(ip, port);
                mSocket.setKeepAlive(true);
                mSocket.setTcpNoDelay(true);
                mSocket.setReuseAddress(true);
            }
            bis = new BufferedInputStream(mSocket.getInputStream());
            OutputStream os = mSocket.getOutputStream();
            pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os)), true);
            Log.d(TAG, "tcp 创建成功...");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void releaseSocket() {
        stopTcpConnection();
        TCPSocket TCPSocket = new TCPSocket(mContext);
        TCPSocket.startTcpSocket(Config.TCP_IP, Config.TCP_PORT);
    }

    public void stopTcpConnection() {
        try {
            stopHeartbeatTimer();
            if (bis != null) {
                bis.close();
            }
            if (pw != null) {
                pw.close();
            }
            if (mThreadPool != null) {
                mThreadPool.shutdown();
                mThreadPool = null;
            }
            if (mSocket != null) {
                mSocket.close();
                mSocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
