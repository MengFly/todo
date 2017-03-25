package com.example.mengfei.todo.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.text.util.Linkify;
import android.widget.TextView;

import com.example.mengfei.todo.TodoApplication;
import com.example.mengfei.todo.activity.AddTaskActivity;
import com.example.mengfei.todo.activity.WebActivity;
import com.example.mengfei.todo.entity.Task;
import com.example.todolib.utils.ClipboardUtils;
import com.example.todolib.view.widget.CustomDialogCreater;

import me.saket.bettermovementmethod.BetterLinkMovementMethod;

/**
 * Created by mengfei on 2017/3/19.
 */

public class LinkManager {

    public static void  linkClick(final Context context, TextView textView) {
        BetterLinkMovementMethod.linkify(Linkify.ALL, textView).setOnLinkClickListener(new BetterLinkMovementMethod.OnLinkClickListener() {
            @Override
            public boolean onClick(final TextView textView, final String url) {
                if (url.startsWith("tel:")) {
                    CustomDialogCreater.getItemsDialog(context, "选择操作", new String[]{"复制到剪贴板", "添加到任务"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    ClipboardUtils.setTextClip(TodoApplication.getContext(), url.substring("tel:".length(), url.length()));
                                    Snackbar.make(textView, "已经复制到剪贴板", Snackbar.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    AddTaskActivity.openAddTaskActivity(context, Task.TASK_TYPE_MOBILE, url.substring("tel:".length(), url.length()));
                                    break;
                            }
                        }
                    }).show();
                    return true;
                } else if (url.startsWith("mailto:")) {
                    CustomDialogCreater.getItemsDialog(context, "选择操作", new String[]{"复制到剪贴板", "添加到任务"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    ClipboardUtils.setTextClip(TodoApplication.getContext(), url.substring("mailto:".length(), url.length()));
                                    Snackbar.make(textView, "已经复制到剪贴板", Snackbar.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    AddTaskActivity.openAddTaskActivity(context, Task.TASK_TYPE_EMAIL, url.substring("mailto:".length(), url.length()));
                                    break;
                            }
                        }
                    }).show();
                    return true;
                } else {
                    CustomDialogCreater.getItemsDialog(context, "选择操作", new String[]{"复制到剪贴板", "添加到任务","直接打开"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    ClipboardUtils.setTextClip(TodoApplication.getContext(), url);
                                    Snackbar.make(textView, "已经复制到剪贴板", Snackbar.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    AddTaskActivity.openAddTaskActivity(context, Task.TASK_TYPE_NET, url);
                                    break;
                                case 2:
                                    WebActivity.StartWebActivityWithURL(context, url);
                                    break;
                            }
                        }
                    }).show();
                    return true;
                }
            }
        });
    }
}
