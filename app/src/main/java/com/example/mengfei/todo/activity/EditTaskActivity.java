package com.example.mengfei.todo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.adapter.TalkAdapter;
import com.example.mengfei.todo.entity.Talk;
import com.example.mengfei.todo.entity.Task;
import com.example.mengfei.todo.entity.TaskManager;
import com.example.todolib.view.widget.CustomDialogCreater;

import java.util.Collections;
import java.util.List;

/**
 * Created by mengfei on 2017/3/14.
 * 添加任务的Activity
 */
public class EditTaskActivity extends BaseActivity {

    private EditText taskTitleET, taskDescET;
    private Button doneBtn, okEditBtn;
    private ListView talkListView;
    private Toolbar toolbar;
    private View headerView;
    private View talkAreaTitle;

    private ImageButton addChatBtn;
    private TalkAdapter adapter;

    private Task task;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            finish();
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_add_task);
        headerView = getLayoutInflater().inflate(R.layout.layout_add_task_header, null);
        headerView.findViewById(R.id.appbar_layout).setVisibility(View.GONE);
        talkAreaTitle = getLayoutInflater().inflate(R.layout.layout_title_bar, null);
        addChatBtn = (ImageButton) talkAreaTitle.findViewById(R.id.ibtn_add_chat);
        toolbar = ((Toolbar) findViewById(R.id.toolbar));
        setSupportActionBar(toolbar);
        doneBtn = (Button) findViewById(R.id.btn_done_task);
        okEditBtn = (Button) findViewById(R.id.btn_ok_edit);
        Intent intent = getIntent();
        if (intent != null) {
            task = (Task) intent.getSerializableExtra("task");
        }
        if (task != null) {
            initActionBar(task.getTitle(), task.getDesc(), true);
        } else {
            showSnackbar(doneBtn, "task layout_title_bar 不合法, 3 秒后退出此界面");
            handler.sendEmptyMessageDelayed(0x0001, 3000);
        }

        taskTitleET = ((EditText) headerView.findViewById(R.id.et_task_title));
        taskDescET = ((EditText) headerView.findViewById(R.id.et_task_desc));
        talkListView = (ListView) findViewById(R.id.lv_talk_items);
        talkListView.addHeaderView(headerView);
        talkListView.addHeaderView(talkAreaTitle);
        initUI();
        initListener();
    }

    private void initListener() {
        addChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText[] inputText = new EditText[1];
                android.support.v7.app.AlertDialog dialog = CustomDialogCreater.getViewDialog(mContext, R.layout.layout_dialog_add_chat, new CustomDialogCreater.ResourceBinder() {
                    @Override
                    public void bindView(View rootView) {
                        inputText[0] = (EditText) rootView.findViewById(R.id.et_talk_input);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextUtils.isEmpty(inputText[0].getText().toString())) {
                            showSnackbar(getCurrentFocus(), "没有输入");
                        } else {
                            Talk talk = new Talk(inputText[0].getText().toString());
                            talk.setTaskId(task.getTaskId());
                            adapter.setItem(talk);
                            talk.save();
                        }
                    }
                });
                dialog.show();
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskManager.completeTask(task);
                MainActivity.startMainWithMsg(mContext, "恭喜完成了一个任务，完成的任务可以在已经完成的任务列表查看");
                finish();
            }
        });
        okEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskTitle = taskTitleET.getText().toString();
                String taskDesc = taskDescET.getText().toString();
                showSnackbar(getCurrentFocus(), taskTitle + taskDesc + task.getTaskId());
                if (TaskManager.checkTitleAndDesc(taskTitle, taskDesc)) {
                    TaskManager.updateTask(task, taskTitle, taskDesc);
                    MainActivity.startMainWithMsg(mContext, "修改成功");
                    finish();
                } else {
                    showSnackbar(getCurrentFocus(), "task title and desc can't empty!!");
                }
            }
        });

    }


    private void initUI() {
        if (task != null) {
            taskTitleET.setText(task.getTitle());
            taskDescET.setText(task.getDesc());
            List<Talk> talks = task.getTalks();
            if (talks != null) {
                adapter = new TalkAdapter(mContext, talks, R.layout.layout_item_talk);
            } else {
                adapter = new TalkAdapter(mContext, Collections.<Talk>emptyList(), R.layout.layout_item_talk);
            }
        } else {
            adapter = new TalkAdapter(mContext, Collections.<Talk>emptyList(), R.layout.layout_item_talk);
        }
        talkListView.setAdapter(adapter);
    }
}
