package com.example.mengfei.todo.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.mengfei.todo.AppConfig;
import com.example.mengfei.todo.R;
import com.example.mengfei.todo.activity.BaseActivity;
import com.example.todolib.view.widget.CustomDialogCreater;

/**
 * 輸入密碼的界面
 * Created by mengfei on 2017/5/17.
 */

public class PassWordInputActivity extends BaseActivity {

    private EditText passWordEt;
    private Button okBtn, forgetPassBtn;
    private CheckBox passCB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_input_pass);
        initView();
        initListener();
    }

    private void initListener() {
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppConfig.getInstance(mContext).checkPassWorld(passWordEt.getText().toString())) {
                    showToast("密码正确");
                    openOtherActivity(MainActivity.class, true);
                } else {
                    showSnackbar(okBtn, "你的密码不正确");
                }
            }
        });
        forgetPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOtherActivity(PassWdForgetActivity.class, false);
            }
        });

        passCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                final AppConfig config = AppConfig.getInstance(mContext);
                if(isChecked != config.getSwitchPass())  {
                    buttonView.setChecked(config.getSwitchPass());
                }
                if (!isChecked) {
                    String pass = passWordEt.getText().toString();
                    if (config.checkPassWorld(pass)) {
                        CustomDialogCreater.getSimpleDialog(mContext, "提示!", "关闭密码功能后将清除您之前设置的密码和密保问题， 是否要继续？", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                config.setSwitchPass(false);
                                buttonView.setChecked(false);
                                config.clearPass();
                                showToast("密码已关闭");
                                finish();
                            }
                        }, null).show();
                    } else {
                        showToast("密码不正确，无法关闭密码功能");
                    }
                } else {
                    config.setSwitchPass(true);
                }
            }
        });

    }

    private void initView() {
        passWordEt = (EditText) findViewById(R.id.et_pass_input);
        okBtn = (Button) findViewById(R.id.btn_ok);
        forgetPassBtn = (Button) findViewById(R.id.btn_forget_passwd);
        passCB = (CheckBox) findViewById(R.id.cb_pass);
        passCB.setChecked(AppConfig.getInstance(mContext).getSwitchPass());
        initActionBar("输入密码", null, false);
    }
}
