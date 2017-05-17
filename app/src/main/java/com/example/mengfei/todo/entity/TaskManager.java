package com.example.mengfei.todo.entity;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.TextUtils;

import com.example.mengfei.todo.AppConstant;
import com.example.mengfei.todo.activity.MainActivity;
import com.example.mengfei.todo.activity.inter.UiShower;
import com.example.mengfei.todo.reciver.TaskTimeCheckReceiver;
import com.example.todolib.utils.CheckUtils;
import com.example.todolib.utils.date.DateTools;

import org.litepal.crud.DataSupport;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Task的管理类
 * Created by mengfei on 2017/3/15.
 */
public class TaskManager {

    public static boolean checkTitleAndDesc(String title, String desc) {
        return !(TextUtils.isEmpty(title) && TextUtils.isEmpty(desc));
    }

    public static boolean saveTask(Task task) {
        return task.save();
    }

    //完成任务
    public static boolean completeTask(Task task) {
        task.setCompleted(true);
        task.setDoneDate(new Date());
        ContentValues values = new ContentValues();
        values.put("isCompleted", task.isCompleted());
        values.put("doneDate", task.getDoneDate().getTime());
        return DataSupport.updateAll(Task.class, values, "taskId=?", task.getTaskId()) > 0;
    }

    //更新任务
    public static boolean updateTask(Task task, String title, String desc) {
        task.setTitle(title);
        task.setDesc(desc);
        ContentValues values = new ContentValues();
        if (title != null) {
            values.put("title", title);
        }
        if (desc != null) {
            values.put("desc", desc);
        }
        if (task.getWantDoneDate() != null) {
            values.put("wantDoneDate", task.getWantDoneDate().getTime());
        }
        return DataSupport.updateAll(Task.class, values, "taskId=?", task.getTaskId()) > 0;
    }

    public static List<Task> getCompletedTask() {
        return DataSupport.where("isCompleted=?", String.valueOf(1)).order("createDate desc").find(Task.class);
    }

    public static List<Task> getNotCompletedTask() {
        return DataSupport.where("isCompleted=?", String.valueOf(0)).order("createDate desc").find(Task.class);
    }

    public static boolean deleteTask(Task task) {
        if (DataSupport.deleteAll(Task.class, "taskId=?", task.getTaskId()) > 0) {
            DataSupport.deleteAll(Talk.class, "taskId=?", task.getTaskId());
            return true;
        } else {
            return false;
        }
    }

    public static PendingIntent getAlarmPendingIntent(Context context, Task task) {
        Intent intent = new Intent(context.getApplicationContext(), TaskTimeCheckReceiver.class);
        intent.putExtra("task", task);
        return PendingIntent.getBroadcast(context, getTaskID(task), intent, PendingIntent.FLAG_ONE_SHOT);
    }

    //根据task获取到通知的id
    public static int getTaskID(Task task) {
        return Integer.parseInt(task.getTaskId().substring(task.getTaskId().length() - 7, task.getTaskId().length()));
    }

    /**
     * 检测Task是否符合标准
     * @param taskType
     * @param taskDesc
     * @param shower 用于显示的shower
     * @return
     */
    public static boolean checkTask(String taskType, String taskDesc, UiShower<String> shower) {
        if (Task.TASK_TYPE_EMAIL.equals(taskType)) {
            if (CheckUtils.isEmail(taskDesc)) {
                return true;
            } else {
                //设置提示信息
                if (shower!= null) {
                    shower.show("您输入的Email地址不合法" +
                            AppConstant.APPSPLITE +
                            "Email填写规范:\n <****.****@***.com/cn> \n示例:\n (shili@gmail.com)");
                }
                return false;
            }
        } else if (Task.TASK_TYPE_MOBILE.equals(taskType)) {
            if (CheckUtils.isMobileExact(taskDesc) || CheckUtils.isTel(taskDesc)) {
                return true;
            } else {
                if (shower != null) {
                    shower.show("您输入的电话或手机号码不合法" +
                            AppConstant.APPSPLITE +
                            "手机号或电话号码填写规范:\n<***-*****或******> \n示例:\n(0000-1111111或10000000000)");
                }
                return false;
            }
        } else if (Task.TASK_TYPE_NET.equals(taskType)) {
            if (CheckUtils.isURL(taskDesc)) {
                return true;
            } else {
                if (shower != null) {
                    shower.show("您输入的网络地址不合法" +
                            AppConstant.APPSPLITE +
                            "网络地址填写规范:\n<http://****.***或https://****.****> \n示例:\n(http://github.com)");
                }
                return false;
            }
        } else {
            return true;
        }
    }

    public static String getTaskShareStr(Task task) {
        StringBuilder sb = new StringBuilder("*****My Todo Task******\n");
        sb.append("TaskName:").append(task.getTitle()).append("\n");
        sb.append("TaskDesc:").append(task.getDesc()).append("\n");
        sb.append("CreateTime:").append(DateTools.formatDate(task.getCreateDate())).append("\n");
        if (task.getWantDoneDate() != null) {
            sb.append("TaskWantDoneTime:").append(DateTools.formatDate(task.getWantDoneDate())).append("\n");
        }
        if (task.getDoneDate() != null) {
            sb.append("TaskDoneTime:").append(DateTools.formatDate(task.getDoneDate())).append("\n");
        }
        sb.append("*******My Task Chats*******\n");
        for (Talk talk : task.getTalks()) {
            sb.append(DateTools.formatDate(talk.getTalkDate())).append(" : ").append(talk.getTalkContent()).append("\n");
        }
        sb.append("             ~ From Todo(任务备忘录)");
        return sb.toString();

    }

    public static Task getHelpTask() {
        Task task = new Task("欢迎使用任务备忘录，点击查看应用使用帮助哦", AppConstant.APP_HELP_URL);
        task.setTaskType(Task.TASK_TYPE_NET);
        task.save();
        return task;
    }

}
