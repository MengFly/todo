package com.example.mengfei.todo.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.text.util.Linkify;
import android.widget.TextView;

import com.example.mengfei.todo.TodoApplication;
import com.example.mengfei.todo.activity.WebActivity;
import com.example.todolib.utils.ClipboardUtils;

import me.saket.bettermovementmethod.BetterLinkMovementMethod;

/**
 * Created by mengfei on 2017/3/19.
 */

public class LinkManager {

    public static void  linkClick(final Context context, TextView textView) {
        BetterLinkMovementMethod.linkify(Linkify.ALL, textView).setOnLinkClickListener(new BetterLinkMovementMethod.OnLinkClickListener() {
            @Override
            public boolean onClick(TextView textView, String url) {
                if (url.startsWith("tel:")) {
                    ClipboardUtils.setTextClip(TodoApplication.getContext(), url.substring("tel:".length(), url.length()));
                    Snackbar.make(textView, "已经复制到剪贴板", Snackbar.LENGTH_SHORT).show();
                    return true;
                } else if (url.startsWith("mailto:")) {
                    ClipboardUtils.setTextClip(TodoApplication.getContext(), url.substring("mailto:".length(), url.length()));
                    Snackbar.make(textView, "已经复制到剪贴板", Snackbar.LENGTH_SHORT).show();
                    return true;
                } else {
                    WebActivity.StartWebActivityWithURL(context, url);
                    return true;
                }
            }
        });
    }
}
