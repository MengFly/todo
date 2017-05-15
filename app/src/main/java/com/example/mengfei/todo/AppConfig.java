package com.example.mengfei.todo;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * App的的所有配置信息
 * Created by mengfei on 2017/3/26.
 */
public class AppConfig {


    private static final String CONFIG_SPF_NAME = "todo-config";
    private static AppConfig mInstance;
    private SharedPreferences configSPF;

    private static final String KEY_IS_TIME_TOO_LATE = "is-time-too-late";
    private boolean isTimeTooLateTipShow = true;//标识着主界面的提示时间太晚的提示框是否显示。默认是显示
    private static final String KEY_IS_DELETE_TIP = "is-delete-tip";
    private boolean isDeleteTipShow = true;//标识着当删除任务的时候是否出现提示框，默认是显示

    private AppConfig(Context context) {
        configSPF = context.getSharedPreferences(CONFIG_SPF_NAME, Context.MODE_PRIVATE);
        isTimeTooLateTipShow = configSPF.getBoolean(KEY_IS_TIME_TOO_LATE, true);
        isDeleteTipShow = configSPF.getBoolean(KEY_IS_DELETE_TIP, true);
    }

    public boolean isTimeTooLateTipShow() {
        return isTimeTooLateTipShow;
    }

    public void setTimeTooLateTipShow(boolean timeTooLateTipShow) {
        isTimeTooLateTipShow = timeTooLateTipShow;
        SharedPreferences.Editor editor = configSPF.edit();
        editor.putBoolean(KEY_IS_TIME_TOO_LATE, isTimeTooLateTipShow);
        editor.apply();
    }

    public boolean isDeleteTipShow() {
        return isDeleteTipShow;
    }

    public void setDeleteTipShow(boolean deleteTipShow) {
        isDeleteTipShow = deleteTipShow;
        SharedPreferences.Editor editor = configSPF.edit();
        editor.putBoolean(KEY_IS_DELETE_TIP, isDeleteTipShow);
        editor.apply();
    }

    public static AppConfig getInstance(Context context) {
        if (mInstance == null) {
            synchronized (AppConfig.class) {
                if (mInstance == null) {
                    mInstance = new AppConfig(context.getApplicationContext());
                }
            }
        }
        return mInstance;
    }
}
