package com.example.mengfei.todo.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * 用于管理整个项目的文件
 * Created by mengfei on 2017/3/26.
 */
public class AppFileManager {

    public static final String DIRECTORY_DCIM = "DCIM";
    public static final String DIRECTORY_DOWNLOADS = "Download";


    public static String getShareFileName(String fileName) {
        //获取地址
        String parentDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        return parentDir + "/" + fileName + ".png";
    }

    public static File getAppRootCacheDir(boolean isExternal, Context context) {
        if (isExternal) {
            return context.getExternalCacheDir();
        } else {
            return context.getCacheDir();
        }
    }

    public static File getAppRootFileDir(boolean isExternal, Context context) {
        if (isExternal) {
            return context.getExternalFilesDir("");
        } else {
            return context.getFilesDir();
        }
    }

    /**
     * 返回SD卡是否可用
     */
    public static boolean isExternal() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                || Environment.isExternalStorageRemovable();
    }


}
