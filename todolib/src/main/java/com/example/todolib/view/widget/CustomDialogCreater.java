package com.example.todolib.view.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by mengfei on 2017/3/15.
 */
public class CustomDialogCreater{

    private View RootView;


    public static AlertDialog getViewDialog(Context context, int viewRes, ResourceBinder binder, DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(viewRes, null, false);
        builder.setView(view);
        if (binder!= null) {
            binder.bindView(view);
        }
        builder.setPositiveButton("确定", okListener);
        return builder.create();
    }

    public static AlertDialog getItemsDialog(Context context, CharSequence title, String[] items, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        TextView textView = new TextView(context);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        int size = getSize(context, 14);
textView.setPadding(size, size, size, size);
        textView.setText(title);
        builder.setCustomTitle(textView);
        builder.setTitle(title);
        builder.setItems(items, listener);
        return builder.create();
    }

    public interface  ResourceBinder {
        void bindView(View rootView);
    }

    public static  int getSize(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) (dp * displayMetrics.density);
    }
}
