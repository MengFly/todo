package com.example.mengfei.todo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.adapter.TaskAdapter;
import com.example.mengfei.todo.entity.TaskManager;


/**
 * 查看应用完成的任务的界面
 * Created by mengfei on 2017/3/17.
 */
public class TotalDoneTaskActivity extends BaseActivity {

    private Toolbar toolbar;
    private ListView doneTaskListView;
    private TaskAdapter doneTaskAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_done_task);
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
                ShowDoneTaskActivity.showDoneTask(mContext, doneTaskAdapter.getItem(position));
            }
        });
    }
}
