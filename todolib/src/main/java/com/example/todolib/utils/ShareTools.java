package com.example.todolib.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;

import java.io.File;

/**
 * 用于提供一些分享有关的方法
 * Created by mengfei on 2017/1/5.
 */
public class ShareTools {

    // 获取到发送图片的Intent
    public static Intent getShareImageIntent(File imageFile) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(imageFile));
        return intent;
    }


    //获取到发送邮件的Intent
    public static Intent getSendEmailIntent(String email, String subject, String text) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + email));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        return intent;
    }

    public static Intent getShareTextIntent(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        return intent;
    }

    public static Intent getCallIntent(String callStr) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" +callStr);
        intent.setData(data);
        return intent;
    }
}
