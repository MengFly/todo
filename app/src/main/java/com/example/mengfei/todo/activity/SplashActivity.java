package com.example.mengfei.todo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.mengfei.todo.AppConfig;
import com.example.mengfei.todo.R;

/**
 * 刷新界面的Activity
 * Created by mengfei on 2017/5/17.
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppConfig.getInstance(mContext).hasPassWd() || AppConfig.getInstance(mContext).getSwitchPass()) {
            openOtherActivity(PassWordInputActivity.class, true);
        } else {
            openOtherActivity(MainActivity.class, true);
        }
    }
}
