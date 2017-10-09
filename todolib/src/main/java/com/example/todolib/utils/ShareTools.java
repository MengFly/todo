package com.example.todolib.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.util.UUID;

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

    //获取到相机的Intent
    public static Intent getCreamerIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/" + "todo/" + UUID.randomUUID() + ".jpg")));
        return intent;
    }


    public static Intent getImageFromAlbumIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
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

    public static Intent getBrowseIntent(String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        return intent;
    }

    //获取发送text的Intent
    public static Intent getShareTextIntent(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        return intent;
    }

    //获取到拨打电话的Intent
    public static Intent getCallIntent(String callStr) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + callStr);
        intent.setData(data);
        return intent;
    }
}
