package com.example.mengfei.todo.entity;

import android.content.ContentValues;
import android.text.TextUtils;

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
        return DataSupport.updateAll(Task.class, values, "taskId=?", task.getTaskId()) > 0;
    }

    public static List<Task> getCompletedTask() {
        return DataSupport.where("isCompleted=?", String.valueOf(1)).order("createDate").find(Task.class);
    }

    public static List<Task> getNotCompletedTask() {
        return DataSupport.where("isCompleted=?", String.valueOf(0)).order("createDate").find(Task.class);
    }

    public static boolean deleteTask(Task task) {
        if(DataSupport.deleteAll(Task.class, "taskId=?", task.getTaskId()) > 0) {
            DataSupport.deleteAll(Talk.class, "taskId=?", task.getTaskId());
            return true;
        }else {
            return false;
        }
    }


}
