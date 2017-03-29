package com.example.mengfei.todo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.adapter.TaskAdapter;
import com.example.mengfei.todo.entity.Task;
import com.example.mengfei.todo.entity.TaskManager;


/**
 * 查看应用完成的任务的界面
 * Created by mengfei on 2017/3/17.
 */
public class TotalDoneTaskActivity extends BaseActivity {

    private ListView doneTaskListView;
    private TaskAdapter doneTaskAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_done_task);
        initView();
    }

    private void initView() {
        initActionBar("已经完成的任务", null, true);
        doneTaskListView = (ListView) findViewById(R.id.lv_done_task);
        TextView emptyView = (TextView) findViewById(R.id.tv_empty_view);
        doneTaskAdapter = new TaskAdapter(mContext, TaskManager.getCompletedTask(), R.layout.layout_item_task);
        doneTaskListView.setAdapter(doneTaskAdapter);
        doneTaskListView.setEmptyView(emptyView);
        initListener();
    }

    private void initListener() {
        doneTaskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = doneTaskAdapter.getItem(position);
                if (Task.TASK_TYPE_TEXT.equals(task.getTaskType())) {
                    EditTaskActivity.openEditTaskActivity(mContext, task, false);
                } else {
                    TaskOpenActivity.openTaskOpenActivity(mContext, task);
                }
            }
        });
    }
}
