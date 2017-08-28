package com.example.mengfei.todo.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.activity.inter.UiShower;

/**
 * Created by mengfei on 2017/8/28.
 */

public class MyDialog extends Dialog {
    private TextView title;
    private TextView message;
    private TextView okBtn;
    private TextView cancelBtn;
    private ImageView icon;

    public MyDialog(@NonNull Context context) {
        this(context, R.style.MyDialogStyle);
    }

    private MyDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.layout_dialog_base);
        init();
    }

    public void setIcon(UiShower<ImageView> icon) {
        icon.show(this.icon);
    }

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

    private void init() {
        title = (TextView) findViewById(R.id.tv_dialog_title);
        message = (TextView) findViewById(R.id.tv_dialog_message);
        okBtn = (TextView) findViewById(R.id.tv_dialog_ok);
        cancelBtn = (TextView) findViewById(R.id.tv_dialog_cancel);
        icon = (ImageView) findViewById(R.id.iv_dialog_icon);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setMessage(String message) {
        this.message.setText(message);
    }

    public void setIcon(@DrawableRes int icon) {
        this.icon.setImageResource(icon);
    }
}
