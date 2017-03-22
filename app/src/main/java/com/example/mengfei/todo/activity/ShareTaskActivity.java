package com.example.mengfei.todo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.entity.OneWords;
import com.example.mengfei.todo.entity.Talk;
import com.example.mengfei.todo.entity.Task;
import com.example.mengfei.todo.utils.image.ImageLoader;

import org.litepal.crud.DataSupport;
import org.w3c.dom.Text;

/**
 * 分享Task的Activity
 * Created by mengfei on 2017/3/21.
 */

public class ShareTaskActivity extends BaseActivity {

    private Task task;
    private static final String INTENT_KEY = "task";

    private Toolbar toolbar;
    private View shareLayout;
    private ImageView headerBackIV;
    private ImageView addImageIV;
    private LinearLayout chatLayout;

    private TextView taskTitleTv, taskDescTv, taskExtraTv;

    private Button shareBtn, saveBtn;

    public static void openShareTaskActivity(Context context, Task task) {
        Intent intent = new Intent(context, ShareTaskActivity.class);
        intent.putExtra(INTENT_KEY, task);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVirable();
        setContentView(R.layout.layout_activity_share_task);
        initView();
        initListener();
        initUI();
    }

    private void initListener() {
    }

    private void initUI() {
        taskTitleTv.setText(task.getTitle());
        taskTitleTv.setText(task.getDesc());
        ImageLoader.loadImage(mContext, DataSupport.findLast(OneWords.class).getPicture2(), headerBackIV, null);
        for (Talk talk : task.getTalks()) {
            TextView view = getTalkTextView(talk);
            chatLayout.addView(view);
        }
        taskExtraTv.setText("我用3天的时间完成了这个任务,感觉棒棒的呢~");
    }

    private TextView getTalkTextView(Talk talk) {
        TextView view = new TextView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        view.setLayoutParams(params);
        view.setPadding(10, 10, 10, 10);
        view.setTextSize(16);
        view.setText(talk.getTalkContent());
        return view;
    }

    private void initVirable() {
        Intent intent = getIntent();
        if (intent != null) {
            task = (Task) intent.getSerializableExtra(INTENT_KEY);
        }
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        shareLayout = findViewById(R.id.sv_share);
        headerBackIV = (ImageView) findViewById(R.id.iv_header_back);
        addImageIV = (ImageView) findViewById(R.id.iv_add_image);
        chatLayout = (LinearLayout) findViewById(R.id.ly_talk);
        taskTitleTv = (TextView) findViewById(R.id.tv_task_title);
        taskDescTv = (TextView) findViewById(R.id.tv_task_desc);
        taskExtraTv = (TextView) findViewById(R.id.tv_task_extra);
        shareBtn = (Button) findViewById(R.id.btn_share);
        saveBtn = (Button) findViewById(R.id.btn_save);
        setSupportActionBar(toolbar);
        initActionBar("分享应用", null, true);
    }
}
