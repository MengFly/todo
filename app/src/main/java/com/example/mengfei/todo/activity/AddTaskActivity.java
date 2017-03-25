package com.example.mengfei.todo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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

    private static final String INTENT_TASK_TYPE_LEY = "task type";
    private static final String INTENT_TASK_DESC_KEY = "task desc";

    private LinearLayout descLayout;
    private EditText taskTitleEt;
    private EditText taskDescEt;
    private View setDateTimeLy;
    private TextView showSetTimeTV;

    private Date wantDoneDate;

    private String taskType;
    private String taskDesc;

    public static void openAddTaskActivity(Context context, String taskType, String desc) {
        Intent intent = new Intent(context, AddTaskActivity.class);
        intent.putExtra(INTENT_TASK_TYPE_LEY, taskType);
        intent.putExtra(INTENT_TASK_DESC_KEY, desc);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVriable();
        setContentView(R.layout.layout_activity_add_task);
        initActionBar("Add Task", null, true);
        initView();
        initUI();
    }

    private void initUI() {
        if (!Task.TASK_TYPE_TEXT.equals(taskType)) {
            descLayout.setVisibility(View.GONE);
            taskDescEt.setText(taskDesc);
        }
    }

    private void initVriable() {
        Intent intent = getIntent();
        if (intent != null) {
            taskType = intent.getStringExtra(INTENT_TASK_TYPE_LEY);
            taskDesc = intent.getStringExtra(INTENT_TASK_DESC_KEY);
        }
    }

    private void initView() {
        descLayout = (LinearLayout) findViewById(R.id.ly_task_desc);
        taskTitleEt = (EditText) findViewById(R.id.et_task_title);
        taskDescEt = (EditText) findViewById(R.id.et_task_desc);
        setDateTimeLy = findViewById(R.id.ly_set_date_time);
        showSetTimeTV = (TextView) findViewById(R.id.tv_show_date_time);
        setDateTimeLy.setOnClickListener(new View.OnClickListener() {
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
            task.setTaskType(taskType);
            task.setWantDoneDate(wantDoneDate);
            if (TaskManager.saveTask(task)) {
                MainActivity.startMainWithMsg(mContext, "添加成功");
            } else {
                showSnackbar(getCurrentFocus(), "添加失败，请重新添加");
            }
        }
    }
}
