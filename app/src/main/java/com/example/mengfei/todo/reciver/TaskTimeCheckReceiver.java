package com.example.mengfei.todo.reciver;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.mengfei.todo.entity.Task;
import com.example.mengfei.todo.entity.TaskManager;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * 用于检测时间的Receiver
 * Created by mengfei on 2017/3/20.
 */
public class TaskTimeCheckReceiver extends BroadcastReceiver {
    private static final String TAG = "receiver";
    private static final long CHECK_TIME = 1000 * 60 * 3;//判定时间为3分钟

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
            checkTask(context);
        }
    }

    //检测任务时候快到预计完成的时间了
    private void checkTask(Context context) {
        List<Task> notDoneTask = TaskManager.getNotCompletedTask();
        for (Task task : notDoneTask) {
            if (task.getWantDoneDate() != null) {
                if (task.getWantDoneDate().after(Calendar.getInstance().getTime()) &&
                        task.getWantDoneDate().getTime() - new Date().getTime() < CHECK_TIME) {
                    sendNotification(task, context);
                }
            }
        }
    }

    //发送一个通知，表示已经到了这个任务的完成时间了
    private void sendNotification(Task task, Context context) {
        Notification.Builder builder = new Notification.Builder(context.getApplicationContext());
        builder.setContentTitle(task.getTitle());
        builder.setTicker("一个任务快到预计时间了");
        builder.setAutoCancel(true);
        builder.setContentText(task.getTitle() == null ? task.getDesc() : task.getTitle());
        if (Build.VERSION.SDK_INT >= 17) {
            builder.setShowWhen(true);
        }
    }
    //根据task获取到通知的id
    public int getTaskID(Task task) {
        return Integer.parseInt(task.getTaskId().substring(task.getTaskId().length() - 7, task.getTaskId().length()));
    }

}
