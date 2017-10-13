package com.example.mengfei.todo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.entity.Action;
import com.example.mengfei.todo.entity.Task;

/**
 * 分享Task的Activity
 * Created by mengfei on 2017/3/21.
 */

public class ShareTaskActivity extends BaseActivity {

    private Task task;
    private static final String INTENT_KEY = "task";

    private TextView taskCreateDateTv, taskWantDoneDateTv;
    private TextView taskTitleTv, taskDescTv;

    public static void start(Context context, Task task) {
        Intent starter = new Intent(context, ShareTaskActivity.class);
        starter.putExtra(INTENT_KEY, task);
        context.startActivity(starter);
    }

    //Action
    private LinearLayout actionLL;
    private TextView actionTitleTV;
    private TextView actionDescTV;
    private ImageButton doActionIB;
    private ImageView actionIconIv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVirable();
        setContentView(R.layout.layout_activity_share_task);
        initView();
        initUI();
    }

    private void initUI() {
        taskTitleTv.setText(task.getTitle());
        taskDescTv.setText(task.getDesc());
        taskCreateDateTv.setText(task.getCreateDate().toString());
        taskWantDoneDateTv.setText(task.getWantDoneDate().toString());
        final Action action = task.getAction();
        if (action != null) {
            actionLL.setVisibility(View.VISIBLE);
            actionTitleTV.setText(action.title);
            actionDescTV.setText(action.desc);
            actionIconIv.setImageBitmap(action.getShowIcon());
            doActionIB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    action.doAction(mContext);
                }
            });
        } else {
            actionLL.setVisibility(View.GONE);
        }
    }

    private void initVirable() {
        Intent intent = getIntent();
        if (intent != null) {
            task = (Task) intent.getSerializableExtra(INTENT_KEY);
        }
    }

    private void initView() {
        taskCreateDateTv = (TextView) findViewById(R.id.tv_task_create_date);
        taskWantDoneDateTv = (TextView) findViewById(R.id.tv_task_want_done_date);
        taskTitleTv = (TextView) findViewById(R.id.tv_task_title);
        taskDescTv = (TextView) findViewById(R.id.tv_task_desc);

        actionLL = (LinearLayout) findViewById(R.id.ll_action);
        actionIconIv = (ImageView) findViewById(R.id.iv_action_icon);
        actionTitleTV = (TextView) findViewById(R.id.tv_action_title);
        actionDescTV = (TextView) findViewById(R.id.tv_action_desc);
        doActionIB = (ImageButton) findViewById(R.id.ib_do_action);
    }
}
