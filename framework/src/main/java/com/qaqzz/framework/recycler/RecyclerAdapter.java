package com.qaqzz.framework.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qaqzz.framework.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author qaqzz
 * @description TODO
 * @date 2019/12/13 16:27
 */
public abstract class RecyclerAdapter<Data>
        extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<Data>>
        implements View.OnClickListener, View.OnLongClickListener,
        AdapterCallback<Data>{
    private final List<Data> mDataList;
    private AdapterListener<Data> mListener;

    /**
     * 构造函数模块
     */
    public RecyclerAdapter() {
        this(null);
    }

    public RecyclerAdapter(AdapterListener<Data> listener) {
        this(new ArrayList<Data>(), listener);
    }

    public RecyclerAdapter(List<Data> dataList, AdapterListener<Data> listener) {
        this.mDataList = dataList;
        this.mListener= listener;
    }
    /**
     * 复写默认的布局类型返回
     * @param position 坐标
     * @return 类型, 其实复写后返回的都是XML文件的ID
     */
    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, mDataList.get(position));
    }

    /**
     * 得到布局的类型
     * @param position 坐标
     * @param data 当前的数据
     * @return xml文件的ID, 用于创建ViewHolder
     */
    @LayoutRes
    protected abstract int getItemViewType(int position, Data data);

    /**
     * 创建一个ViewHolder
     * @param parent RecyclerAdapter
     * @param viewType 界面类型, 约定为xml布局ID
     * @return ViewHolder
     */
    @NonNull
    @Override
    public ViewHolder<Data> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 得到LayoutInflater用户吧XML初始化为View
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // 把XML id为viewType的问卷初始化为一个root View
        View root = inflater.inflate(viewType, parent, false);
        // 通过子类必须实现的方法, 得到一个ViewHolder
        ViewHolder<Data> holder = onCreateViewHolder(root, viewType);

        // 设置View的tag 为ViewHolder, 进行双向绑定
        root.setTag(R.id.tag_recycler_holder, holder);

        //设置事件点击
        root.setOnClickListener(this);
        root.setOnLongClickListener(this);

        // 进行界面注解绑定
        holder.unbinder = ButterKnife.bind(holder, root);

        //绑定callback
        holder.callback = this;

        return holder;
    }

    /**
     * 得到一个新的ViewHolder
     * @param root 根布局
     * @param viewType 布局类型, 其实就是XML的ID
     * @return ViewHolder
     */
    protected abstract ViewHolder<Data> onCreateViewHolder(View root, int viewType);

    /**
     * 绑定数据到一个holder上
     * @param holder ViewHolder
     * @param position 数据坐标
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder<Data> holder, int position) {
        // 得到需要绑定的数据
        Data data = mDataList.get(position);
        // 触发Holder的绑定方法
        holder.bind(data);
    }

    /**
     * 得到当前集合的数据量
     * @return
     */
    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 插入一条数据并通知插入
     * @param data
     */
    public void add(Data data) {
        mDataList.add(data);
        notifyItemInserted( mDataList.size() - 1 );
    }

    /**
     * 插入一堆数据, 并通知这段集合更新
     * @param dataList
     */
    public void add(Data... dataList) {
        if (dataList != null && dataList.length > 0 ) {
            int startPos = mDataList.size();
            Collections.addAll(mDataList, dataList);
            notifyItemRangeInserted(startPos, dataList.length);
        }
    }

    /**
     * 插入一堆数据, 并通知这段集合更新
     * @param dataList
     */
    public void add(Collection<Data> dataList) {
        if (dataList != null && dataList.size() > 0 ) {
            int startPos = mDataList.size();
            mDataList.addAll(dataList);
            notifyItemRangeInserted(startPos, dataList.size());
        }
    }

    /**
     * 删除操作
     */
    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    /**
     * 替换一个新的集合, 其中包含了清空
     * @param dataList 一个新的集合
     */
    private void replace(Collection<Data> dataList) {
        mDataList.clear();
        if (dataList == null || dataList.size() == 0 ) {
            return;
        }
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
        if (this.mListener != null) {
            // 得到 ViewHolder  当前对应的适配器的坐标
            int pos = viewHolder.getAdapterPosition();
            // 回调方法
            this.mListener.onItemClick(viewHolder, mDataList.get(pos));
        }
    }

    @Override
    public boolean onLongClick(View v) {
        ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
        if (this.mListener != null) {
            // 得到 ViewHolder  当前对应的适配器的坐标
            int pos = viewHolder.getAdapterPosition();
            // 回调方法
            this.mListener.onItemLongClick(viewHolder, mDataList.get(pos));
            return true;
        }
        return false;
    }

    /**
     * 设置适配器监听
     * @param adapterListener
     */
    public void setListener(AdapterListener<Data> adapterListener) {
        this.mListener = adapterListener;
    }

    /**
     * 我们的自定义监听器
     * @param <Data> 泛型
     */
    public interface AdapterListener<Data>{
        // 当cell点击的时候触发
        void onItemClick(RecyclerAdapter.ViewHolder holder, Data data);
        // 当cell长按时触发
        void onItemLongClick(RecyclerAdapter.ViewHolder holder, Data data);
    }

    /**
     * 自定义的ViewHolder
     * @param <Data> 泛型类型
     */
    public static abstract class ViewHolder<Data> extends RecyclerView.ViewHolder {
        private Unbinder unbinder;
        private AdapterCallback<Data> callback;
        protected Data mData;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        /**
         * 用于绑定数据的触发
         * @param data
         */
        void bind(Data data) {
            this.mData = data;
        }

        /**
         * 当触发绑定数据的时候 的回调; 必须复写
         * @param data
         */
        protected abstract void onBind (Data data);


        /**
         * Holder自己对自己对应的Data进行更新操作
         * @param data
         */
        public void updateData(Data data) {
            if (this.callback != null ) {
                this.callback.update(data, this);
            }
        }
    }
}
