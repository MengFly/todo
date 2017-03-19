package com.example.mengfei.todo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.entity.Talk;
import com.example.mengfei.todo.entity.Task;
import com.example.todolib.view.widget.DateTextView;

/**
 * Created by mengfei on 2017/3/18.
 */

public class ShowDoneTaskActivity extends BaseActivity {

    private Task doneTask;
    private static final String INTENT_SHOW_DONE_TASK_KEY = "done task";
    private DateTextView createDate;
    private DateTextView doneDate;
    private CollapsingToolbarLayout collLayout;
    private Toolbar toolbar;
    private LinearLayout chatLayout;


    public static void showDoneTask(Context context, Task task) {
        Intent intent = new Intent(context, ShowDoneTaskActivity.class);
        intent.putExtra(INTENT_SHOW_DONE_TASK_KEY, task);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVriable();
        setContentView(R.layout.layout_activity_show_done_task);
        createDate = (DateTextView) findViewById(R.id.dtv_create_date);
        doneDate = (DateTextView) findViewById(R.id.dtv_done_date);
        collLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        chatLayout = (LinearLayout) findViewById(R.id.ly_talk);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initUI();
    }

    private void initUI() {
        collLayout.setTitle(doneTask.getTitle());
        createDate.setDate(doneTask.getCreateDate());
        doneDate.setDate(doneTask.getDoneDate());
        initActionBar(doneTask.getTitle(), doneTask.getDesc(), true);
        for (Talk talk : doneTask.getTalks()) {
            View view = getChildTalkView(talk);
            chatLayout.addView(view);
        }

    }

    private View getChildTalkView(Talk talk) {
        View view = getLayoutInflater().inflate(R.layout.layout_child_talk,null);
        ((TextView) view.findViewById(R.id.tv_title)).setText(talk.getTalkContent());
        ((DateTextView) view.findViewById(R.id.dtv_date)).setDate(talk.getTalkDate());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.leftMargin=10;
        params.rightMargin=10;
        params.topMargin = 24;
        view.setLayoutParams(params);
        return view;
    }

    private void initVriable() {
        Intent intent = getIntent();
        if (intent!= null) {
            doneTask = (Task) intent.getSerializableExtra(INTENT_SHOW_DONE_TASK_KEY);
        }
//        talkAdapter = new TalkAdapter(mContext, doneTask.getTalks(), R.layout.layout_item_talk);
    }
}