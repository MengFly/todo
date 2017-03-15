package com.example.todolib.utils;

import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * 用于提供一些分享有关的方法
 * Created by mengfei on 2017/1/5.
 */
public class ShareTools {

    public static Intent getShareImageIntent(File imageFile) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(imageFile));
        return intent;
    }
}
