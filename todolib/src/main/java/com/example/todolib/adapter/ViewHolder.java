package com.example.todolib.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ViewHolder {

    private View mConvertView;
    private int mPosition;

    private SparseArray<View> mView;// 用于存储item里面的控件

    private ViewHolder(Context context, int position, ViewGroup parent,
                       int layoutId) {
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        mView = new SparseArray<View>();
        this.mPosition = position;
        mConvertView.setTag(this);
    }

    /**
     * 获取子布局的Item--Position（位置）
     *
     * @return position
     */
    public int getPosition() {
        return mPosition;
    }

    /**
     * 根据布局的item的里面控件的id来获取控件
     *
     * @param id 控件的id
     * @return 控件
     */
    public View getView(int id) {
        if (mView.get(id) == null) {
            View itemView = mConvertView.findViewById(id);
            this.mView.put(id, itemView);
            return itemView;
        } else {
            return mView.get(id);
        }
    }

    /**
     * 获取已经加载的View
     *
     * @return 已经加载的View
     */
    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 获取ViewHolder实例
     *
     * @param context  context
     * @param position position
     * @param parent   parent
     * @param layoutId layouytId
     * @return viewHolder
     */
    public static ViewHolder getHolder(Context context, int position,
                                       View convertView, ViewGroup parent, int layoutId) {

        if (convertView == null) {
            return new ViewHolder(context, position, parent, layoutId);
        } else {
            return (ViewHolder) convertView.getTag();
        }
    }

}
