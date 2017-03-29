package com.example.mengfei.todo.utils;

import android.os.Environment;

/**
 * 用于管理整个项目的文件
 * Created by mengfei on 2017/3/26.
 */
public class AppFileManager {


    public static String getShareFileName(String fileName) {
        //获取地址
        String parentDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        return parentDir + "/" + fileName + ".png";
    }
}
