package com.example.todolib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;

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

    public static Bitmap getScrollViewImage(ScrollView view) {
        int viewHeight = 0;
        for (int i = 0; i < view.getChildCount(); i++) {
            viewHeight += view.getChildAt(i).getHeight();
        }
        // 创建对应大小的bitmap
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), viewHeight,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
}

