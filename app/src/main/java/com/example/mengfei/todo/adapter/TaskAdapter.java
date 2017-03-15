package com.example.mengfei.todo.adapter;

import android.content.Context;
import android.widget.TextView;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.entity.Task;
import com.example.todolib.adapter.CommonAdapter;
import com.example.todolib.adapter.ViewHolder;
import com.example.todolib.view.widget.DateTextView;

import org.w3c.dom.Text;

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
        ((TextView)holder.getView(R.id.tv_task_title)).setText(bean.getTitle());
        ((TextView) holder.getView(R.id.tv_task_desc)).setText(bean.getDesc());
        ((DateTextView) holder.getView(R.id.dtv_create_date)).setDate(bean.getCreateDate());
        ((TextView) holder.getView(R.id.tv_chat_count)).setText(String.valueOf(bean.getTalks().size()));
    }

}
