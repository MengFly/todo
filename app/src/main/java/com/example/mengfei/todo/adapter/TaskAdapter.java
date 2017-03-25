package com.example.mengfei.todo.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.entity.Task;
import com.example.todolib.adapter.CommonAdapter;
import com.example.todolib.adapter.ViewHolder;
import com.example.todolib.en_de_code.impl.DecodeAES;
import com.example.todolib.view.widget.DateTextView;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * task adapter
 * Created by mengfei on 2017/3/14.
 */
public class TaskAdapter extends CommonAdapter<Task> {
    private static final String TAG = "adapter";

    public TaskAdapter(Context montext, List<Task> mList, int mItemLayoutResorce) {
        super(montext, mList, mItemLayoutResorce);
    }

    @Override
    public void bindItemDatas(ViewHolder holder, Task bean) {
        ((TextView) holder.getView(R.id.tv_task_title)).setText(bean.getTitle());
        ((TextView) holder.getView(R.id.tv_task_desc)).setText(bean.getDesc());
        ((DateTextView) holder.getView(R.id.dtv_create_date)).setDate(bean.getCreateDate());
        ((TextView) holder.getView(R.id.tv_chat_count)).setText(String.valueOf(bean.getTalks().size()));
        ((ImageView) holder.getView(R.id.iv_task_stat)).setImageDrawable(null);
        if (bean.getWantDoneDate() != null && !bean.isCompleted()) {
            if (bean.getWantDoneDate().before(Calendar.getInstance().getTime())) {
                ((ImageView) holder.getView(R.id.iv_task_stat)).setImageResource(R.drawable.ic_time_over);
            } else {
                ((ImageView) holder.getView(R.id.iv_task_stat)).setImageResource(R.drawable.ic_time_tip);
            }
        }
        if (Task.TASK_TYPE_NET.equals(bean.getTaskType())) {
            ((ImageView) holder.getView(R.id.iv_task_type)).setImageResource(R.drawable.ic_task_type_net);
        } else if (Task.TASK_TYPE_MOBILE.equals(bean.getTaskType())) {
            ((ImageView) holder.getView(R.id.iv_task_type)).setImageResource(android.R.drawable.ic_menu_call);
        } else if (Task.TASK_TYPE_EMAIL.equals(bean.getTaskType())) {
            ((ImageView) holder.getView(R.id.iv_task_type)).setImageResource(R.drawable.ic_task_type_email);
        } else {
            ((ImageView) holder.getView(R.id.iv_task_type)).setImageResource(R.drawable.ic_task_type_text);
        }

    }

}
