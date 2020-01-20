package com.qaqzz.common.widget.recycler;

/**
 * @author qaqzz
 * @description TODO
 * @date 2019/12/13 16:27
 */
public interface AdapterCallback<Data> {
    void update(Data data, RecyclerAdapter.ViewHolder<Data> holder);
}
