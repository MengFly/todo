package com.example.mengfei.todo.reciver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.mengfei.todo.AppConfig;
import com.example.mengfei.todo.R;
import com.example.mengfei.todo.activity.ShareTaskActivity;
import com.example.mengfei.todo.entity.Action;
import com.example.mengfei.todo.entity.Task;

import org.litepal.crud.DataSupport;


/**
 * 用于检测时间的Receiver
 * Created by mengfei on 2017/3/20.
 */
public class TaskTimeCheckReceiver extends BroadcastReceiver {

    public static String ACTION = "com.mengfei.todo.CHECK_TASK_TIME";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && ACTION.equals(intent.getAction())) {
            int taskId = intent.getIntExtra("taskId", -1);
            Task task = DataSupport.find(Task.class, taskId);
            if (task != null) {
                sendTaskNotification(context, task);
            }
        }
    }

    private void sendTaskNotification(Context context, Task task) {
        Intent intent = new Intent(context, ShareTaskActivity.class);
        intent.putExtra("task", task);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendIntent = PendingIntent.getActivity(context, task.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .setContentTitle(task.getTitle())
                .setContentText(task.getDesc() == null ? "没有描述" : task.getDesc())
                .setSmallIcon(R.drawable.ic_app_small)
                .setLargeIcon(getLargeIcon(context, task))
                .setContentIntent(pendIntent)
                .setSound(AppConfig.getInstance(context).getNotificationSoundUri())
                .setAutoCancel(true);
        Notification notification = builder.build();
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(task.getId(), notification);
    }

    private Bitmap getLargeIcon(Context context, Task task) {
        Action action = task.getAction();
        if (action != null) {
            return action.getShowIcon();
        } else {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_app_icon);
        }
    }


}
