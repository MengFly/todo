package com.example.mengfei.todo.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.mengfei.todo.AppConfig;
import com.example.mengfei.todo.R;
import com.example.mengfei.todo.entity.Task;
import com.example.mengfei.todo.entity.TaskManager;
import com.example.mengfei.todo.utils.dialog.CheckBoxDialog;

/**
 * Created by mengfei on 2017/3/14.
 * 添加任务的Activity
 */
public class EditTaskActivity extends BaseActivity {

    private static final String INTENT_KEY_TASK = "task";
    public static final int REQUEST_CODE = 0x123;

    private EditText taskTitleET, taskDescET;
    private Task task;

    /**
     * 打开编辑Task的界面
     *
     * @param sendTask 要显示的Task
     */
    public static void openEditTaskActivity(Activity context, Task sendTask) {
        Intent intent = new Intent(context, EditTaskActivity.class);
        intent.putExtra(INTENT_KEY_TASK, sendTask);
        context.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        task = (Task) getIntent().getSerializableExtra(INTENT_KEY_TASK);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_add_task);
        taskTitleET = (EditText) findViewById(R.id.et_task_title);
        taskDescET = (EditText) findViewById(R.id.et_task_desc);
        taskTitleET.setText(task.getTitle());
        taskDescET.setText(task.getDesc());
        initActionBar("查看和编辑", null, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_task, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_image_share:
                ShareTaskActivity.openShareTaskActivity(mContext, task);
                return true;
            case R.id.menu_commit:
                String title = taskTitleET.getText().toString();
                if (!TextUtils.isEmpty(title)) {
                    task.setTitle(title);
                }
                task.setDesc(taskDescET.getText().toString());
                TaskManager.updateTask(task, title, task.getDesc());
                Intent intent = new Intent();
                intent.putExtra("task", task);
                setResult(RESULT_OK, intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
