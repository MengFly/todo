package com.example.todolib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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

    /**
     * 将drawable对象转换成字符数组
     *
     * @return 转换后的byte数组
     */
    public static byte[] changeDrawableToByte(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap changeByteToBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static Bitmap changeDrawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

}

