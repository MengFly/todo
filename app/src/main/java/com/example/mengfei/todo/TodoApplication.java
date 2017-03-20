package com.example.mengfei.todo;


import com.aitangba.swipeback.ActivityLifecycleHelper;
import com.tencent.smtt.sdk.QbSdk;

import org.litepal.LitePalApplication;

/**
 * Created by mengfei on 2017/3/14.
 */
public class TodoApplication extends LitePalApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        QbSdk.initX5Environment(this, null);//加载腾讯X5内核
        registerActivityLifecycleCallbacks(ActivityLifecycleHelper.build());
    }

}
