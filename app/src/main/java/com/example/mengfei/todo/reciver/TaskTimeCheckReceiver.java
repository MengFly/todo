package com.example.mengfei.todo.reciver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.util.Log;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.activity.EditTaskActivity;
import com.example.mengfei.todo.activity.TaskOpenActivity;
import com.example.mengfei.todo.entity.Talk;
import com.example.mengfei.todo.entity.Task;
import com.example.mengfei.todo.entity.TaskManager;

import java.util.ArrayList;
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
        Intent intent;
        if (!Task.TASK_TYPE_TEXT.equals(task.getTaskType())) {
            intent = new Intent(context, TaskOpenActivity.class);

        } else {
            intent = new Intent(context, EditTaskActivity.class);
        }
        intent.putExtra("task", task);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(pendingIntent);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_app_icon));
        builder.setSmallIcon(R.drawable.ic_app_small);
        ((NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE)).notify(getTaskID(task), builder.build());
    }

    //根据task获取到通知的id
    public int getTaskID(Task task) {
        return Integer.parseInt(task.getTaskId().substring(task.getTaskId().length() - 7, task.getTaskId().length()));
    }

}
