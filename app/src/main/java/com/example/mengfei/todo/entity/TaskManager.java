package com.example.mengfei.todo.entity;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.example.mengfei.todo.reciver.TaskTimeCheckReceiver;
import com.example.todolib.utils.date.DateTools;

import org.litepal.crud.DataSupport;

import java.util.Date;
import java.util.List;

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

    public static boolean updateTask(Task task, String title, String desc) {
        task.setTitle(title);
        task.setDesc(desc);
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("desc", desc);
        if (task.getWantDoneDate() != null) {
            values.put("wantDoneDate", task.getWantDoneDate().getTime());
        }
        return DataSupport.updateAll(Task.class, values, "taskId=?", task.getTaskId()) > 0;
    }

    public static List<Task> getCompletedTask() {
        return DataSupport.where("isCompleted=?", String.valueOf(1)).order("createDate").find(Task.class);
    }

    public static List<Task> getNotCompletedTask() {
        return DataSupport.where("isCompleted=?", String.valueOf(0)).order("createDate").find(Task.class);
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


}
