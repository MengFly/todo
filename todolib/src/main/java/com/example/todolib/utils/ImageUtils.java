package com.example.todolib.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by mengfei on 2017/3/17.
 */

public class ImageUtils {


    public static void adjustImageToFullScreen(WindowManager manager, ImageView imageView, int width, int height) {
        DisplayMetrics metric = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metric);
        int metricsWidth = metric.widthPixels;     // 屏幕宽度（像素）
        int h = metricsWidth / width * height;
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.width = metricsWidth;
        params.height = h;
        imageView.setLayoutParams(params);
    }
}

