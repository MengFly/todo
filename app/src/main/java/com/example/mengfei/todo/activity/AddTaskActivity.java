package com.example.mengfei.todo.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.activity.inter.UiShower;
import com.example.mengfei.todo.entity.Task;
import com.example.mengfei.todo.entity.TaskManager;
import com.example.mengfei.todo.utils.dialog.DateTimeDialog;
import com.example.todolib.utils.CheckUtils;
import com.example.todolib.utils.DisplayUtils;
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

    private FloatingActionButton taskStyleTextBtn;
    private FloatingActionButton taskStylePhoneBtn;
    private FloatingActionButton taskStyleEmailBtn;
    private FloatingActionButton taskStyleNetBtn;
    private FloatingActionButton taskStyleShowBtn;
    private boolean btnIsOpen = false;//标志着底部的task类型的Btn是否展开

    private CoordinatorLayout taskStyleLy;

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
            taskStyleLy.setVisibility(View.GONE);
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
        taskStyleLy = (CoordinatorLayout) findViewById(R.id.ly_task_style);
        taskStyleTextBtn = (FloatingActionButton) findViewById(R.id.btn_style_text);
        taskStylePhoneBtn = (FloatingActionButton) findViewById(R.id.btn_style_phone);
        taskStyleEmailBtn = (FloatingActionButton) findViewById(R.id.btn_style_email);
        taskStyleNetBtn = (FloatingActionButton) findViewById(R.id.btn_style_url);
        taskStyleShowBtn = (FloatingActionButton) findViewById(R.id.btn_show_style);
        initListener();
    }

    private void initListener() {
        taskStyleShowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOrCloseBtnLy();
            }
        });
        taskStyleTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnIsOpen) {
                    openOrCloseBtnLy();
                    taskStyleShowBtn.setImageResource(R.drawable.ic_task_type_text);
                    taskType = Task.TASK_TYPE_TEXT;
                    taskDescEt.setHint("输入任务描述");
                }
            }
        });
        taskStylePhoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnIsOpen) {
                    openOrCloseBtnLy();
                    taskStyleShowBtn.setImageResource(android.R.drawable.stat_sys_phone_call);
                    taskType = Task.TASK_TYPE_MOBILE;
                    taskDescEt.setHint("在这里输入要拨打的电话或手机号码");
                }
            }
        });
        taskStyleEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnIsOpen) {
                    openOrCloseBtnLy();
                    taskStyleShowBtn.setImageResource(R.drawable.ic_task_type_email);
                    taskType = Task.TASK_TYPE_EMAIL;
                    taskDescEt.setHint("在这里输入要发送Email的Email地址");
                }
            }
        });
        taskStyleNetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnIsOpen) {
                    openOrCloseBtnLy();
                    taskStyleShowBtn.setImageResource(R.drawable.ic_task_type_net);
                    taskType = Task.TASK_TYPE_NET;
                    taskDescEt.setHint("在这里输入要访问的网络地址");
                }
            }
        });
    }

    //打开或者关闭Btn的按钮
    public void openOrCloseBtnLy() {
        if (btnIsOpen) {
            closeBtn();
        } else {
            openBtn();
        }
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
            final Task task = new Task(taskTitle, taskDesc.trim());
            task.setTaskType(taskType);
            task.setWantDoneDate(wantDoneDate);
            //先检查用户输入是否符合标准
            if (checkStyle(taskType, task.getDesc())) {
                if (TaskManager.saveTask(task)) {
                    MainActivity.startMainWithMsg(mContext, "添加成功");
                    finish();//添加完成之后退出此界面
                } else {
                    showSnackbar(getCurrentFocus(), "添加失败，请重新添加");
                }
            }

        }
    }

    private boolean checkStyle(String taskType, String taskDesc) {
        if (Task.TASK_TYPE_EMAIL.equals(taskType)) {
            if (CheckUtils.isEmail(taskDesc)) {
                return true;
            } else {
                showSnackbar(taskStyleLy, "您输入的Email地址不合法");
                return false;
            }
        } else if (Task.TASK_TYPE_MOBILE.equals(taskType)) {
            if (CheckUtils.isMobileExact(taskDesc) || CheckUtils.isTel(taskDesc)) {
                return true;
            } else {
                showSnackbar(taskStyleLy,"您输入的电话或手机号码不合法");
                return false;
            }
        } else if (Task.TASK_TYPE_NET.equals(taskType)) {
            if (CheckUtils.isURL(taskDesc)) {
                return true;
            } else {
                showSnackbar(taskStyleLy, "您输入的网络地址不合法");
                return false;
            }
        } else {
            return true;
        }
    }

    public void openBtn() {
        btnIsOpen = true;
        int translatenWidht = DisplayUtils.dip2px(mContext, 60);//移动的动作
        float showTranslationX = taskStyleShowBtn.getTranslationX();
        ObjectAnimator animator = ObjectAnimator.ofFloat(taskStyleEmailBtn, "translationX", showTranslationX, showTranslationX - translatenWidht);
        animator.setDuration(500);
        animator.start();
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(taskStyleNetBtn, "translationX", showTranslationX, showTranslationX - 2 * translatenWidht);
        animator2.setDuration(500);
        animator2.start();
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(taskStylePhoneBtn, "translationX", showTranslationX, showTranslationX + translatenWidht);
        animator3.setDuration(500);
        animator3.start();
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(taskStyleTextBtn, "translationX", showTranslationX, showTranslationX + 2 * translatenWidht);
        animator4.setDuration(500);
        animator4.start();
    }

    public void closeBtn() {
        btnIsOpen = false;
        float showTranslationX = taskStyleShowBtn.getTranslationX();
        ObjectAnimator animator = ObjectAnimator.ofFloat(taskStyleEmailBtn, "translationX", taskStyleEmailBtn.getTranslationX(), showTranslationX);
        animator.setDuration(500);
        animator.start();
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(taskStyleNetBtn, "translationX", taskStyleNetBtn.getTranslationX(), showTranslationX);
        animator2.setDuration(500);
        animator2.start();
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(taskStylePhoneBtn, "translationX", taskStylePhoneBtn.getTranslationX(), showTranslationX);
        animator3.setDuration(500);
        animator3.start();
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(taskStyleTextBtn, "translationX", taskStyleTextBtn.getTranslationX(), showTranslationX);
        animator4.setDuration(500);
        animator4.start();
    }
}
