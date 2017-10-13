package com.example.mengfei.todo.entity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.example.mengfei.todo.TodoApplication;
import com.example.mengfei.todo.activity.BaseActivity;
import com.example.mengfei.todo.activity.WebActivity;
import com.example.todolib.utils.ImageUtils;
import com.example.todolib.utils.ShareTools;

import org.bouncycastle.util.Pack;
import org.litepal.crud.DataSupport;

public class Action extends DataSupport {
    public static String TYPE_URL = "type_url";
    public static String TYPE_Email = "type_email";
    public static String TYPE_App = "type_app";
    public static String TYPE_Phone = "type_phone";

    public Action() {
    }

    public Action(String title, String desc, Drawable icon, String type, String taskId) {
        this(title, desc, ImageUtils.changeDrawableToBitmap(icon), type, taskId);
    }

    public Action(String title, String desc, Bitmap icon, String type, String taskId) {
        this.title = title;
        this.desc = desc;
        this.type = type;
        this.taskId = taskId;
        if (icon != null)
            this.icon = ImageUtils.changeDrawableToByte(icon);
    }

    public Action(String title, String desc, int id, String type, String taskId) {
        this(title, desc, TodoApplication.getContext().getResources().getDrawable(id), type, taskId);
    }

    public String taskId;
    public String title;
    public String desc;
    public byte[] icon;
    public String type;

    public Bitmap getShowIcon() {
        return ImageUtils.changeByteToBitmap(icon);
    }

    public void doAction(Context context) {
        if (type.equals(TYPE_App)) {
            PackageManager pm = TodoApplication.getContext().getPackageManager();
            Intent intent = pm.getLaunchIntentForPackage(desc);
            if (((BaseActivity) context).isUsedIntentActivity(intent)) {
                context.startActivity(intent);
            }
        } else if (type.equals(TYPE_Email)) {
            String emailAddress = title;
            String emailContent = desc;
            Intent emailIntent = ShareTools.getSendEmailIntent(emailAddress, null, emailContent);
            if (((BaseActivity) context).isUsedIntentActivity(emailIntent)) {
                context.startActivity(emailIntent);
            } else {
                ((BaseActivity) context).showToast("没有可以执行的应用");
            }
        } else if (type.equals(TYPE_URL)) {
            Intent urlIntent = ShareTools.getBrowseIntent(desc);
            if (((BaseActivity) context).isUsedIntentActivity(urlIntent)) {
                context.startActivity(urlIntent);
            } else {
                WebActivity.StartWebActivityWithURL(context, desc);
            }
        } else if (type.equals(TYPE_Phone)) {
            Intent callIntent = ShareTools.getCallIntent(desc);
            if (((BaseActivity) context).isUsedIntentActivity(callIntent)) {
                context.startActivity(callIntent);
            } else {
                ((BaseActivity) context).showToast("没有可执行的应用程序");
            }
        }
    }

}
