package com.example.todolib.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 通用的Adapter 通过实现里面的bindItemDatas方法，即可将要绑定的数据绑定到视图上<br>
 *
 * @param <T> 要Adapter显示的数据的封装类<br>
 */
public abstract class CommonAdapter<T> extends BaseAdapter implements Filterable {

    protected Context mContext;

    protected List<T> mList;
    protected List<T> showList;
    protected ArrayFilter mFilter;

    private int mItemLayoutResource;

    public CommonAdapter(Context mContext, List<T> mList, int mItemLayoutResorce) {
        this.mContext = mContext;
        this.mList = mList;
        this.showList = this.mList;
        this.mItemLayoutResource = mItemLayoutResorce;
    }

    @Override
    public int getCount() {
        return showList.size();
    }

    public void setItem(T item) {
        this.mList.add(item);
        this.notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
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
        return showList.get(position);
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

        bindItemDatas(holder, showList.get(position));

        return holder.getConvertView();
    }

    public int indexOf(T t) {
        return showList.indexOf(t);
    }

    public boolean isFilter(CharSequence str, T t) {
        return true;
    }


    /**
     * 为ListView的Item绑定数据
     *
     * @param holder 通过holder的getView来获取到item的控件
     * @param bean   bean 数据
     */
    public abstract void bindItemDatas(ViewHolder holder, T bean);

    private class ArrayFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<T> list;
            if (TextUtils.isEmpty(constraint)) {
                list = mList;
            } else {
                list = new ArrayList<>();
                for (T t : mList) {
                    if (isFilter(constraint, t)) {
                        list.add(t);
                    }
                }
            }
            results.count = list.size();
            results.values = list;
            return results;
        }

        //通知适配器更新界面
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            showList = (List<T>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

}
