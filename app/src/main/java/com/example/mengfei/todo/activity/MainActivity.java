package com.example.mengfei.todo.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.icu.text.LocaleDisplayNames;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.activity.inter.UiShower;
import com.example.mengfei.todo.adapter.TaskAdapter;
import com.example.mengfei.todo.entity.OneWords;
import com.example.mengfei.todo.entity.OneWordsManager;
import com.example.mengfei.todo.entity.Task;
import com.example.mengfei.todo.entity.TaskManager;
import com.example.mengfei.todo.service.TaskTimeCheckService;
import com.example.mengfei.todo.utils.DateUtils;

import com.example.mengfei.todo.utils.image.ImageLoader;
import com.example.todolib.utils.DisplayUtils;
import com.example.todolib.view.widget.CustomDialogCreater;
import com.example.todolib.view.widget.VisibleImageView;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity {

    private static String showMsg = null;

    private TextView oneWordsOfDayTV;
    private ListView taskLV;
    private CoordinatorLayout coordinatorLayout;
    private FloatingActionButton addTaskBtn;
    private Toolbar toolbar;
    private ImageView headerBackIV;

    private TaskAdapter adapter;
    private NavigationView menuNav;
    private DrawerLayout drawerLayout;

    private int headerHeight = 300;
    private int statBarHeight = 25;

    private int toolbarNowAlpha = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_main);
        Intent intent = new Intent(mContext, TaskTimeCheckService.class);
        startService(intent);
        initView();

        initStatBarSize();
        initListener();
        initUI();

    }

    private void initStatBarSize() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statBarHeight = DisplayUtils.px2dip(mContext, getResources().getDimensionPixelSize(resourceId));
        } else {
            statBarHeight = 25;
        }
    }

    //检查时间是否太晚
    private void checkTime() {
        if (DateUtils.isTooLate()) {
            CustomDialogCreater.getSimpleDialog(mContext, "重要提示！！！", "时间已经太晚了哦，小Do提醒您充足的睡眠有助于提高工作效率哦。", null, null).show();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initDatas();
    }

    private void closeNavMenu() {
        if (drawerLayout.isDrawerOpen(menuNav)) {
            drawerLayout.closeDrawer(menuNav);
        }
    }


    private void initUI() {
        checkTime();
        initOneWords();
    }

    //初始化每日一句
    private void initOneWords() {
        OneWordsManager.getOneWords(new UiShower<OneWords>() {
            @Override
            public void show(OneWords oneWords) {
                oneWordsOfDayTV.setText(oneWords.getShowSpannableString());
                Log.d("URL", oneWords.getPicture2());
                ImageLoader.loadImage(mContext, oneWords.getPicture2(), headerBackIV, new BlurTransformation(mContext, 15));
            }
        });
    }

    private void initListener() {
        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeNavMenu();
                openOtherActivity(AddTaskActivity.class, false);
            }
        });
        taskLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position - 1 < 0) return;
                closeNavMenu();
                EditTaskActivity.openEditTaskActivity(mContext, adapter.getItem(position - 1), false);
            }
        });
        taskLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position - 1 < 0) return false;
                taskOnLongClick(adapter.getItem(position - 1));
                return true;
            }
        });
        menuNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return navItemSelected(item);
            }
        });
        taskLV.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Rect headerRect = new Rect();
                headerBackIV.getGlobalVisibleRect(headerRect);
                int top = DisplayUtils.px2dip(mContext, headerRect.top);
                if (top <= 0 || firstVisibleItem >= 1) {
                    toolbar.setBackgroundColor(getResources().getColor(R.color.app_main_color));
                    toolbar.setTitle("未完成的任务");
                } else {
                    int now = DisplayUtils.px2dip(mContext, headerRect.bottom) - statBarHeight;
                    toolbarNowAlpha = (int) (255 * ((headerHeight - now) / (headerHeight + 0.5f)));
                    if (toolbarNowAlpha <= 3) {
                        toolbar.setBackground(getResources().getDrawable(R.drawable.back_toolbar));
                    } else {
                        toolbar.setBackgroundColor(Color.argb(toolbarNowAlpha, 189, 17, 17));
                    }
                    toolbar.setTitle("Todo");
                }
            }
        });
    }

    //侧滑菜单的点击响应事件
    private boolean navItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_done_task:
                closeNavMenu();
                openOtherActivity(TotalDoneTaskActivity.class, false);
                return true;
            case R.id.menu_history_words:
                closeNavMenu();
                openOtherActivity(HistoryOneWordsActivity.class, false);
                return true;
            case R.id.menu_about_app:
                closeNavMenu();
                WebActivity.StartWebActivityWithURL(mContext, "http://mengfly.github.io/app/todo/aboutapp.html");
                return true;
            case R.id.menu_back:
                closeNavMenu();
                openOtherActivity(BackActivity.class, false);
                return true;
            case R.id.menu_setting:
                closeNavMenu();
                openOtherActivity(SettingActivity.class, false);
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

    private void taskOnLongClick(final Task task) {
        SpannableString title = new SpannableString("选择操作任务 —— " + task.getTitle());
        title.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.app_main_color)), "选择操作任务 —— ".length(), title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        title.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.app_btn_color)), 0, "选择操作任务".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        CustomDialogCreater.getItemsDialog(mContext, title, new String[]{"完成目标", "删除任务", "查看任务"}, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    //完成目标
                    case 0:
                        completeTask(task);
                        break;
                    //删除任务
                    case 1:
                        deleteTask(task);
                        break;
                    case 2:
                        EditTaskActivity.openEditTaskActivity(mContext, task, false);
                        break;
                }
            }
        }).show();
    }

    //完成Task
    private void completeTask(Task task) {
        if (TaskManager.completeTask(task)) {
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

    private void initView() {
        toolbar = ((Toolbar) findViewById(R.id.toolbar));
        setSupportActionBar(toolbar);
        View headerLayout = getLayoutInflater().inflate(R.layout.layout_main_header, null);
        oneWordsOfDayTV = ((TextView) headerLayout.findViewById(R.id.tv_word_of_day));
        headerBackIV = (ImageView) headerLayout.findViewById(R.id.iv_back);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cl_btn);
        menuNav = (NavigationView) findViewById(R.id.nv_menu);
        taskLV = ((ListView) findViewById(R.id.lv_task_items));
        taskLV.addHeaderView(headerLayout);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawrlayout);
        addTaskBtn = ((FloatingActionButton) findViewById(R.id.btn_add_task));
        initActionBar("TODO", null, false);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
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

    /**
     * 启动MainActivity
     *
     * @param msg 要让MainActivity显示的消息
     */
    public static void startMainWithMsg(Context context, String msg) {
        Intent intent = new Intent(context, MainActivity.class);
        showMsg = msg;
        context.startActivity(intent);
    }

}
