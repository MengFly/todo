package com.example.todolib.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Collections;
import java.util.List;

/**
 * 通用的Adapter 通过实现里面的bindItemDatas方法，即可将要绑定的数据绑定到视图上<br>
 *
 * @param <T> 要Adapter显示的数据的封装类<br>
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    protected Context mContext;

    protected List<T> mList;

    private int mItemLayoutResource;

    public CommonAdapter(Context mContext, List<T> mList, int mItemLayoutResorce) {
        this.mContext = mContext;
        this.mList = mList;
        this.mItemLayoutResource = mItemLayoutResorce;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    public void setItem(T item) {
        this.mList.add(item);
        this.notifyDataSetChanged();
    }

    public void setItem(T item, int index) {
        this.mList.add(index, item);
        this.notifyDataSetChanged();
    }

    public void removeItem(T item) {
        this.mList.remove(item);
        this.notifyDataSetChanged();
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.getHolder(mContext, position,
                convertView, parent, mItemLayoutResource);

        bindItemDatas(holder, mList.get(position));

        return holder.getConvertView();
    }

    public int indexOf(T t) {
        return mList.indexOf(t);
    }

    /**
     * 为ListView的Item绑定数据
     *
     * @param holder 通过holder的getView来获取到item的控件
     * @param bean   bean 数据
     */
    public abstract void bindItemDatas(ViewHolder holder, T bean);

}
