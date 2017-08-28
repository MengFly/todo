package com.example.mengfei.todo.entity;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.example.mengfei.todo.AppConstant;
import com.example.mengfei.todo.reciver.TaskTimeCheckReceiver;

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

    // 放进回收站
    public static boolean deleteTask(Task task) {
        ContentValues values = new ContentValues();
        values.put("isDelete", true);
        return DataSupport.updateAll(Task.class, values, "taskId=?", task.getTaskId()) > 0;
    }

    public static boolean recoveryTask(Task task) {
        ContentValues values = new ContentValues();
        values.put("isDelete", false);
        return DataSupport.updateAll(Task.class, values, "taskId=?", task.getTaskId()) > 0;
    }

    //永久删除
    public static  boolean alwaysDelete(Task task) {
        return DataSupport.deleteAll(Task.class, "taskId=?", task.getTaskId()) > 0;
    }

    //根据task获取到通知的id
    public static int getTaskID(Task task) {
        return Integer.parseInt(task.getTaskId().substring(task.getTaskId().length() - 7, task.getTaskId().length()));
    }


    public static Task getHelpTask() {
        Task task = new Task("欢迎使用任务备忘录，点击查看应用使用帮助哦", AppConstant.APP_HELP_URL);
        task.save();
        return task;
    }

}
