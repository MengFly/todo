package com.example.mengfei.todo.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.activity.BaseActivity;
import com.example.mengfei.todo.activity.inter.UiShower;
import com.example.mengfei.todo.entity.ActionManager;
import com.example.mengfei.todo.entity.Task;
import com.example.mengfei.todo.entity.Action;

import java.util.Calendar;
import java.util.Date;

public class AddAndEditTaskDialog extends Dialog {

    private Context context;
    private UiShower<Task> showEditedTask;
    private Task editTask;
    private EditText taskTitleET, taskDescET;
    private TextView addActionTv, addTimeTv;
    private TextView cancelBtn;
    private TextView okBtn;
    private Date wantDoneDate;
    private Action action;

    private LinearLayout actionLL;
    private TextView actionTitleTV;
    private TextView actionDescTV;
    private ImageButton doActionIB;
    private ImageView actionIconIv;

    //titlebar
    private TextView titleBarTitle;
    private ImageButton titleBarMenu;
    private ImageButton titleBarBack;

    public AddAndEditTaskDialog(@NonNull Context context, Task editTask, UiShower<Task> showEditedTask) {
        this(context, R.style.ScreenDialog, editTask, showEditedTask);
    }

    private AddAndEditTaskDialog(@NonNull Context context, @StyleRes int themeResId, Task editTask, UiShower<Task> shower) {
        super(context, themeResId);
        this.editTask = editTask;
        this.showEditedTask = shower;
        this.context = context;
        setContentView(R.layout.layout_dialog_add_edit_task);
        initView();
        initDatas();
        initListener();
    }

    private void initView() {
        taskTitleET = (EditText) findViewById(R.id.et_task_title);
        taskDescET = (EditText) findViewById(R.id.et_task_desc);
        okBtn = (TextView) findViewById(R.id.tv_dialog_ok);
        cancelBtn = (TextView) findViewById(R.id.tv_dialog_cancel);

        addActionTv = ((TextView) findViewById(R.id.tv_add_action));
        addTimeTv = ((TextView) findViewById(R.id.tv_add_time));

        titleBarMenu = (ImageButton) findViewById(R.id.ib_menu);
        titleBarTitle = (TextView) findViewById(R.id.tv_title);
        titleBarBack = (ImageButton) findViewById(R.id.ib_back);

        actionLL = (LinearLayout) findViewById(R.id.ll_action);
        actionIconIv = (ImageView) findViewById(R.id.iv_action_icon);
        actionTitleTV = (TextView) findViewById(R.id.tv_action_title);
        actionDescTV = (TextView) findViewById(R.id.tv_action_desc);
        doActionIB = (ImageButton) findViewById(R.id.ib_do_action);
    }

    private void initDatas() {
        if (editTask != null) {
            titleBarTitle.setText(context.getResources().getText(R.string.edit_task));
        } else {
            editTask = new Task("", "");
            titleBarTitle.setText(context.getResources().getText(R.string.add_task));
        }
        taskTitleET.setText(editTask.getTitle());
        taskDescET.setText(editTask.getDesc());
        updateActionView(editTask.getAction());
        updateWantDoneDateView(editTask.getWantDoneDate());

    }

    private void updateWantDoneDateView(Date wantDoneDate) {
        this.wantDoneDate = wantDoneDate;
        if (wantDoneDate == null) {
            addTimeTv.setText(context.getResources().getText(R.string.click_add_time));
        } else {
            addTimeTv.setText(DateUtils.formatDateTime(context, wantDoneDate.getTime(), DateUtils.FORMAT_ABBREV_ALL));
        }
    }

    private void updateActionView(final Action action) {
        this.action = action;
        if (action == null) {
            actionLL.setVisibility(View.GONE);
            addActionTv.setVisibility(View.VISIBLE);
        } else {
            addActionTv.setVisibility(View.GONE);
            actionLL.setVisibility(View.VISIBLE);
            actionTitleTV.setText(action.title);
            actionDescTV.setText(action.desc);
            actionIconIv.setImageBitmap(action.getShowIcon());
            doActionIB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    action.doAction(context);
                }
            });
        }
    }

    private void initListener() {
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = taskTitleET.getText().toString();
                String desc = taskDescET.getText().toString();
                if (checkText(title, desc)) {
                    editTask.setTitle(title);
                    editTask.setDesc(desc);
                    editTask.setWantDoneDate(wantDoneDate);
                    ActionManager.saveAction(action, editTask.getTaskId());
                    showEditedTask.show(editTask);
                    dismiss();
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        titleBarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        titleBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        addTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DateTimeDialog(context, Calendar.getInstance().getTime(), new UiShower<Date>() {
                    @Override
                    public void show(Date date) {
                        updateWantDoneDateView(date);
                    }
                }).show();
            }
        });
        addActionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("选择要添加的动作类型").setItems(R.array.action_type, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onActionTypeClick(which);
                    }
                }).create().show();
            }
        });


    }

    /**
     * item>打开一个App</item>
     * <item>打电话</item>
     * <item>打开一个网页</item>
     * <item>发送一个Email</item>
     *
     * @param which
     */
    private void onActionTypeClick(int which) {
        switch (which) {
            case 0:
                selectApp();
                break;
            case 1:
                selectPhone();
                break;
            case 2:
//                selectURL();
                break;
            case 3:
//                selectEmail();
                break;
        }
    }

    private void selectPhone() {
        new GetContactsDialog(context, new UiShower<GetContactsDialog.Contact>() {
            @Override
            public void show(GetContactsDialog.Contact contact) {
                Action action = new Action(contact.name, contact.number, contact.icon, Action.TYPE_Phone, editTask.getTaskId());
                updateActionView(action);
            }
        }).show();
    }

    private void selectApp() {
        new GetAppDialog(context, new UiShower<GetAppDialog.AppBean>() {
            @Override
            public void show(GetAppDialog.AppBean appBean) {
                Action action = new Action(appBean.name, appBean.packageName, appBean.icon, Action.TYPE_App, editTask.getTaskId());
                updateActionView(action);
            }
        }).show();
    }

    private boolean checkText(String title, String desc) {
        if (TextUtils.isEmpty(title)) {
            ((BaseActivity) context).showToast("标题不能为空");
            return false;
        }
        return true;
    }

}
