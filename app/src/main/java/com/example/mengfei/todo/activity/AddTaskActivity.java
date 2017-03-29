package com.example.mengfei.todo.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.activity.inter.UiShower;
import com.example.mengfei.todo.entity.Task;
import com.example.mengfei.todo.entity.TaskManager;
import com.example.mengfei.todo.utils.dialog.DateTimeDialog;
import com.example.todolib.utils.CheckUtils;
import com.example.todolib.utils.date.DateTools;

import java.util.Date;

/**
 * 这是添加任务的界面
 * Created by mengfei on 2017/3/14.
 */
public class AddTaskActivity extends BaseActivity {

    private static final String INTENT_TASK_TYPE_LEY = "task type";
    private static final String INTENT_TASK_DESC_KEY = "task desc";

    private String extraText;//这个字符串是从外部传过来的字符串，用于支持在应用外部添加任务

    private LinearLayout descLayout;
    private EditText taskTitleEt;
    private EditText taskDescEt;

    private Date wantDoneDate;

    private String taskType;
    private String taskDesc;

    public static void openAddTaskActivity(Context context, String taskType, String desc) {
        context.startActivity(getOpenActivityIntent(context, taskType, desc));
    }

    public static Intent getOpenActivityIntent(Context context, String taskType, String desc) {
        Intent intent = new Intent(context, AddTaskActivity.class);
        intent.putExtra(INTENT_TASK_TYPE_LEY, taskType);
        intent.putExtra(INTENT_TASK_DESC_KEY, desc);
        return intent;
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
        if (extraText != null) {//优先处理外部的添加任务事件
            extraText = extraText.trim();
            addTaskFromOutApp();
            taskDescEt.setText(taskDesc);
        } else {
            if (!Task.TASK_TYPE_TEXT.equals(taskType)) {
                descLayout.setVisibility(View.GONE);
                taskDescEt.setText(taskDesc);
            }
        }
    }

    private void addTaskFromOutApp() {
        taskDesc = extraText;
        String dialogTip = null;//用于提示的文字
        if (CheckUtils.isEmail(extraText)) {
            taskType = Task.TASK_TYPE_EMAIL;
            dialogTip = "当前任务信息是一个Email链接，是否添加为Email任务";
        } else if (CheckUtils.isMobileExact(extraText) || CheckUtils.isTel(extraText)) {
            taskType = Task.TASK_TYPE_MOBILE;
            dialogTip = "当前任务信息是一个号码，是否添加为电话任务";
        } else if (CheckUtils.isURL(extraText)) {
            taskType = Task.TASK_TYPE_NET;
            dialogTip = "当前任务信息是一个URL链接，是否添加为URL任务";
        } else {
            taskType = Task.TASK_TYPE_TEXT;
        }
        if (dialogTip != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage(dialogTip);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    taskDescEt.setFocusable(false);
                }
            });
            //如果不添加为其他类型的任务则默认添加为text类型
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    taskType = Task.TASK_TYPE_TEXT;
                }
            });
            builder.create().show();
        }
    }


    private void initVriable() {
        Intent intent = getIntent();
        if (intent != null) {
            taskType = intent.getStringExtra(INTENT_TASK_TYPE_LEY);
            taskDesc = intent.getStringExtra(INTENT_TASK_DESC_KEY);
            extraText = intent.getStringExtra(Intent.EXTRA_TEXT);
        }
    }

    private void initView() {
        descLayout = (LinearLayout) findViewById(R.id.ly_task_desc);
        taskTitleEt = (EditText) findViewById(R.id.et_task_title);
        taskDescEt = (EditText) findViewById(R.id.et_task_desc);
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
            case R.id.menu_add_time:
                addTipTime(item);
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
