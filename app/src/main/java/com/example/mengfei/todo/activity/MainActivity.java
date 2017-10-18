package com.example.mengfei.todo.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mengfei.todo.AppConfig;
import com.example.mengfei.todo.AppConstant;
import com.example.mengfei.todo.R;
import com.example.mengfei.todo.activity.inter.UiShower;
import com.example.mengfei.todo.adapter.TaskAdapter;
import com.example.mengfei.todo.entity.DailySentence;
import com.example.mengfei.todo.entity.DailySentenceManager;
import com.example.mengfei.todo.entity.Task;
import com.example.mengfei.todo.entity.TaskManager;
import com.example.mengfei.todo.utils.DateUtils;
import com.example.mengfei.todo.dialog.AddAndEditTaskDialog;
import com.example.mengfei.todo.utils.image.ImageLoader;

import java.util.List;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity {

    private AppConfig config = null;
    private ImageView oneWordOfDayImg;
    private TextView oneWordOfDayTv;

    private ListView taskLV;
    private TaskAdapter adapter;

    private CoordinatorLayout coordinatorLayout;
    private FloatingActionButton addTaskBtn;

    private NavigationView menuNav;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        config = AppConfig.getInstance(mContext);
        checkTime();
        setContentView(R.layout.layout_activity_main);
        initView();
        initListener();
        initOneWord();
    }

    //检查时间是否太晚
    private void checkTime() {
        if (DateUtils.isTooLate() && config.isTimeTooLateTipShow()) {
            showMessageDialog(R.drawable.ic_good_night, "重要提示！！！",
                    "时间已经太晚了哦，小Do提醒您充足的睡眠有助于提高工作效率哦。(此消息可以在设置中关闭)", null, null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private void initListener() {
        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAndEditTaskDialog dialog = new AddAndEditTaskDialog(mContext, null, new UiShower<Task>() {
                    @Override
                    public void show(Task task) {
                        showAndSaveTask(task);
                    }
                });
                dialog.show();
            }
        });
        taskLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                Task editTask = adapter.getItem(position);
                AddAndEditTaskDialog editTaskDialog = new AddAndEditTaskDialog(mContext, editTask, new UiShower<Task>() {
                    @Override
                    public void show(Task task) {
                        showAndUpdateTask(task, view);
                    }
                });
                editTaskDialog.show();
            }
        });
        taskLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                taskOnLongClick(view, adapter.getItem(position));
                return true;
            }
        });
        menuNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return navItemSelected(item);
            }
        });
    }

    private void showAndUpdateTask(Task task, View view) {
        TaskManager.updateTask(task, task.getTitle(), task.getDesc());
        adapter.notifyDataSetChanged();
        showSnackbar(coordinatorLayout, "修改成功");
    }

    private void showAndSaveTask(Task task) {
        TaskManager.saveTask(task);
        adapter.setItem(task, 0);
    }

    //侧滑菜单的点击响应事件
    private boolean navItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_done_task:
                GetTaskActivity.start(mContext, GetTaskActivity.TYPE_DONE);
                return true;
            case R.id.menu_history_words:
                openOtherActivity(HistoryOneWordsActivity.class, false);
                return true;
            case R.id.menu_about_app:
                WebActivity.StartWebActivityWithURL(mContext, AppConstant.ABOUT_APP_URL);
                return true;
            case R.id.menu_back:
                openOtherActivity(FeedbackActivity.class, false);
                return true;
            case R.id.menu_setting:
                openOtherActivity(SettingActivity.class, false);
                return true;
            case R.id.menu_recycle:
                GetTaskActivity.start(mContext, GetTaskActivity.TYPE_RECYCLE);
                return true;
            default:
                return false;
        }
    }

    //主界面不允许滑动
    @Override
    public boolean supportSlideBack() {
        return false;
    }

    //主界面的后退事件
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(menuNav)) {
            drawerLayout.closeDrawer(menuNav);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (drawerLayout.isDrawerOpen(menuNav)) {
                drawerLayout.closeDrawer(menuNav);
            } else {
                drawerLayout.openDrawer(menuNav);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void taskOnLongClick(View view, final Task task) {
        PopupMenu menu = new PopupMenu(mContext, view, Gravity.CENTER);
        menu.getMenuInflater().inflate(R.menu.menu_task_item_long_press, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_complete_task:
                        completeTask(task);
                        return true;
                    case R.id.menu_delete_task:
                        deleteTask(task);
                        return true;
                }
                return false;
            }
        });
        menu.show();
    }

    void initOneWord() {
        DailySentenceManager.getOneWords(new UiShower<DailySentence>() {
            @Override
            public void show(DailySentence dailySentence) {
                ImageLoader.loadImage(mContext, dailySentence.getPicture2(), oneWordOfDayImg, null);
                oneWordOfDayTv.setText(dailySentence.getShowSpannableString());
            }
        });
    }

    //完成Task
    private void completeTask(Task task) {
        if (TaskManager.completeTask(task)) {
            showSnackbar(coordinatorLayout, getString(R.string.string_task_done_tip));
            adapter.removeItem(task);
        }
    }

    //删除Task
    private void deleteTask(final Task task) {
        showMessageDialog(R.drawable.ic_menu_delete, getString(R.string.string_delete),
                getString(R.string.string_is_delete_task) + task.getTitle(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TaskManager.deleteTask(task)) {
                            adapter.removeItem(task);
                            showSnackbar(coordinatorLayout, getString(R.string.string_delete_success));
                        }
                    }
                }, null);
    }

    private void initView() {
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cl_btn);
        menuNav = (NavigationView) findViewById(R.id.nv_menu);
        taskLV = ((ListView) findViewById(R.id.lv_task_items));
        drawerLayout = (DrawerLayout) findViewById(R.id.drawrlayout);
        addTaskBtn = ((FloatingActionButton) findViewById(R.id.btn_add_task));
        oneWordOfDayImg = (ImageView) findViewById(R.id.iv_back);
        oneWordOfDayTv = (TextView) findViewById(R.id.tv_word_of_day);

//        taskLV.setEmptyView(getEmptyView("点击下方的添加按钮添加任务"));
        initActionBar("任务备忘录", null, false);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }
        initDatas();
    }


    //当点击Menu按钮的时候
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initDatas() {
        List<Task> tasks = TaskManager.getNotCompletedTask();
        if (AppConfig.getInstance(mContext).isFirstInstall()) {
            tasks.add(TaskManager.getHelpTask());
        }
        adapter = new TaskAdapter(mContext, tasks, R.layout.layout_item_task);
        taskLV.setAdapter(adapter);
    }

}
