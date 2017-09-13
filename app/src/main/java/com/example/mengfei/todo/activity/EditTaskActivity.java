package com.example.mengfei.todo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.activity.inter.UiShower;
import com.example.mengfei.todo.entity.Task;
import com.example.mengfei.todo.entity.TaskManager;
import com.example.mengfei.todo.utils.dialog.DateTimeDialog;
import com.example.mengfei.todo.utils.dialog.GetAppDialog;
import com.example.mengfei.todo.utils.dialog.GetContactsDialog;
import com.example.todolib.view.widget.DateTextView;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Handler;

/**
 * Created by mengfei on 2017/3/14.
 * 添加任务的Activity
 */
public class EditTaskActivity extends BaseActivity {

    private static final String INTENT_KEY_TASK = "task";
    public static final int REQUEST_CODE = 0x123;

    private EditText taskTitleET, taskDescET;
    private Task task;
    private DateTextView createDateDTV;
    private DateTextView wantDoneDateDTV;
    private ProgressBar nowPB;
    private EditText inputEt;

    ImageButton showMenuBtn;

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

    android.os.Handler handler = new android.os.Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            updateTime();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        task = (Task) getIntent().getSerializableExtra(INTENT_KEY_TASK);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_add_task);
        taskTitleET = (EditText) findViewById(R.id.et_task_title);
        taskDescET = (EditText) findViewById(R.id.et_task_desc);
        createDateDTV = (DateTextView) findViewById(R.id.dtv_create_date);
        wantDoneDateDTV = (DateTextView) findViewById(R.id.dtv_want_done_date);
        nowPB = (ProgressBar) findViewById(R.id.pb_);
        inputEt = (EditText) findViewById(R.id.et_input);
        showMenuBtn = (ImageButton) findViewById(R.id.ib_show_menu);

        taskTitleET.setText(task.getTitle());
        taskDescET.setText(task.getDesc());
        createDateDTV.setDate(task.getCreateDate());
        createDateDTV.setShowMode(DateTextView.SHOW_MODE_ALL);
        wantDoneDateDTV.setShowMode(DateTextView.SHOW_MODE_ONLY_DATE);

        if (task.getWantDoneDate() != null) {
            wantDoneDateDTV.setDate(task.getWantDoneDate());
            handler.sendEmptyMessageDelayed(0x234, 2000);
        } else {
            nowPB.setFocusable(false);
        }
        initActionBar("查看和编辑", null, true);
        initListener();
    }

    private void initListener() {
        showMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu menu = new PopupMenu(mContext, showMenuBtn);
                menu.inflate(R.menu.menu_task_select);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_select_phone:
                                new GetContactsDialog(mContext, new UiShower<GetContactsDialog.Contact>() {
                                    @Override
                                    public void show(GetContactsDialog.Contact contact) {
                                        inputEt.setText("tel: " + contact.name + " <" + contact.number + ">");
                                    }
                                }).show();
                                return true;
                            case R.id.menu_select_app:
                                new GetAppDialog(mContext, new UiShower<GetAppDialog.AppBean>() {
                                    @Override
                                    public void show(GetAppDialog.AppBean appBean) {

                                    }
                                }).show();
                        }
                        return false;
                    }
                });
                if (menu.getMenu().hasVisibleItems()) {
                    menu.show();
                }
            }
        });
        wantDoneDateDTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimeDialog dialog = new DateTimeDialog(mContext, new Date(), new UiShower<Date>() {
                    @Override
                    public void show(Date date) {
                        task.setWantDoneDate(date);
                        updateTime();
                    }
                });
                dialog.show();
            }
        });
    }

    private void updateTime() {
        int tootal = (int) (task.getWantDoneDate().getTime() / 1000 - task.getCreateDate().getTime() / 1000);
        int now = (int) (Calendar.getInstance().getTime().getTime() / 1000 - task.getCreateDate().getTime() / 1000);
        nowPB.setMax(tootal);
        nowPB.setProgress(now);
        wantDoneDateDTV.setDate(task.getWantDoneDate());
        if (now >= tootal) {
            return;
        }
        handler.sendEmptyMessageDelayed(0x234, 2000);
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
                task.setWantDoneDate(wantDoneDateDTV.getDate());
                TaskManager.updateTask(task, title, task.getDesc());
                Intent intent = new Intent();
                intent.putExtra("task", task);
                setResult(RESULT_OK, intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPause() {
        handler.removeMessages(0x234);
        super.onPause();
    }
}
