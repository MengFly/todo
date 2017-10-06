package com.example.mengfei.todo;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.todolib.en_de_code.EncodeAndDecodeFactory;
import com.example.todolib.utils.io.StreamManager;

import org.w3c.dom.Text;


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
    private static final String KEY_IS_DELETE_TIP = "is-delete-type";
    private boolean isDeleteTipShow = true;//标识着当删除任务的时候是否出现提示框，默认是显示

    private static final String KEY_switch_pass = "switch_pass";
    //密码的key
    private static final String  KEY_PASSWORD = "passwd";
    //密保问题的key
    private static final String KEY_MIBAO_Q = "mibao_q";
    private static final String  KEY_MIBAO = "mibao";

    //应用是否是第一次安装
    private static final String KEY_IS_FIRST_INSTALL = "is-first-install";

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
        configSPF.edit().putBoolean(KEY_IS_TIME_TOO_LATE, isTimeTooLateTipShow).apply();
    }

    //设置密码是否开启
    public void setSwitchPass(boolean b) {
        configSPF.edit().putBoolean(KEY_switch_pass, b).apply();
    }

    public boolean getSwitchPass() {
        return configSPF.getBoolean(KEY_switch_pass, false);
    }

    /**
     * 设置PassWord
     */
    public void setPassWord(String passWord) {
        String encodePass = EncodeAndDecodeFactory.getMd5Degist().md5Degist(passWord.trim());
        configSPF.edit().putString(KEY_PASSWORD, encodePass).apply();
    }

    public void setMibaoQ(String mibaoQ) {
        configSPF.edit().putString(KEY_MIBAO_Q, mibaoQ).apply();
    }

    public String getMibaoQ() {
        return configSPF.getString(KEY_MIBAO_Q, null);
    }

    /**
     * 检测password
     */
    public boolean checkPassWorld(String passWord) {
        if (TextUtils.isEmpty(passWord)) {
            return false;
        } else {
            return EncodeAndDecodeFactory.getMd5Degist().md5Degist(passWord.trim()).equals(configSPF.getString(KEY_PASSWORD, null));
        }
    }

    /**
     * 设置密保
     */
    public void setMibao(String mibao) {
        String encodePass = EncodeAndDecodeFactory.getMd5Degist().md5Degist(mibao.trim());
        configSPF.edit().putString(KEY_MIBAO, encodePass).apply();
    }

    /**
     * 是否存在密码
     */
    public boolean hasPassWd() {
        return configSPF.getString(KEY_PASSWORD, null) != null;
    }

    /**
     * 检测密保
     */
    public boolean checkMibao(String mibao) {
        if (TextUtils.isEmpty(mibao)) {
            return false;
        } else {
            return EncodeAndDecodeFactory.getMd5Degist().md5Degist(mibao.trim()).equals(configSPF.getString(KEY_MIBAO, null));
        }
    }
    /**
     * App是否是第一次安装
     * @return
     */
    public boolean isFirstInstall() {
        boolean isFirstInstall = configSPF.getBoolean(KEY_IS_FIRST_INSTALL, true);
        if (isFirstInstall) {
           configSPF.edit().putBoolean(KEY_IS_FIRST_INSTALL, false).apply();
        }
        return isFirstInstall;
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

    public void clearPass() {
        SharedPreferences.Editor e = configSPF.edit();
        e.putString(KEY_MIBAO_Q, null);
        e.putString(KEY_MIBAO, null);
        e.putString(KEY_PASSWORD, null);
        e.apply();
    }
}
