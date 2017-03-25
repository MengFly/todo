package com.example.todolib.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * Created by mengfei on 2017/3/18.
 */
public class ClipboardUtils {

    private static ClipboardManager getClipboardManager(Context context) {
        Context applicationContext = context.getApplicationContext();
        return (ClipboardManager) applicationContext.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    public static void setTextClip(Context context, String textClip) {
        ClipData clipData = ClipData.newPlainText("text", textClip);
        getClipboardManager(context).setPrimaryClip(clipData);
    }

    /**
     * 获取剪贴板的文本
     *
     * @return 剪贴板的文本
     */
    public static CharSequence getText(Context context) {
        ClipboardManager clipboard = getClipboardManager(context);
        ClipData clip = clipboard.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            return clip.getItemAt(0).coerceToText(context.getApplicationContext());
        }
        return null;
    }

}
