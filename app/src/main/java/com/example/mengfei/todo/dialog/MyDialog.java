package com.example.mengfei.todo.dialog;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.activity.inter.UiShower;

/**
 * 简单的展示Message的Dialog
 * Created by mengfei on 2017/8/28.
 */
public class MyDialog extends BaseDialog {
    private TextView title;
    private TextView message;
    private ImageView icon;

    public MyDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.layout_dialog_base);
        title = (TextView) findViewById(R.id.tv_dialog_title);
        message = (TextView) findViewById(R.id.tv_dialog_message);
        icon = (ImageView) findViewById(R.id.iv_dialog_icon);
    }

    public void setIcon(UiShower<ImageView> icon) {
        icon.show(this.icon);
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
