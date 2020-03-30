package com.qaqzz.free_im.im;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qaqzz.framework.entity.Constants;
import com.qaqzz.framework.utils.SpUtils;
import com.qaqzz.free_im.CardContants;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Timer;
import java.util.TimerTask;


/**
 * @author qaqzz
 * @description TODO
 * @date 2020/3/16 16:55
 */
public class SocketService extends Service {

    /*socket*/
    private Socket socket;
    /*连接线程*/
    private Thread connectThread;
    private Timer timer = new Timer();
    private OutputStream outputStream;

    private SocketBinder sockerBinder = new SocketBinder();
    private String ip;
    private int port;
    private TimerTask task;

    /*默认重连*/
    private boolean isReConnect = true;

    private Handler handler = new Handler(Looper.getMainLooper());


    @Override
    public IBinder onBind(Intent intent) {
        return sockerBinder;
    }


    public class SocketBinder extends Binder {

        /*返回SocketService 在需要的地方可以通过ServiceConnection获取到SocketService  */
        public SocketService getService() {
            return SocketService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        /*拿到传递过来的ip和端口号*/
//        ip = intent.getStringExtra("ip");
//        port = intent.getStringExtra("port");

        ip = CardContants.IM_TCP;
        port = CardContants.IM_TCP_PORT;

        /*初始化socket*/
        initSocket();

        return super.onStartCommand(intent, flags, startId);
    }


    /*初始化socket*/
    public void initSocket() {
        if (socket == null && connectThread == null) {
            connectThread = new Thread(new Runnable() {
                @Override
                public void run() {

                    socket = new Socket();
                    try {
                        /*超时时间为5秒*/
                        socket.connect(new InetSocketAddress(CardContants.IM_TCP, CardContants.IM_TCP_PORT), 5 * 1000);
                        /*连接成功的话  发送心跳包*/
                        if (socket.isConnected()) {

                            // 获取用户ID, access_token, 设备ID
                            String token = SpUtils.getInstance().getString(Constants.SP_TOKEN, "");
                            String uid = SpUtils.getInstance().getString(Constants.SP_USERID, "");
                            String device_id = SpUtils.getInstance().getString(Constants.SP_DEVICEID, "");
                            AuthMessageStruct AuthMessage = new AuthMessageStruct();
                            AuthMessage.setAccess_token(token);
                            AuthMessage.setUser_id(uid);
                            AuthMessage.setDevice_id(device_id);

                            // 连接认证
                            Gson gson = new Gson();
                            String jsonStr = gson.toJson(AuthMessage);
                            System.out.println(jsonStr);
                            sendMsg("10"+jsonStr);

                            Log.d("SOCKET", "socket已连接");

                            /*发送连接成功的消息*/
//                            EventMsg msg = new EventMsg();
//                            msg.setTag(Constants.CONNET_SUCCESS);
//                            EventBus.getDefault().post(msg);
                            /*发送心跳数据*/
                            sendBeatData();

                            // 读取服务器发送的数据
                            Read();
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                        if (e instanceof SocketTimeoutException) {
                            Log.d("SOCKET", "连接超时，正在重连");

                            releaseSocket();

                        } else if (e instanceof NoRouteToHostException) {
                            Log.d("SOCKET", "该地址不存在，请检查");

                        } else if (e instanceof ConnectException) {
                            Log.d("SOCKET", "连接异常或被拒绝，请检查");

                        }


                    }

                }
            });

            /*启动连接线程*/
            connectThread.start();

        }


    }


    /*发送数据*/
    public void sendMsg(final String msg) {
        if (socket != null && socket.isConnected()) {
            /*发送指令*/
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        OutputStream outputStream = socket.getOutputStream();
                        if (outputStream != null) {
                            outputStream.write((msg).getBytes("UTF-8"));
                            outputStream.flush();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }).start();

        } else {
            Log.d("SOCKET", "socket连接错误,请重试");
        }
    }


    /*接受数据*/
    public void Read() {
        if (socket != null && socket.isConnected()) {
            Context context = this.getBaseContext();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
                        SocketLogic mSocketLogic = new SocketLogic(context);
                        while (true) {
                            byte[] data = new byte[1024];
                            int size = 0;
                            //收到客服端发送的消息后，返回一个消息给客户端
                            while((size = bis.read(data)) != -1) {
                                String str = new String(data, 0, size);
                                Message msg = new Message();
                                msg.obj = new String(str);
                                //mHandler.sendMessage(msg);
                                // 消息处理
                                mSocketLogic.MessageHandle(str);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } else {
            Log.d("SOCKET", "socket连接错误,请重试");
        }
    }

    /**
     * 定时发送数据, 心跳
     */
    private void sendBeatData() {
        if (timer == null) {
            timer = new Timer();
        }

        if (task == null) {
            task = new TimerTask() {
                @Override
                public void run() {
                    try {
                        outputStream = socket.getOutputStream();

                        /*这里的编码方式根据你的需求去改*/
                        outputStream.write(("99").getBytes("UTF-8"));
                        outputStream.flush();
                    } catch (Exception e) {
                        /*发送失败说明socket断开了或者出现了其他错误*/
                        Log.d("SOCKET", "连接断开，正在重连");
                        /*重连*/
                        releaseSocket();
                        e.printStackTrace();


                    }
                }
            };
        }
        timer.schedule(task, 0, 2000);
    }


    /*释放资源*/
    private void releaseSocket() {

        if (task != null) {
            task.cancel();
            task = null;
        }
        if (timer != null) {
            timer.purge();
            timer.cancel();
            timer = null;
        }

        if (outputStream != null) {
            try {
                outputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            outputStream = null;
        }

        if (socket != null) {
            try {
                socket.close();

            } catch (IOException e) {
            }
            socket = null;
        }

        if (connectThread != null) {
            connectThread = null;
        }

        /*重新初始化socket*/
        if (isReConnect) {
            initSocket();
        }

    }

//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Log.i("SocketService", "onDestroy");
//        isReConnect = false;
//        releaseSocket();
//    }
}
