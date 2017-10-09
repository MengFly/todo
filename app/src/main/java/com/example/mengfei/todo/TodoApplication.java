package com.example.mengfei.todo;


import android.app.AlarmManager;

import com.aitangba.swipeback.ActivityLifecycleHelper;
import com.tencent.smtt.sdk.QbSdk;

import org.litepal.LitePalApplication;

public class TodoApplication extends LitePalApplication {

    public static AlarmManager getAlarmManager() {
        return (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        QbSdk.initX5Environment(this, null);//加载腾讯X5内核
        registerActivityLifecycleCallbacks(ActivityLifecycleHelper.build());
    }

}
