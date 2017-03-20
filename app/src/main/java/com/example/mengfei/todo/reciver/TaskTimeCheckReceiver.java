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
    private List<Task> notDoneAndWantDoneTimeNotNullList = null;
    private int datasCounts = 0;//检测数据库中的记录数据，用于判断数据有没有改变

    private static final long CHECK_TIME = 1000 * 60 * 3;//判定时间为3分钟
    private NotificationManager manager;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: " + intent.getAction().toString());
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
            initList();
            checkTask(context);
        }
    }

    //检测任务时候快到预计完成的时间了
    private void checkTask(Context context) {
        for (Task task : notDoneAndWantDoneTimeNotNullList) {
            if (task.getWantDoneDate().after(new Date()) &&
                    task.getWantDoneDate().getTime() - new Date().getTime() < CHECK_TIME) {
                sendNotification(task, context);
            }
        }
    }

    //发送一个通知，表示已经到了这个任务的完成时间了
    private void sendNotification(Task task, Context context) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(task.getTitle());
        builder.setTicker("一个任务快到预计时间了");
        builder.setAutoCancel(true);
        builder.setContentText(task.getTitle() == null ? task.getDesc() : task.getTitle());
        if (Build.VERSION.SDK_INT >= 17) {
            builder.setShowWhen(true);
        }
        Intent intent = new Intent(context, EditTaskActivity.class);
        intent.putExtra("task", task);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(pendingIntent);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_app_icon));
        builder.setSmallIcon(R.drawable.ic_app_small);
        manager.notify(getTaskID(task), builder.build());
    }

    //根据task获取到通知的id
    public int getTaskID(Task task) {
        return Integer.parseInt(task.getTaskId().substring(task.getTaskId().length() - 7, task.getTaskId().length()));
    }

    private void initList() {
        if (notDoneAndWantDoneTimeNotNullList == null || datasCounts != Task.count(Task.class)) {
            List<Task> notDontTaskList = TaskManager.getNotCompletedTask();
            datasCounts = Task.count(Task.class);//更新数据
            if (notDoneAndWantDoneTimeNotNullList == null) {
                notDoneAndWantDoneTimeNotNullList = new ArrayList<>();
            }
            notDoneAndWantDoneTimeNotNullList.clear();
            for (Task task : notDontTaskList) {
                if (task.getWantDoneDate() != null) {
                    notDoneAndWantDoneTimeNotNullList.add(task);
                }
            }
        }
        Log.d(TAG, "initList: " + notDoneAndWantDoneTimeNotNullList.size());
    }

}
