package com.example.mengfei.todo.entity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.mengfei.todo.AppConstant;
import com.example.mengfei.todo.R;
import com.example.mengfei.todo.TodoApplication;
import com.example.mengfei.todo.reciver.TaskTimeCheckReceiver;

import org.litepal.crud.DataSupport;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Task的管理类
 * Created by mengfei on 2017/3/15.
 */
public class TaskManager {

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
        ContentValues values = new ContentValues();
        if (title != null) {
            values.put("title", title);
        }
        if (desc != null) {
            values.put("desc", desc);
        }
        if (task.getWantDoneDate() != null) {
            values.put("wantDoneDate", task.getWantDoneDate().getTime());
            addTaskAlarm(task);
        }
        return DataSupport.updateAll(Task.class, values, "taskId=?", task.getTaskId()) > 0;
    }

    //所有能查到的任务应该是没有在回收站里面的
    public static List<Task> getCompletedTask() {
        return DataSupport.where("isCompleted=? and isDelete=?", String.valueOf(1), String.valueOf(0)).order("createDate desc").find(Task.class);
    }

    public static List<Task> getNotCompletedTask() {
        return DataSupport.where("isCompleted=? and isDelete=?", String.valueOf(0), String.valueOf(0)).order("createDate desc").find(Task.class);
    }

    public static List<Task> getDeleteTask() {
        return DataSupport.where("isDelete=?", String.valueOf(1)).order("createDate desc").find(Task.class);
    }

    public static boolean saveTask(Task task) {
        boolean isSave = task.save();
        if (isSave) {
            addTaskAlarm(task);
        }
        return isSave;
    }

    // 放进回收站
    public static boolean deleteTask(Task task) {
        //在删除Task之前要删除这个Task的闹钟
        TaskManager.cancelTaskAlarm(task);
        ContentValues values = new ContentValues();
        values.put("isDelete", true);
        return DataSupport.updateAll(Task.class, values, "taskId=?", task.getTaskId()) > 0;
    }

    private static void cancelTaskAlarm(Task task) {
        AlarmManager alarmManager = TodoApplication.getAlarmManager();
        alarmManager.cancel(getAlarmPendingIntent(task));
    }

    public static void addTaskAlarm(Task task) {
        AlarmManager alarmManager = TodoApplication.getAlarmManager();
        //在添加闹钟的时候要清除之前的闹钟信息，因为不能让之前的闹钟影响现在的闹钟
        cancelTaskAlarm(task);
        if (task.getWantDoneDate() != null && task.getWantDoneDate().after(Calendar.getInstance().getTime())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, task.getWantDoneDate().getTime(), getAlarmPendingIntent(task));
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, task.getWantDoneDate().getTime(), getAlarmPendingIntent(task));
            }
        }
    }

    public static boolean recoveryTask(Task task) {
        ContentValues values = new ContentValues();
        values.put("isDelete", false);
        return DataSupport.updateAll(Task.class, values, "taskId=?", task.getTaskId()) > 0;
    }

    //永久删除
    public static boolean alwaysDelete(Task task) {
        cancelTaskAlarm(task);
        return DataSupport.deleteAll(Task.class, "taskId=?", task.getTaskId()) > 0;
    }

    //根据task获取到通知的id
    public static int getTaskID(Task task) {
        return Integer.parseInt(task.getTaskId().substring(task.getTaskId().length() - 5, task.getTaskId().length()));
    }

    private static PendingIntent getAlarmPendingIntent(Task task) {
        Intent intent = new Intent(TaskTimeCheckReceiver.ACTION);
        intent.putExtra("taskId", task.getId());
        return PendingIntent.getBroadcast(TodoApplication.getContext(), task.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static Task getHelpTask() {
        Task task = new Task("欢迎使用任务备忘录，点击查看应用使用帮助哦", AppConstant.APP_HELP_URL);
        Action action = new Action("任务备忘录使用帮助", AppConstant.APP_HELP_URL,
                TodoApplication.getContext().getResources().getDrawable(R.drawable.ic_url),
                Action.TYPE_URL, task.getTaskId());
        action.save();
        task.save();
        return task;
    }

}
