package com.example.mengfei.todo.reciver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.activity.EditTaskActivity;
import com.example.mengfei.todo.entity.Task;
import com.example.mengfei.todo.entity.TaskManager;


/**
 * 用于检测时间的Receiver
 * Created by mengfei on 2017/3/20.
 */
public class TaskTimeCheckReceiver extends BroadcastReceiver {
    public static final String ACTION = "com.mengfei.todo.CHECK_TASK_TIME";


    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "receive", Toast.LENGTH_SHORT).show();
        if (intent.getAction().equals(ACTION)) {
            Task task = (Task) intent.getSerializableExtra("task");
            if (task != null) {
                sendNotification(context, task);
            } else {
                Toast.makeText(context, "task is null", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendNotification(Context context, Task task) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(task.getTitle());
        builder.setContentText(task.getDesc());
        builder.setAutoCancel(true);
        builder.setContentIntent(getTaskIntent(context, task));
        builder.setSmallIcon(R.drawable.ic_app_icon);
        builder.setDefaults(Notification.DEFAULT_ALL);
        NotificationManager systemService = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        systemService.notify(TaskManager.getTaskID(task), builder.build());
    }

    private PendingIntent getTaskIntent(Context context, Task task) {
        Intent intent = new Intent(context, EditTaskActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("task", task);
        return PendingIntent.getActivity(context, TaskManager.getTaskID(task), intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
