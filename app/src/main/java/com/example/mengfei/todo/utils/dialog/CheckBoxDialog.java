package com.example.mengfei.todo.utils.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.mengfei.todo.R;

/**
 * 获取一个带有CheckBox的dialog
 * Created by mengfei on 2017/3/26.
 */

public class CheckBoxDialog {

    public static AlertDialog getDialog(Context context, String title, String message, String checkString, CompoundButton.OnCheckedChangeListener listener,
                                        String okStr, String cancelStr, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View rootView = LayoutInflater.from(context).inflate(R.layout.layout_dialog_check, null);
        builder.setView(rootView);
        if (title == null) {
            rootView.findViewById(R.id.tv_dialog_title).setVisibility(View.GONE);
        } else {
            ((TextView) rootView.findViewById(R.id.tv_dialog_title)).setText(title);
        }
        if (message == null) {
            rootView.findViewById(R.id.tv_dialog_message).setVisibility(View.GONE);
        } else {
            ((TextView) rootView.findViewById(R.id.tv_dialog_message)).setText(message);
        }
        if (checkString == null) {
            rootView.findViewById(R.id.cb_dialog).setVisibility(View.GONE);
        } else {
            ((CheckBox) rootView.findViewById(R.id.cb_dialog)).setText(checkString);
            ((CheckBox) rootView.findViewById(R.id.cb_dialog)).setOnCheckedChangeListener(listener);
        }
        builder.setPositiveButton(okStr, okListener);
        builder.setNegativeButton(cancelStr, cancelListener);
        return builder.create();
    }
}
