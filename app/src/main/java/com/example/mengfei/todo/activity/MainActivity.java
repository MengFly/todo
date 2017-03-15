package com.example.mengfei.todo.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.adapter.TaskAdapter;
import com.example.mengfei.todo.entity.OneWords;
import com.example.mengfei.todo.entity.Talk;
import com.example.mengfei.todo.entity.Task;
import com.example.mengfei.todo.entity.TaskManager;
import com.example.todolib.net.CommonOkHttpClient;
import com.example.todolib.net.listener.DisposeDataHandle;
import com.example.todolib.net.listener.DisposeDataListener;
import com.example.todolib.net.request.CommonRequest;
import com.example.todolib.view.widget.CustomDialogCreater;
import com.google.gson.Gson;


import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity {

    private static String showMsg = null;

    private TextView taskDetailsTV,oneWordsOfDayTV;
    private ListView taskLV;
    private CoordinatorLayout coordinatorLayout;
    private FloatingActionButton addTaskBtn;
    private Toolbar toolbar;

    private TaskAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_main);
        initView(savedInstanceState);
        initListener();
        initUI();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initDatas();
    }


    private void initUI() {
        initOneWords();
    }

    private void initOneWords() {
        OneWords oneWords = OneWords.findFirst(OneWords.class);
        if (oneWords != null && SimpleDateFormat.getDateInstance().format(new Date()).equals(SimpleDateFormat.getDateInstance().format(oneWords.getGetDate()))) {
            oneWordsOfDayTV.setText(oneWords.getShowSpannableString());
        } else {
            CommonOkHttpClient.get(CommonRequest.createGetRequest("http://open.iciba.com/dsapi/", null), new DisposeDataHandle(new DisposeDataListener() {
                @Override
                public void onSuccess(Object responseObj) {
                    try {
                        OneWords oneWords = new Gson().fromJson(responseObj.toString(), OneWords.class);
                        oneWords.setGetDate(new Date());
                        oneWordsOfDayTV.setText(oneWords.getShowSpannableString());
                        oneWords.saveIfNotExist("sid=?", oneWords.getSid());
                    } catch (Exception e) {
                        e.printStackTrace();
                        OneWords oneWords = OneWords.findFirst(OneWords.class);
                        oneWordsOfDayTV.setText(oneWords == null ? "坚持下去，看小Do能帮你完成多少事情" : oneWords.getShowSpannableString());
                    }
                }

                @Override
                public void onFailure(Object reasonObj) {
                    OneWords oneWords = OneWords.findFirst(OneWords.class);
                    oneWordsOfDayTV.setText(oneWords == null ? "坚持下去，看小Do能帮你完成多少事情" : oneWords.getShowSpannableString());
                }
            }));
        }
    }

    private void initListener() {
        taskDetailsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOtherActivity(TotalTaskDetailsActivity.class, false);
            }
        });
        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOtherActivity(AddTaskActivity.class, false);
            }
        });
        taskLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position - 1 < 0) return;
                showTask(adapter.getItem(position-1));
            }
        });
        taskLV.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem >= 1) {
                    toolbar.setTitle("当前未完成的任务");
                } else {
                    toolbar.setTitle("Todo");
                }
            }
        });
        taskLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position - 1 < 0) return false;
                taskOnLongClick(adapter.getItem(position-1));
                return true;
            }
        });
    }

    private void taskOnLongClick(final Task task) {
        SpannableString title = new SpannableString("选择操作任务 —— " + task.getTitle());
        title.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.app_main_color)), "选择操作任务 —— ".length(), title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        title.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.app_btn_color)), 0, "选择操作任务".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        CustomDialogCreater.getItemsDialog(mContext, title , new String[]{"完成目标", "删除任务", "查看任务"}, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    //完成目标
                    case 0:
                        completeTask(task);
                        break;
                    //删除任务
                    case  1:
                        deleteTask(task);
                        break;
                    case 2:
                        showTask(task);
                        break;
                }
            }
        }).show();
    }

    //完成Task
    private void completeTask(Task task) {
        if(TaskManager.completeTask(task)) {
            showSnackbar(coordinatorLayout, "任务已经完成，可以在已完成任务界面里面查看呦");
            adapter.removeItem(task);
        }
    }

    //删除Task
    private void deleteTask(Task task) {
        if (TaskManager.deleteTask(task)) {
            adapter.removeItem(task);
            showSnackbar(coordinatorLayout, "删除成功");
        }
    }



    //展示一个Task
    private void showTask(Task task) {
        Intent intent = new Intent(mContext, EditTaskActivity.class);
        intent.putExtra("task", task);
        startActivity(intent);
    }

    private void initView(Bundle savedInstanceState) {
        toolbar = ((Toolbar) findViewById(R.id.toolbar));
        setSupportActionBar(toolbar);
        View headerLayout = getLayoutInflater().inflate(R.layout.layout_main_header, null);
        taskDetailsTV = ((TextView) headerLayout.findViewById(R.id.tv_task_details));
        oneWordsOfDayTV = ((TextView) headerLayout.findViewById(R.id.tv_word_of_day));
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cl_btn);
        taskLV = ((ListView) findViewById(R.id.lv_task_items));
        taskLV.addHeaderView(headerLayout);
        addTaskBtn = ((FloatingActionButton) findViewById(R.id.btn_add_task));
        initActionBar("TODO", null, false);
        initDatas();
    }

    private void initDatas() {
        List<Task> tasks = TaskManager.getNotCompletedTask();
        adapter = new TaskAdapter(mContext, tasks, R.layout.layout_item_task);
        taskLV.setAdapter(adapter);
        if (showMsg != null) {
            showSnackbar(coordinatorLayout, showMsg);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showMsg = null;
    }

    public static void startMainWithMsg(Context context, String msg) {
        Intent intent = new Intent(context, MainActivity.class);
        showMsg = msg;
        context.startActivity(intent);
    }

}
