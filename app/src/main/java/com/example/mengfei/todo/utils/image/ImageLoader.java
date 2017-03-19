package com.example.mengfei.todo.utils.image;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;

/**
 * Created by mengfei on 2017/3/18.
 */
public class ImageLoader {

    public static void loadImage(Context context, String url, ImageView imageView, Transformation transformation) {
        if (transformation != null) {
            Glide.with(context).load(url).bitmapTransform(transformation).into(imageView);
        } else  {
            Glide.with(context).load(url).into(imageView);
        }
    }
}
