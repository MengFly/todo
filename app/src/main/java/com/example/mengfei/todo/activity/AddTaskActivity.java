package com.example.mengfei.todo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

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

    public static final int REQUEST_CODE = 0x45;

    private EditText taskTitleEt;
    private EditText taskDescEt;

    private Date wantDoneDate;

    public static void openAddTaskActivityForResult(Activity context) {
        Intent intent = new Intent(context, AddTaskActivity.class);
        context.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_add_task);
        initActionBar("Add Task", null, true);
        initView();
    }

    private void initView() {
        taskTitleEt = (EditText) findViewById(R.id.et_task_title);
        taskDescEt = (EditText) findViewById(R.id.et_task_desc);
        findViewById(R.id.select_menu).setVisibility(View.GONE);
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

    //添加提醒时间
    private void addTipTime(final MenuItem item) {
        new DateTimeDialog(mContext, new Date(), new UiShower<Date>() {
            @Override
            public void show(Date date) {
                wantDoneDate = date;
                item.setTitle(DateTools.formatDate(wantDoneDate));
            }
        }).show();
    }

    private void saveTask() {
        String taskTitle = taskTitleEt.getText().toString();
        String taskDesc = taskDescEt.getText().toString();
        if (!TaskManager.checkTitleAndDesc(taskTitle, taskDesc)) {
            showSnackbar(getCurrentFocus(), "task can not empty");
        } else {
            final Task task = new Task(taskTitle, taskDesc.trim());
            task.setWantDoneDate(wantDoneDate);
            boolean isSave = task.save();
            if (isSave) {
                Intent intent = new Intent();
                intent.putExtra("task", task);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                showSnackbar(getCurrentFocus(), "保存失败");
            }
        }

    }

}
