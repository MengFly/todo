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
import com.example.mengfei.todo.entity.Task;
import com.example.todolib.utils.ShareTools;

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
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open();
            }
        });
    }

    private void open() {
        if (Task.TASK_TYPE_EMAIL.equals(task.getTaskType())) {
            Intent intent = ShareTools.getSendEmailIntent(task.getDesc(), "", emailEt.getText().toString());
            if (isUsedIntentActivity(intent)) {
                startActivity(Intent.createChooser(intent, "选择应用"));
            } else {
                showSnackbar(okBtn, "没有对应的应用");
            }
        } else if (Task.TASK_TYPE_MOBILE.equals(task.getTaskType())) {
            Intent intent = ShareTools.getCallIntent(task.getDesc());
            if (isUsedIntentActivity(intent)) {
                startActivity(Intent.createChooser(intent, "选择应用"));
            }else {
                showSnackbar(okBtn, "没有对应的应用");
            }
        } else {
           WebActivity.StartWebActivityWithURL(mContext, task.getDesc());
        }
    }


    private void initUI() {
        taskTitleTv.setText(task.getTitle());
        taskDescTv.setText(getOpenSpann());
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
        emailEt = (EditText) findViewById(R.id.et_content);
        okBtn = (Button) findViewById(R.id.btn_ok);
    }

    public SpannableString getOpenSpann() {
        String showString = "";
        if (Task.TASK_TYPE_EMAIL.equals(task.getTaskType())) {
            showString += "email:";
            okBtn.setText("发送邮件");
            emailEt.setVisibility(View.VISIBLE);
        } else if (Task.TASK_TYPE_MOBILE.equals(task.getTaskType())) {
            showString += "tel:";
            okBtn.setText("拨打电话");
        } else {
            showString += "url:";
            okBtn.setText("打开网页");
        }
        showString += task.getDesc();
        SpannableString spannbleString = new SpannableString(showString);
        spannbleString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_text_main_dark)),
                0, showString.indexOf(":"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannbleString.setSpan(new RelativeSizeSpan(2.0f),0,showString.indexOf(":"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );
        return spannbleString;
    }
}
