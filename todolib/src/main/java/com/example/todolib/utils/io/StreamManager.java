package com.example.todolib.utils.io;

import java.io.Closeable;
import java.io.IOException;

/**
 * 这个类用于提供一些有关安卓数据流的简便操作
 * Created by mengfei on 2017/3/23.
 */
public class StreamManager {

    public static void closeAll(Closeable... streams) {
        for (Closeable closeable : streams) {
            try {
                if (closeable != null) {
                    closeable.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}