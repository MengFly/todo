package com.example.mengfei.todo.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.mengfei.todo.reciver.TaskTimeCheckReceiver;

/**
 * 这个Service用于启动检测时间的BrodCaseReciver
 * Created by mengfei on 2017/3/20.
 */
public class TaskTimeCheckService extends Service {


    private static final String TAG = "service";
    private static TaskTimeCheckReceiver receiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        receiver = new TaskTimeCheckReceiver();
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_TIME_TICK));
//        Log.d(TAG, "onCreate: ");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
