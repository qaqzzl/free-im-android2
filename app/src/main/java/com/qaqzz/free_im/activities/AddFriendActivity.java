package com.qaqzz.free_im.activities;

import android.Manifest;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qaqzz.framework.adapter.CommonAdapter;
import com.qaqzz.framework.adapter.CommonViewHolder;
import com.qaqzz.framework.base.BaseBackActivity;
import com.qaqzz.framework.entity.Constants;
import com.qaqzz.framework.utils.CommonUtils;
import com.qaqzz.framework.utils.LogUtils;
import com.qaqzz.framework.utils.SpUtils;
import com.qaqzz.free_im.R;
import com.qaqzz.free_im.api.SearchFriendApi;
import com.qaqzz.free_im.bean.SearchFriendBean;
import com.qaqzz.free_im.http.api.ApiListener;
import com.qaqzz.free_im.http.api.ApiUtil;
import com.qaqzz.free_im.model.AddFriendModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * FileName: AddFriendActivity
 * Founder: LiuGuiLin
 * Profile: 添加好友
 */
public class AddFriendActivity extends BaseBackActivity implements View.OnClickListener {
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_add_friend;
    }

    //标题
    public static final int TYPE_TITLE = 0;
    //内容
    public static final int TYPE_CONTENT = 1;

    /**
     * 1.模拟用户数据
     * 2.根据条件查询
     * 3.推荐好友
     */

    private LinearLayout ll_to_contact;
    private EditText et_search;
    private ImageView iv_search;
    private RecyclerView mSearchResultView;

    private View include_empty_view;

    private CommonAdapter mAddFriendAdapter;
    private List<AddFriendModel> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 初始化View
     */
    protected void initWidget() {

        include_empty_view = findViewById(R.id.include_empty_view);

        ll_to_contact = (LinearLayout) findViewById(R.id.ll_to_contact);

        et_search = (EditText) findViewById(R.id.et_search);
        iv_search = (ImageView) findViewById(R.id.iv_search);

        mSearchResultView = (RecyclerView) findViewById(R.id.mSearchResultView);

        ll_to_contact.setOnClickListener(this);
        iv_search.setOnClickListener(this);

        //列表的实现
        mSearchResultView.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mAddFriendAdapter = new CommonAdapter<>(mList, new CommonAdapter.OnMoreBindDataListener<AddFriendModel>() {
            @Override
            public int getItemType(int position) {
                return mList.get(position).getType();
            }

            @Override
            public void onBindViewHolder(final AddFriendModel model, CommonViewHolder viewHolder, int type, int position) {
                if (type == TYPE_TITLE) {
                    viewHolder.setText(R.id.tv_title, model.getTitle());
                } else if (type == TYPE_CONTENT) {
                    //设置头像
                    viewHolder.setImageUrl(AddFriendActivity.this, R.id.iv_photo, model.getPhoto());
                    //设置性别
                    viewHolder.setImageResource(R.id.iv_sex,
                            model.isSex() ? R.drawable.img_boy_icon : R.drawable.img_girl_icon);
                    //设置昵称
                    viewHolder.setText(R.id.tv_nickname, model.getNickName());
                    //年龄
                    viewHolder.setText(R.id.tv_age, model.getAge() + getString(R.string.text_search_age));
                    //设置描述
                    viewHolder.setText(R.id.tv_desc, model.getDesc());

                    //点击事件
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UserInfoActivity.startActivity(AddFriendActivity.this,
                                    model.getUserId());
                        }
                    });
                }
            }

            @Override
            public int getLayoutId(int type) {
                if (type == TYPE_TITLE) {
                    return R.layout.layout_search_title_item;
                } else if (type == TYPE_CONTENT) {
                    return R.layout.layout_search_user_item;
                }
                return 0;
            }
        });

        mSearchResultView.setAdapter(mAddFriendAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //跳转到从通讯录导入
            case R.id.ll_to_contact:
                //处理权限
                if (checkPermissions(Manifest.permission.READ_CONTACTS)) {
                    // startActivity(new Intent(this, ContactFirendActivity.class));
                } else {
                    requestPermission(new String[]{Manifest.permission.READ_CONTACTS});
                }
                break;
            case R.id.iv_search:
                LogUtils.i("iv_search");
                queryNicknameUser();
                break;
        }
    }

    /**
     * 通过会员昵称查询
     */
    private void queryNicknameUser() {
        //1.获取搜索内容
        String name = et_search.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, getString(R.string.text_login_nickname_null),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        //2.过滤自己
        String nickname = SpUtils.getInstance().getString(Constants.SP_USER_NAME, "");;
        if (name.equals(nickname)) {
            Toast.makeText(this, getString(R.string.text_add_friend_no_me), Toast.LENGTH_SHORT).show();
            return;
        }

        //3.查询
        try{
            SearchFriendApi apiBase = new SearchFriendApi(name);
            apiBase.post(new ApiListener() {
                @Override
                public void success(ApiUtil api, JSONObject response) {
                    LogUtils.i("请求成功");
                    include_empty_view.setVisibility(View.VISIBLE);
                    mSearchResultView.setVisibility(View.GONE);
                    List <SearchFriendBean.SearchListBean> search_list = apiBase.mInfo.getSearch_list();
                    if (CommonUtils.isEmpty(apiBase.mInfo.getSearch_list())) {
                        //每次你查询有数据的话则清空
                        mList.clear();
                        include_empty_view.setVisibility(View.GONE);
                        mSearchResultView.setVisibility(View.VISIBLE);
                        for (int i=0; i < search_list.size(); i++ ) {
                            SearchFriendBean.SearchListBean m = search_list.get(i);
                            addTitle(getString(R.string.text_add_friend_title));
                            addContent(m);
                            mAddFriendAdapter.notifyDataSetChanged();
                        }
                    } else {
                        //显示空数据
                        include_empty_view.setVisibility(View.VISIBLE);
                        mSearchResultView.setVisibility(View.GONE);
                    }
                }
                @Override
                public void error(ApiUtil api, JSONObject response) {
                    LogUtils.i("请求失败");
                }
            });

        }catch (Exception ex) {
            ex.printStackTrace();
        }
        //推荐
        pushUser(name);
    }

    /**
     * 推荐好友
     * @param name 过滤所查询的会员
     */
    private void pushUser(String name) {
        //查询所有的好友 取10个
//        BmobManager.getInstance().queryAllUser(new FindListener<IMUser>() {
//            @Override
//            public void done(List<IMUser> list, BmobException e) {
//                if (e == null) {
//                    if (CommonUtils.isEmpty(list)) {
//                        addTitle(getString(R.string.text_add_friend_content));
//                        int num = (list.size() <= 100) ? list.size() : 100;
//                        for (int i = 0; i < num; i++) {
//                            //也不能自己推荐给自己
//                            String phoneNumber = BmobManager.getInstance().getUser().getMobilePhoneNumber();
//                            if (list.get(i).getMobilePhoneNumber().equals(phoneNumber)) {
//                                //跳过本次循环
//                                continue;
//                            }
//                            //也不能查询到所查找的好友
//                            if (list.get(i).getMobilePhoneNumber().equals(phone)) {
//                                //跳过本次循环
//                                continue;
//                            }
//
//                            addContent(list.get(i));
//                        }
//                        mAddFriendAdapter.notifyDataSetChanged();
//                    }
//                }
//            }
//        });
    }

    /**
     * 添加头部
     *
     * @param title
     */
    private void addTitle(String title) {
        AddFriendModel model = new AddFriendModel();
        model.setType(TYPE_TITLE);
        model.setTitle(title);
        mList.add(model);
    }

    /**
     * 添加内容
     *
     * @param m
     */
    private void addContent(SearchFriendBean.SearchListBean m) {
        AddFriendModel model = new AddFriendModel();
        model.setType(TYPE_CONTENT);
        model.setUserId(m.getMember_id());
        model.setPhoto(m.getAvatar());
        model.setSex(m.getGender().equals("m")?false:true);
        model.setAge(m.getAge());
        model.setNickName(m.getNickname());
        model.setDesc(m.getSignature());
        mList.add(model);
    }
}
