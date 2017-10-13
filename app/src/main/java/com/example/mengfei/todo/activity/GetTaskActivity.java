package com.example.mengfei.todo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.adapter.TaskAdapter;
import com.example.mengfei.todo.entity.Task;
import com.example.mengfei.todo.entity.TaskManager;

import java.util.List;


/**
 * 查看应用完成的任务的界面
 * Created by mengfei on 2017/3/17.
 */
public class GetTaskActivity extends BaseActivity {

    private static final String KEY = "type";
    public static final int REQUEST_CODE = 0x89;

    public static final String TYPE_DONE = "done";
    public static final String TYPE_RECYCLE = "recycle";

    private ListView doneTaskListView;
    private TaskAdapter doneTaskAdapter;
    private String type;

    //判断数据是否有变化
    private boolean dataIsChange = false;

    public static void start(Activity context, String type) {
        Intent starter = new Intent(context, GetTaskActivity.class);
        starter.putExtra(KEY, type);
        context.startActivityForResult(starter, REQUEST_CODE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        type = intent.getStringExtra(KEY);
        setContentView(R.layout.layout_activity_get_task);
        initView();
    }

    private void initView() {
        doneTaskListView = (ListView) findViewById(R.id.lv_done_task);
        List<Task> tasks;
        if (type.equals(TYPE_RECYCLE)) {
            tasks = TaskManager.getDeleteTask();
            initActionBar("回收站", null, true);
        } else {
            initActionBar(getString(R.string.string_title_done_task), null, true);
            tasks = TaskManager.getCompletedTask();
        }
        doneTaskAdapter = new TaskAdapter(mContext, tasks, R.layout.layout_item_task);
        doneTaskListView.setAdapter(doneTaskAdapter);
        doneTaskListView.setEmptyView(getEmptyView(null));
        initListener();
    }

    private void initListener() {
        doneTaskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Task task = doneTaskAdapter.getItem(position);
                PopupMenu popmenu = new PopupMenu(mContext, view);
                popmenu.setGravity(Gravity.CENTER);
                if (type.equals(TYPE_RECYCLE)) {
                    popmenu.inflate(R.menu.menu_recycle);
                } else {
                    popmenu.inflate(R.menu.menu_done_task);
                }
                popmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_recovery:
                                TaskManager.recoveryTask(task);
                                doneTaskAdapter.removeItem(task);
                                showSnackbar(toolbar, "已经恢复至" + (task.isCompleted() ? "已完成的任务" : "未完成的任务"));
                                if (!task.isCompleted()) {
                                    dataIsChange = true;
                                }
                                return true;
                            case R.id.menu_always_delete:
                                TaskManager.alwaysDelete(task);
                                doneTaskAdapter.removeItem(task);
                                showSnackbar(getCurrentFocus(), "已经完全删除");
                                return true;
                            case R.id.menu_delete_task:
                                TaskManager.deleteTask(task);
                                doneTaskAdapter.removeItem(task);
                                showSnackbar(getCurrentFocus(), "已删除");
                                return true;
                            case R.id.menu_see_task:
                                ShareTaskActivity.start(mContext, task);
                                return true;

                        }
                        return false;
                    }
                });
                popmenu.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("dataIsChange", dataIsChange);
        setResult(RESULT_OK, intent);
        finish();
    }

}
