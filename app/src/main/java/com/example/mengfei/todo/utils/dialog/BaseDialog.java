package com.example.mengfei.todo.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.TextView;

import com.example.mengfei.todo.R;

/**
 * Created by mengfei on 2017/8/29.
 */

public abstract class BaseDialog extends Dialog {
    protected TextView okBtn;
    protected TextView cancelBtn;

    public BaseDialog(@NonNull Context context) {
        this(context, R.style.MyDialogStyle);
    }

    private BaseDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        initView();
        initBtn();
    }

    private void initBtn() {
        okBtn = (TextView) findViewById(R.id.tv_dialog_ok);
        cancelBtn = (TextView) findViewById(R.id.tv_dialog_cancel);
    }

    protected abstract void initView();

    public void setOkListener(final View.OnClickListener listener) {
        if (listener == null) {
            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        } else {
            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(v);
                    dismiss();
                }
            });
        }
    }

    public void setCancelListener(final View.OnClickListener listener) {
        if (listener == null) {
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        } else {
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(v);
                    dismiss();
                }
            });
        }
    }
}
