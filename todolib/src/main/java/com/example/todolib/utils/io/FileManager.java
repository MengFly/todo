package com.example.todolib.utils.io;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件的管理类
 * Created by mengfei on 2017/3/23.
 */
public class FileManager {

    public static void saveBitmap(Bitmap bitmap, String filename) {
        FileOutputStream outputStream = null;
        try {
            File saveFile = new File(filename);
            if (saveFile.exists()) {
                saveFile.delete();
            }
            saveFile.createNewFile();
            outputStream = new FileOutputStream(saveFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            StreamManager.closeAll(outputStream);
        }
    }
}
