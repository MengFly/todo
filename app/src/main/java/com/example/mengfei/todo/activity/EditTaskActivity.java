package com.example.mengfei.todo.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mengfei.todo.AppConfig;
import com.example.mengfei.todo.R;
import com.example.mengfei.todo.activity.inter.UiShower;
import com.example.mengfei.todo.adapter.TalkAdapter;
import com.example.mengfei.todo.entity.Talk;
import com.example.mengfei.todo.entity.Task;
import com.example.mengfei.todo.entity.TaskManager;
import com.example.mengfei.todo.utils.dialog.AddChatDialog;
import com.example.mengfei.todo.utils.dialog.CheckBoxDialog;
import com.example.mengfei.todo.utils.dialog.DateTimeDialog;
import com.example.mengfei.todo.utils.dialog.EditTagDialog;
import com.example.todolib.utils.ClipboardUtils;
import com.example.todolib.utils.ShareTools;
import com.example.todolib.utils.date.DateTools;
import com.example.todolib.view.widget.CustomDialogCreater;

import org.litepal.crud.DataSupport;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by mengfei on 2017/3/14.
 * 添加任务的Activity
 */
public class EditTaskActivity extends BaseActivity {

    private static final String INTENT_KEY_TASK = "task";
    private View addDateTimeView;//设置时间的View
    private TextView showDateTimeTV;//显示设置时间的view

    private EditText taskTitleET, taskDescET;
    private Button okEditBtn;
    private ListView talkListView;
    private CoordinatorLayout coordinatorLayout;

    private ImageButton addChatBtn;
    private TalkAdapter adapter;

    private Task task;

    /**
     * 打开编辑Task的界面
     *
     * @param sendTask 要显示的Task
     */
    public static void openEditTaskActivity(Activity context, Task sendTask, boolean isFinish) {
        Intent intent = new Intent(context, EditTaskActivity.class);
        intent.putExtra(INTENT_KEY_TASK, sendTask);
        context.startActivity(intent);
        if (isFinish) {
            context.finish();
        }
    }

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
        setContentView(R.layout.layout_activity_edit_task);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        okEditBtn = (Button) findViewById(R.id.btn_ok_edit);
        initActionBar("Edit Task", null, true);
        Intent intent = getIntent();
        if (intent != null) {
            task = (Task) intent.getSerializableExtra(INTENT_KEY_TASK);
        }
        if (task == null) {
            showSnackbar(coordinatorLayout, "task layout_title_bar 不合法, 3 秒后退出此界面");
            handler.sendEmptyMessageDelayed(0x0001, 3000);
        }
        initEditHeader();
        initAddDateTimeView();
        initAddChatTitle();
        initUI();
        initListener();
    }

    //初始化编辑任务的头部
    private void initEditHeader() {
        View headerView = getLayoutInflater().inflate(R.layout.layout_include_add_task_header, null);
        headerView.findViewById(R.id.appbar_layout).setVisibility(View.GONE);
        taskTitleET = ((EditText) headerView.findViewById(R.id.et_task_title));
        taskDescET = ((EditText) headerView.findViewById(R.id.et_task_desc));
        talkListView = (ListView) findViewById(R.id.lv_talk_items);
        talkListView.addHeaderView(headerView);
    }


    //初始化评论区title
    private void initAddChatTitle() {
        View talkAreaTitle = getLayoutInflater().inflate(R.layout.layout_title_bar, null);
        addChatBtn = (ImageButton) talkAreaTitle.findViewById(R.id.ibtn_add_chat);
        talkListView.addHeaderView(talkAreaTitle);
    }

    //初始化添加时间的View
    private void initAddDateTimeView() {
        addDateTimeView = getLayoutInflater().inflate(R.layout.layout_include_add_date_time, null);
        showDateTimeTV = (TextView) addDateTimeView.findViewById(R.id.tv_show_date_time);
        if (task.getWantDoneDate() != null) {
            showDateTimeTV.setText(DateTools.formatDate(task.getWantDoneDate()));
        }
        talkListView.addHeaderView(addDateTimeView);
    }

    private void initListener() {
        addChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTalkDialog();
            }
        });
        okEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskTitle = taskTitleET.getText().toString();
                String taskDesc = taskDescET.getText().toString();
                showSnackbar(coordinatorLayout, taskTitle + taskDesc + task.getTaskId());
                if (TaskManager.checkTitleAndDesc(taskTitle, taskDesc)) {
                    TaskManager.updateTask(task, taskTitle, taskDesc);
                    MainActivity.startMainWithMsg(mContext, "修改成功");
                    finish();
                } else {
                    showSnackbar(coordinatorLayout, "task title and desc can't empty!!");
                }
            }
        });
        talkListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int itemIndex = position - 3;
                if (itemIndex < 0) return true;
                final Talk talk = adapter.getItem(itemIndex);
                CustomDialogCreater.getItemsDialog(mContext, "选择操作", new String[]{"复制到剪切板", "删除评论"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                ClipboardUtils.setTextClip(mContext, talk.getTalkContent());
                                showSnackbar(coordinatorLayout, "您的评论已经复制到了剪贴板了");
                                break;
                            case 1:
                                DataSupport.deleteAll(Talk.class, "talkId=?", talk.getTalkId());
                                adapter.removeItem(talk);
                                showSnackbar(coordinatorLayout, "评论删除成功");
                                break;
                        }
                    }
                }).show();
                return false;
            }
        });
        addDateTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DateTimeDialog(mContext, new Date(), new UiShower<Date>() {
                    @Override
                    public void show(Date date) {
                        showDateTimeTV.setText(DateTools.formatDate(date));
                        task.setWantDoneDate(date);
                    }
                }).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_task, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_done_task:
                TaskManager.completeTask(task);
                MainActivity.startMainWithMsg(mContext, "恭喜完成了一个任务，完成的任务可以在已经完成的任务列表查看");
                finish();
                return true;
            case R.id.menu_text_share:
                String shareStr = TaskManager.getTaskShareStr(task);
                Intent intent = ShareTools.getShareTextIntent(shareStr);
                if (isUsedIntentActivity(intent)) {
                    startActivity(Intent.createChooser(intent, "选择要分享的应用"));
                } else {
                    showSnackbar(toolbar, "您的设备没有对应的应用可供分享");
                }
                return true;
            case R.id.menu_delete_task:
                if (AppConfig.getInstance(mContext).isDeleteTipShow()) {
                    CheckBoxDialog.getDialog(mContext, null, "是否删除这个任务？", "不再提示", new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            AppConfig.getInstance(mContext).setDeleteTipShow(!isChecked);
                        }
                    }, "确定删除", "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            TaskManager.deleteTask(task);
                            MainActivity.startMainWithMsg(mContext, "删除成功");
                            finish();
                        }
                    }, null).show();
                } else {
                    TaskManager.deleteTask(task);
                    MainActivity.startMainWithMsg(mContext, "删除成功");
                    finish();
                }
                return true;
            case R.id.menu_image_share:
                ShareTaskActivity.openShareTaskActivity(mContext, task);
                return true;
            case R.id.menu_edit_tag :
                new EditTagDialog(mContext, task).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //显示一个添加聊天信息的Dialog
    private void showAddTalkDialog() {
        AddChatDialog.newInstance(mContext, new UiShower<Talk>() {
            @Override
            public void show(Talk talk) {
                if (talk == null) {
                    showSnackbar(coordinatorLayout, "没有输入");
                } else {
                    adapter.setItem(talk, 0);
                    talk.save();
                }
            }
        }, task).show();
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
