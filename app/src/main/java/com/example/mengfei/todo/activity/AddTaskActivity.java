package com.example.mengfei.todo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.activity.inter.UiShower;
import com.example.mengfei.todo.entity.Task;
import com.example.mengfei.todo.entity.TaskManager;
import com.example.mengfei.todo.utils.dialog.DateTimeDialog;
import com.example.todolib.utils.date.DateTools;

import java.util.Date;

/**
 * 这是添加任务的界面
 * Created by mengfei on 2017/3/14.
 */
public class AddTaskActivity extends BaseActivity {

    private Toolbar toolbar;
    private EditText taskTitleEt;
    private EditText taskDescEt;
    private CardView setTimeCV;
    private TextView showSetTimeTV;

    private Date wantDoneDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_add_task);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initActionBar("Add Task", null, true);
        initView();
    }

    private void initView() {
        taskTitleEt = (EditText) findViewById(R.id.et_task_title);
        taskDescEt = (EditText) findViewById(R.id.et_task_desc);
        setTimeCV = (CardView) findViewById(R.id.cv_set_time);
        showSetTimeTV = (TextView) findViewById(R.id.tv_show_date_time);
        setTimeCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DateTimeDialog(mContext, new Date(), new UiShower<Date>() {
                    @Override
                    public void show(Date date) {
                        wantDoneDate = date;
                        showSetTimeTV.setText(DateTools.formatDate(date));
                    }
                }).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_task, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_ok:
                saveTask();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveTask() {
        String taskTitle = taskTitleEt.getText().toString();
        String taskDesc = taskDescEt.getText().toString();
        if (!TaskManager.checkTitleAndDesc(taskTitle, taskDesc)) {
            showSnackbar(getCurrentFocus(), "task can not empty");
        } else {
            final Task task = new Task(taskTitle, taskDesc);
            task.setWantDoneDate(wantDoneDate);
            if (TaskManager.saveTask(task)) {
                MainActivity.startMainWithMsg(mContext, "添加成功");
            } else {
                showSnackbar(getCurrentFocus(), "添加失败，请重新添加");
            }
        }
    }
}
