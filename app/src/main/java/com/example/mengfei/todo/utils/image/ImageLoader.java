package com.example.mengfei.todo.utils.image;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.example.mengfei.todo.R;

/**
 * Created by mengfei on 2017/3/18.
 */
public class ImageLoader {

    public static void loadImage(Context context, String url, ImageView imageView, Transformation transformation) {
        Log.d("URL", "loadImage: " + url);
        if (transformation != null) {
            Glide.with(context).load(url).error(R.drawable.ic_back_error).bitmapTransform(transformation).into(imageView);
        } else  {
            Glide.with(context).load(url).error(R.drawable.ic_back_error).into(imageView);
        }
    }
}
