package com.example.mengfei.todo.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.mengfei.todo.entity.Task;


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
            } else {
                Toast.makeText(context, "task is null", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
