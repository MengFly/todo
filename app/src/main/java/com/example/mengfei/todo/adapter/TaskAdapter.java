package com.example.mengfei.todo.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.entity.Task;
import com.example.todolib.adapter.CommonAdapter;
import com.example.todolib.adapter.ViewHolder;
import com.example.todolib.view.widget.DateTextView;

import java.util.List;

/**
 * task adapter
 * Created by mengfei on 2017/3/14.
 */
public class TaskAdapter extends CommonAdapter<Task> {

    public TaskAdapter(Context montext, List<Task> mList, int mItemLayoutResorce) {
        super(montext, mList, mItemLayoutResorce);
    }

    @Override
    public void bindItemDatas(ViewHolder holder, Task bean) {
        ((TextView) holder.getView(R.id.tv_task_title)).setText(bean.getTitle());
        ((TextView) holder.getView(R.id.tv_task_desc)).setText(TextUtils.isEmpty(bean.getDesc()) ? "没有描述" : bean.getDesc());
        DateTextView dateTextView = (DateTextView) holder.getView(R.id.dtv_create_date);
        dateTextView.setShowMode(DateTextView.SHOW_MODE_ONLY_HOW_LONG);
        dateTextView.setDate(bean.getCreateDate());
    }

}
