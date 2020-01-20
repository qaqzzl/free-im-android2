package com.qaqzz.free_im.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.qaqzz.framework.adapter.CommonAdapter;
import com.qaqzz.framework.adapter.CommonViewHolder;
import com.qaqzz.framework.base.BaseActivity;
import com.qaqzz.framework.entity.Constants;
import com.qaqzz.framework.event.EventManager;
import com.qaqzz.framework.event.MessageEvent;
import com.qaqzz.framework.helper.FileHelper;
import com.qaqzz.framework.utils.LogUtils;
import com.qaqzz.framework.utils.SpUtils;
import com.qaqzz.free_im.R;
import com.qaqzz.free_im.api.FriendIdGetChatroomIdApi;
import com.qaqzz.free_im.http.api.ApiListener;
import com.qaqzz.free_im.http.api.ApiUtil;
import com.qaqzz.free_im.im.AuthMessageStruct;
import com.qaqzz.free_im.im.MessageStruct;
import com.qaqzz.free_im.im.SocketIm;
import com.qaqzz.free_im.model.MyMemberInfo;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * FileName: ChatActivity
 * Founder: LiuGuiLin
 * Profile: 聊天
 */
public class ChatActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_chat;
    }
/**
     *
     * 发送文本逻辑
     * 1.跳转到ChatActivity
     * 2.实现我们的聊天列表 适配器
     * 3.加载我们的历史记录
     * 4.实时更新我们的聊天信息
     * 5.发送消息
     */

    /**
     * 发送图片逻辑
     * 1.读取(相机和相册)
     * 2.发送图片消息
     * 3.完成我们适配器的UI
     * 4.完成Service的图片接收逻辑
     * 5.通知UI刷新
     */

    /**
     * 发送地址的逻辑
     * 1.获取地址
     * 2.发送位置消息
     * 不能忘记：
     * 1.历史消息
     * 2.适配器
     * 3.发送消息
     */

    //左边
    public static final int TYPE_LEFT_TEXT = 0;
    public static final int TYPE_LEFT_IMAGE = 1;
    public static final int TYPE_LEFT_LOCATION = 2;

    //右边
    public static final int TYPE_RIGHT_TEXT = 3;
    public static final int TYPE_RIGHT_IMAGE = 4;
    public static final int TYPE_RIGHT_LOCATION = 5;

    private static final int LOCATION_REQUEST_CODE = 1888;

    private static final int CHAT_INFO_REQUEST_CODE = 1889;

    /**
     * 跳转
     *
     * @param mContext
     * @param userId
     * @param userName
     * @param userPhoto
     */
    public static void startActivity(Context mContext, String userId,
                                     String userName, String userPhoto) {
//        if (!CloudManager.getInstance().isConnect()) {
//            Toast.makeText(mContext, mContext.getString(R.string.text_server_status), Toast.LENGTH_SHORT).show();
//            return;
//        }
        Intent intent = new Intent(mContext, ChatActivity.class);
        intent.putExtra(Constants.INTENT_USER_ID, userId);
        intent.putExtra(Constants.INTENT_USER_NAME, userName);
        intent.putExtra(Constants.INTENT_USER_PHOTO, userPhoto);
        mContext.startActivity(intent);
    }

    //聊天列表
    private RecyclerView mChatView;
    //输入框
    private EditText et_input_msg;
    //发送按钮
    private Button btn_send_msg;
    //语音输入
    private LinearLayout ll_voice;
    //相机
    private LinearLayout ll_camera;
    //图片
    private LinearLayout ll_pic;
    //位置
    private LinearLayout ll_location;

    //背景主题
    private LinearLayout ll_chat_bg;

    //对方用户信息
    private String yourUserId;
    private String yourUserName;
    private String yourUserPhoto;

    //自己的信息
    private String meUserPhoto;

    //列表
    private CommonAdapter<ChatModel> mChatAdapter;
    private List<ChatModel> mList = new ArrayList<>();

    //图片文件
    private File uploadFile = null;

    // 聊天室房间ID
    private String chatroom_id = "";

    private static class ChatModel {

        private int type;

        //文本
        private String text;

        //图片
        private String imgUrl;
        private File localFile;

        //位置
        private double la;
        private double lo;
        private String address;
        private String mapUrl;

        //...


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

        public String getMapUrl() {
            return mapUrl;
        }

        public void setMapUrl(String mapUrl) {
            this.mapUrl = mapUrl;
        }

        public File getLocalFile() {
            return localFile;
        }

        public void setLocalFile(File localFile) {
            this.localFile = localFile;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    protected void initWidget() {

        mChatView = (RecyclerView) findViewById(R.id.mChatView);
        et_input_msg = (EditText) findViewById(R.id.et_input_msg);
        btn_send_msg = (Button) findViewById(R.id.btn_send_msg);

        ll_voice = (LinearLayout) findViewById(R.id.ll_voice);
        ll_camera = (LinearLayout) findViewById(R.id.ll_camera);
        ll_pic = (LinearLayout) findViewById(R.id.ll_pic);
        ll_location = (LinearLayout) findViewById(R.id.ll_location);
        ll_chat_bg = (LinearLayout) findViewById(R.id.ll_chat_bg);

        btn_send_msg.setOnClickListener(this);
        ll_voice.setOnClickListener(this);
        ll_camera.setOnClickListener(this);
        ll_pic.setOnClickListener(this);
        ll_location.setOnClickListener(this);

        updateChatTheme();

        mChatView.setLayoutManager(new LinearLayoutManager(this));
        mChatAdapter = new CommonAdapter<>(mList, new CommonAdapter.OnMoreBindDataListener<ChatModel>() {
            @Override
            public int getItemType(int position) {
                return mList.get(position).getType();
            }

            @Override
            public void onBindViewHolder(final ChatModel model, CommonViewHolder viewHolder, int type, int position) {
                switch (model.getType()) {
                    case TYPE_LEFT_TEXT:
                        viewHolder.setText(R.id.tv_left_text, model.getText());
                        viewHolder.setImageUrl(ChatActivity.this, R.id.iv_left_photo, yourUserPhoto);
                        break;
                    case TYPE_RIGHT_TEXT:
                        viewHolder.setText(R.id.tv_right_text, model.getText());
                        viewHolder.setImageUrl(ChatActivity.this, R.id.iv_right_photo, meUserPhoto);
                        break;
                    case TYPE_LEFT_IMAGE:
                        viewHolder.setImageUrl(ChatActivity.this, R.id.iv_left_img, model.getImgUrl());
                        viewHolder.setImageUrl(ChatActivity.this, R.id.iv_left_photo, yourUserPhoto);

                        viewHolder.getView(R.id.iv_left_img).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                ImagePreviewActivity.startActivity(
//                                        ChatActivity.this, true, model.getImgUrl());
                            }
                        });

                        break;
                    case TYPE_RIGHT_IMAGE:
                        if (TextUtils.isEmpty(model.getImgUrl())) {
                            if (model.getLocalFile() != null) {
                                //加载本地文件
                                viewHolder.setImageFile(ChatActivity.this, R.id.iv_right_img, model.getLocalFile());
                                viewHolder.getView(R.id.iv_right_img).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
//                                        ImagePreviewActivity.startActivity(
//                                                ChatActivity.this, false, model.getLocalFile().getPath());
                                    }
                                });
                            }
                        } else {
                            viewHolder.setImageUrl(ChatActivity.this, R.id.iv_right_img, model.getImgUrl());
                            viewHolder.getView(R.id.iv_right_img).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                                    ImagePreviewActivity.startActivity(
//                                            ChatActivity.this, true, model.getImgUrl());
                                }
                            });
                        }
                        viewHolder.setImageUrl(ChatActivity.this, R.id.iv_right_photo, meUserPhoto);
                        break;
                    case TYPE_LEFT_LOCATION:
                        viewHolder.setImageUrl(ChatActivity.this, R.id.iv_left_photo, yourUserPhoto);
                        viewHolder.setImageUrl(ChatActivity.this, R.id.iv_left_location_img
                                , model.getMapUrl());
                        viewHolder.setText(R.id.tv_left_address, model.getAddress());

                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                LocationActivity.startActivity(ChatActivity.this, false,
//                                        model.getLa(), model.getLo(), model.getAddress(), LOCATION_REQUEST_CODE);
                            }
                        });

                        break;
                    case TYPE_RIGHT_LOCATION:
                        viewHolder.setImageUrl(ChatActivity.this, R.id.iv_right_photo, meUserPhoto);
                        viewHolder.setImageUrl(ChatActivity.this,
                                R.id.iv_right_location_img, model.getMapUrl());
                        viewHolder.setText(R.id.tv_right_address, model.getAddress());

                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                LocationActivity.startActivity(ChatActivity.this, false,
//                                        model.getLa(), model.getLo(), model.getAddress(), LOCATION_REQUEST_CODE);
                            }
                        });
                        break;
                }
            }

            @Override
            public int getLayoutId(int type) {
                if (type == TYPE_LEFT_TEXT) {
                    return R.layout.layout_chat_left_text;
                } else if (type == TYPE_RIGHT_TEXT) {
                    return R.layout.layout_chat_right_text;
                } else if (type == TYPE_LEFT_IMAGE) {
                    return R.layout.layout_chat_left_img;
                } else if (type == TYPE_RIGHT_IMAGE) {
                    return R.layout.layout_chat_right_img;
                } else if (type == TYPE_LEFT_LOCATION) {
                    return R.layout.layout_chat_left_location;
                } else if (type == TYPE_RIGHT_LOCATION) {
                    return R.layout.layout_chat_right_location;
                }
                return 0;
            }
        });
        mChatView.setAdapter(mChatAdapter);

        loadMeInfo();

        queryMessage();
    }

    // 初始化数据
    protected void initData() {
        try{
            FriendIdGetChatroomIdApi apiBase = new FriendIdGetChatroomIdApi(yourUserId);
            apiBase.post(new ApiListener() {
                @Override
                public void success(ApiUtil api, JSONObject response) {
                    chatroom_id = apiBase.mInfo.getChatroom_id();
                }
                @Override
                public void error(ApiUtil api, JSONObject response) {

                }
            });

        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 更新主题
     */
    private void updateChatTheme() {
        //主题的选择 0:无主题
        int chat_theme = SpUtils.getInstance().getInt(Constants.SP_CHAT_THEME, 0);
        switch (chat_theme) {
            case 1:
                ll_chat_bg.setBackgroundResource(R.drawable.img_chat_bg_1);
                break;
            case 2:
                ll_chat_bg.setBackgroundResource(R.drawable.img_chat_bg_2);
                break;
            case 3:
                ll_chat_bg.setBackgroundResource(R.drawable.img_chat_bg_3);
                break;
            case 4:
                ll_chat_bg.setBackgroundResource(R.drawable.img_chat_bg_4);
                break;
            case 5:
                ll_chat_bg.setBackgroundResource(R.drawable.img_chat_bg_5);
                break;
            case 6:
                ll_chat_bg.setBackgroundResource(R.drawable.img_chat_bg_6);
                break;
            case 7:
                ll_chat_bg.setBackgroundResource(R.drawable.img_chat_bg_7);
                break;
            case 8:
                ll_chat_bg.setBackgroundResource(R.drawable.img_chat_bg_8);
                break;
            case 9:
                //9的话是默认，可以不设置图片，直接就是纯白
                //ll_chat_bg.setBackgroundResource(R.drawable.img_chat_bg_9);
                break;
        }
    }

    /**
     * 查询聊天记录
     */
    private void queryMessage() {
//        CloudManager.getInstance().getHistoryMessages(yourUserId, new RongIMClient.ResultCallback<List<Message>>() {
//            @Override
//            public void onSuccess(List<Message> messages) {
//                if (CommonUtils.isEmpty(messages)) {
//                    try {
//                        parsingListMessage(messages);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    queryRemoteMessage();
//                }
//            }
//
//            @Override
//            public void onError(RongIMClient.ErrorCode errorCode) {
//                LogUtils.e("errorCode:" + errorCode);
//            }
//        });
    }

    /**
     * 解析历史记录
     *
     * @param messages
     */
//    private void parsingListMessage(List<Message> messages) {
//        //倒序
//        Collections.reverse(messages);
//        //遍历
//        for (int i = 0; i < messages.size(); i++) {
//            Message m = messages.get(i);
//            String objectName = m.getObjectName();
//            if (objectName.equals(CloudManager.MSG_TEXT_NAME)) {
//                TextMessage textMessage = (TextMessage) m.getContent();
//                String msg = textMessage.getContent();
//                LogUtils.i("msg:" + msg);
//                try {
//                    TextBean textBean = new Gson().fromJson(msg, TextBean.class);
//                    if (textBean.getType().equals(CloudManager.TYPE_TEXT)) {
//                        //添加到UI 判断是你 还是 我
//                        if (m.getSenderUserId().equals(yourUserId)) {
//                            addText(0, textBean.getMsg());
//                        } else {
//                            addText(1, textBean.getMsg());
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else if (objectName.equals(CloudManager.MSG_IMAGE_NAME)) {
//                ImageMessage imageMessage = (ImageMessage) m.getContent();
//                String url = imageMessage.getRemoteUri().toString();
//                if (!TextUtils.isEmpty(url)) {
//                    LogUtils.i("url:" + url);
//                    if (m.getSenderUserId().equals(yourUserId)) {
//                        addImage(0, url);
//                    } else {
//                        addImage(1, url);
//                    }
//                }
//            } else if (objectName.equals(CloudManager.MSG_LOCATION_NAME)) {
//                LocationMessage locationMessage = (LocationMessage) m.getContent();
//                if (m.getSenderUserId().equals(yourUserId)) {
//                    addLocation(0, locationMessage.getLat(),
//                            locationMessage.getLng(), locationMessage.getPoi());
//                } else {
//                    addLocation(1, locationMessage.getLat(),
//                            locationMessage.getLng(), locationMessage.getPoi());
//                }
//            }
//        }
//    }

    /**
     * 查询服务器历史记录
     */
//    private void queryRemoteMessage() {
//        CloudManager.getInstance().getRemoteHistoryMessages(yourUserId, new RongIMClient.ResultCallback<List<Message>>() {
//            @Override
//            public void onSuccess(List<Message> messages) {
//                if (CommonUtils.isEmpty(messages)) {
//                    try {
//                        parsingListMessage(messages);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onError(RongIMClient.ErrorCode errorCode) {
//                LogUtils.e("errorCode:" + errorCode);
//            }
//        });
//    }

    /**
     * 加载自我信息
     */
    private void loadMeInfo() {
        Intent intent = getIntent();
        yourUserId = intent.getStringExtra(Constants.INTENT_USER_ID);
        yourUserName = intent.getStringExtra(Constants.INTENT_USER_NAME);
        yourUserPhoto = intent.getStringExtra(Constants.INTENT_USER_PHOTO);

        meUserPhoto = new MyMemberInfo().getmUserInfoBean().getAvatar();

        LogUtils.i("yourUserPhoto:" + yourUserPhoto);
        LogUtils.i("meUserPhoto:" + meUserPhoto);

        //设置标题
        if (!TextUtils.isEmpty(yourUserName)) {
            getSupportActionBar().setTitle(yourUserName);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_msg:     // 发送消息
                String inputText = et_input_msg.getText().toString().trim();
                if (TextUtils.isEmpty(inputText)) {
                    return;
                }
//                CloudManager.getInstance().sendTextMessage(inputText,
//                        CloudManager.TYPE_TEXT, yourUserId);
                MessageStruct messageStruct = new MessageStruct();
                messageStruct.setChatroom_id(chatroom_id);
                messageStruct.setCode(1);
                messageStruct.setContent(inputText);
                Gson gson = new Gson();
                String jsonStr = gson.toJson(messageStruct);
                SocketIm.Send("04"+jsonStr);

                Log.d("SOCKET", "is_init:" + SocketIm.is_init);

                addText(1, inputText);
                //清空
                et_input_msg.setText("");
                break;
            case R.id.ll_voice:
//                VoiceManager.getInstance(this).startSpeak(new RecognizerDialogListener() {
//                    @Override
//                    public void onResult(RecognizerResult recognizerResult, boolean b) {
//                        String result = recognizerResult.getResultString();
//                        if (!TextUtils.isEmpty(result)) {
//                            LogUtils.i("result:" + result);
//                            VoiceBean voiceBean = new Gson().fromJson(result, VoiceBean.class);
//                            if (voiceBean.isLs()) {
//                                StringBuffer sb = new StringBuffer();
//                                for (int i = 0; i < voiceBean.getWs().size(); i++) {
//                                    VoiceBean.WsBean wsBean = voiceBean.getWs().get(i);
//                                    String sResult = wsBean.getCw().get(0).getW();
//                                    sb.append(sResult);
//                                }
//                                LogUtils.i("result:" + sb.toString());
//                                et_input_msg.setText(sb.toString());
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onError(SpeechError speechError) {
//                        LogUtils.e("speechError:" + speechError.toString());
//                    }
//                });
                break;
            case R.id.ll_camera:
//                FileHelper.getInstance().toCamera(this);
                break;
            case R.id.ll_pic:
//                FileHelper.getInstance().toAlbum(this);
                break;
            case R.id.ll_location:
//                LocationActivity.startActivity(this, true, 0, 0, "", LOCATION_REQUEST_CODE);
                break;
        }
    }

    /**
     * 添加数据的基类
     *
     * @param model
     */
    private void baseAddItem(ChatModel model) {
        mList.add(model);
        mChatAdapter.notifyDataSetChanged();
        //滑动到底部
        mChatView.scrollToPosition(mList.size() - 1);
    }

    /**
     * 添加左边文字
     *
     * @param index 0:左边 1:右边
     * @param text
     */
    private void addText(int index, String text) {
        LogUtils.i("ChatA:" + text);
        ChatModel model = new ChatModel();
        if (index == 0) {
            model.setType(TYPE_LEFT_TEXT);
        } else {
            model.setType(TYPE_RIGHT_TEXT);
        }
        model.setText(text);
        baseAddItem(model);
    }

    /**
     * 添加图片
     *
     * @param index
     * @param url
     */
    private void addImage(int index, String url) {
        ChatModel model = new ChatModel();
        if (index == 0) {
            model.setType(TYPE_LEFT_IMAGE);
        } else {
            model.setType(TYPE_RIGHT_IMAGE);
        }
        model.setImgUrl(url);
        baseAddItem(model);
    }

    /**
     * 添加图片
     *
     * @param index
     * @param file
     */
    private void addImage(int index, File file) {
        ChatModel model = new ChatModel();
        if (index == 0) {
            model.setType(TYPE_LEFT_IMAGE);
        } else {
            model.setType(TYPE_RIGHT_IMAGE);
        }
        model.setLocalFile(file);
        baseAddItem(model);
    }

    /**
     * 添加地址
     *
     * @param index
     * @param la
     * @param lo
     * @param address
     */
    private void addLocation(int index, double la, double lo, String address) {
        ChatModel model = new ChatModel();
        if (index == 0) {
            model.setType(TYPE_LEFT_LOCATION);
        } else {
            model.setType(TYPE_RIGHT_LOCATION);
        }
        model.setLa(la);
        model.setLo(lo);
        model.setAddress(address);
//        model.setMapUrl(MapManager.getInstance().getMapUrl(la, lo));
        baseAddItem(model);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (!event.getUserId().equals(yourUserId)) {
            return;
        }
        switch (event.getType()) {
            case EventManager.FLAG_SEND_TEXT:
                addText(0, event.getText());
                break;
            case EventManager.FLAG_SEND_IMAGE:
                addImage(0, event.getImgUrl());
                break;
            case EventManager.FLAG_SEND_LOCATION:
                addLocation(0, event.getLa(), event.getLo(), event.getAddress());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == BaseActivity.RESULT_OK) {
            if (requestCode == FileHelper.CAMEAR_REQUEST_CODE) {
                uploadFile = FileHelper.getInstance().getTempFile();
            } else if (requestCode == FileHelper.ALBUM_REQUEST_CODE) {
                Uri uri = data.getData();
                if (uri != null) {
                    //String path = uri.getPath();
                    //获取真实的地址
                    String path = FileHelper.getInstance().getRealPathFromURI(this, uri);
                    //LogUtils.e("path:" + path);
                    if (!TextUtils.isEmpty(path)) {
                        uploadFile = new File(path);
                    }
                }
            } else if (requestCode == LOCATION_REQUEST_CODE) {
                double la = data.getDoubleExtra("la", 0);
                double lo = data.getDoubleExtra("lo", 0);
                String address = data.getStringExtra("address");

                LogUtils.i("la:" + la);
                LogUtils.i("lo:" + lo);
                LogUtils.i("address:" + address);

//                if (TextUtils.isEmpty(address)) {
//                    MapManager.getInstance().poi2address(la, lo, new MapManager.OnPoi2AddressGeocodeListener() {
//                        @Override
//                        public void poi2address(String address) {
//                            //发送位置消息
//                            CloudManager.getInstance().sendLocationMessage(yourUserId, la, lo, address);
//                            addLocation(1, la, lo, address);
//                        }
//                    });
//                } else {
//                    //发送位置消息
//                    CloudManager.getInstance().sendLocationMessage(yourUserId, la, lo, address);
//                    addLocation(1, la, lo, address);
//                }

            } else if (requestCode == CHAT_INFO_REQUEST_CODE) {
                finish();
            }
            if (uploadFile != null) {
                //发送图片消息
//                CloudManager.getInstance().sendImageMessage(yourUserId, uploadFile);
                //更新列表
                addImage(1, uploadFile);
                uploadFile = null;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_chat_menu:
//                ChatInfoActivity.startChatInfo(this, yourUserId, CHAT_INFO_REQUEST_CODE);
                break;
            case R.id.menu_chat_audio:
//                if (!checkWindowPermissions()) {
//                    requestWindowPermissions();
//                } else {
//                    CloudManager.getInstance().startAudioCall(this, yourUserId);
//                }
                break;
            case R.id.menu_chat_video:
//                if (!checkWindowPermissions()) {
//                    requestWindowPermissions();
//                } else {
//                    CloudManager.getInstance().startVideoCall(this, yourUserId);
//                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
