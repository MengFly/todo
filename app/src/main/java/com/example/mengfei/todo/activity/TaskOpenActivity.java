package com.example.mengfei.todo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.activity.inter.UiShower;
import com.example.mengfei.todo.entity.Task;
import com.example.mengfei.todo.entity.TaskManager;
import com.example.mengfei.todo.utils.dialog.DateTimeDialog;
import com.example.todolib.utils.ShareTools;
import com.example.todolib.utils.date.DateTools;

import java.util.Calendar;
import java.util.Date;

/**
 * 这是另外的包含打电话，发送email或者打开网页的Activity
 * Created by mengfei on 2017/3/25.
 */
public class TaskOpenActivity extends BaseActivity {
    private static final String INTENT_KEY = "task";

    private Task task;
    private TextView taskTitleTv;
    private TextView taskDescTv;
    private EditText emailEt;
    private Button okBtn;
    private TextView addTimeTv;

    public static void openTaskOpenActivity(Context context, Task task) {
        Intent intent = new Intent(context, TaskOpenActivity.class);
        intent.putExtra(INTENT_KEY, task);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVirable();
        setContentView(R.layout.layout_activity_task_open);
        initView();
        initUI();
        initListener();
    }


    private void initListener() {
        //添加提醒时间
        addTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DateTimeDialog(mContext, Calendar.getInstance().getTime(), new UiShower<Date>() {
                    @Override
                    public void show(Date date) {
                        task.setWantDoneDate(date);
                        addTimeTv.setText(DateTools.formatDate(date));
                        TaskManager.updateTask(task, null, null);
                    }
                }).show();
            }
        });
    }

    @Override
    public boolean supportSlideBack() {
        return false;
    }

    private void initUI() {
        taskTitleTv.setText(task.getTitle());
        if (task.getWantDoneDate() != null) {
            addTimeTv.setText(DateTools.formatDate(task.getWantDoneDate()));
        }
    }

    private void initVirable() {
        Intent intent = getIntent();
        if (intent != null) {
            task = (Task) intent.getSerializableExtra(INTENT_KEY);
        }
    }

    private void initView() {
        taskTitleTv = (TextView) findViewById(R.id.tv_task_title);
        taskDescTv = (TextView) findViewById(R.id.tv_task_desc);
        addTimeTv = (TextView) findViewById(R.id.tv_add_time);
        emailEt = (EditText) findViewById(R.id.et_content);
        okBtn = (Button) findViewById(R.id.btn_ok);
    }

}
