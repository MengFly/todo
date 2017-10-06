package com.example.mengfei.todo.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.entity.Task;
import com.example.mengfei.todo.entity.Action;
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
    public void bindItemDatas(final ViewHolder holder, Task bean) {
        //init Title and Desc
        ((TextView) holder.getView(R.id.tv_task_title)).setText(bean.getTitle());
        ((TextView) holder.getView(R.id.tv_task_desc)).setText(TextUtils.isEmpty(bean.getDesc()) ? "没有描述" : bean.getDesc());

        //init createDate
        DateTextView dateTextView = (DateTextView) holder.getView(R.id.dtv_create_date);
        dateTextView.setShowMode(DateTextView.SHOW_MODE_ONLY_HOW_LONG);
        dateTextView.setDate(bean.getCreateDate());

        //init Task Action
        final Action action = bean.getAction();
        if (action == null) {
            holder.getView(R.id.ll_action).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.ll_action).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.ib_action)).setImageBitmap(action.getShowIcon());
            ((TextView) holder.getView(R.id.tv_action_type)).setText(action.type);
            (holder.getView(R.id.ib_action)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    action.doAction(mContext);
                }
            });
        }

    }

}
