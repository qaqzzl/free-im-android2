package com.qaqzz.free_im.im;

import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.qaqzz.framework.entity.Constants;
import com.qaqzz.framework.utils.SpUtils;
import com.qaqzz.free_im.CardContants;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author qaqzz
 * @description TODO
 * @date 2020/1/15 15:40
 */
public class SocketIm extends Thread {

    private static Socket socket;
    public static boolean is_init = false;     // 是否已经初始化socket

    private BufferedOutputStream bos;
    private BufferedInputStream bis;
    private ReadThread readThread;
//    private WriteRead writeRead;

    public void run() {
        try {
            // 获取用户ID, access_token, 设备ID
            String token = SpUtils.getInstance().getString(Constants.SP_TOKEN, "");
            String uid = SpUtils.getInstance().getString(Constants.SP_USERID, "");
            String device_id = SpUtils.getInstance().getString(Constants.SP_DEVICEID, "");
            AuthMessageStruct AuthMessage = new AuthMessageStruct();
            AuthMessage.setAccess_token(token);
            AuthMessage.setUser_id(uid);
            AuthMessage.setDevice_id(device_id);

            socket = new Socket();
            //建立Socket连接，需要服务端IP地址和端口号
            socket.connect(new InetSocketAddress(CardContants.IM_TCP, CardContants.IM_TCP_PORT), 5 * 1000);
            //通过Socket实例获取输入输出流，作为和服务器交换数据的通道
            bis = new BufferedInputStream(socket.getInputStream());
            bos = new BufferedOutputStream(socket.getOutputStream());

            // 连接认证
            Gson gson = new Gson();
            String jsonStr = gson.toJson(AuthMessage);
            System.out.println(jsonStr);
            Send("10"+jsonStr);

            is_init = true;     // 初始化完成

            // 开启新线程监听消息
            readThread = new ReadThread();
            readThread.start();
        } catch (UnknownHostException e) {
            Log.d("SOCKET", "UnknownHostException");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("SOCKET", "IOException");
            e.printStackTrace();
        }
    }

    //启动一个线程，一直读取从服务端发送过来的消息
    private class ReadThread extends Thread {
        @Override
        public void run() {
            while (true) {
                byte[] data = new byte[1024];
                int size = 0;
                try {
                    //收到客服端发送的消息后，返回一个消息给客户端
                    while((size = bis.read(data)) != -1) {
                        String str = new String(data, 0, size);
                        Message msg = new Message();
                        msg.obj = new String(str);
                        //mHandler.sendMessage(msg);
                        Log.d("SOCKET", "接受到的消息:" + str);
                        // 写入本地数据库
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 发送消息
    public static void Send(String str) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                // 判断是否初始化socket
                if (!is_init) {

                }
                try {
                    //2.拿到客户端的socket对象的输出流发送给服务器数据
                    OutputStream os = socket.getOutputStream();
                    //写入要发送给服务器的数据
                    String message = new String(str.getBytes(),"UTF-8");
                    os.write(str.getBytes());
                    os.flush();
                    // 关闭输出流
                    //socket.shutdownOutput();
                    //os.close();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }


    /**
     * 监听指定聊天室消息
     */



}
