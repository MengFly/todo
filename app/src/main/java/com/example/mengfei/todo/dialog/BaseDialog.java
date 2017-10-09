package com.example.mengfei.todo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.TextView;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.TodoApplication;

/**
 * Created by mengfei on 2017/8/29.
 */

public abstract class BaseDialog extends Dialog {
    protected TextView okBtn;
    protected TextView cancelBtn;

    public BaseDialog(@NonNull Context context) {
        this(context, R.style.MyDialogStyle);
    }

    public BaseDialog(@NonNull Context context, @StyleRes int themeResId) {
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

    public void changeOkBtnStat(boolean enabled) {
        if (okBtn == null) return;
        okBtn.setEnabled(enabled);
        okBtn.setClickable(enabled);
        okBtn.setFocusable(enabled);
        if (enabled) {
            okBtn.setTextColor(TodoApplication.getContext().getResources().getColor(R.color.app_btn_color));
        } else {
            okBtn.setTextColor(TodoApplication.getContext().getResources().getColor(R.color.color_text_disabled_dark));
        }
    }

    public void setCancelListener(View.OnClickListener listener) {
        setCancelListener(listener, true, null);
    }

    public void setCancelListener(final View.OnClickListener listener, final boolean isDismiss, String btnName) {
        if (btnName != null) {
            cancelBtn.setText(btnName);
        }
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
                    if (isDismiss) {
                        dismiss();
                    }
                }
            });
        }
    }
}
