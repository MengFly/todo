package com.example.mengfei.todo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mengfei.todo.AppConfig;
import com.example.mengfei.todo.R;


/**
 * 忘记密码的界面
 * Created by mengfei on 2017/5/17.
 */
public class PassWdForgetActivity extends BaseActivity {

    private EditText newPassEt;
    private TextView mibaoQTv;
    private EditText mibaoAEt;
    private Button resetMibaoBtn;
    private Button setNewPassBtn;

    private AppConfig config;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        config = AppConfig.getInstance(mContext);
        setContentView(R.layout.layout_activity_set_new_pass);
        newPassEt = (EditText) findViewById(R.id.et_set_new_pass);
        mibaoQTv = (TextView) findViewById(R.id.tv_mibao_q);
        mibaoAEt = (EditText) findViewById(R.id.et_mibao_a);
        resetMibaoBtn = (Button) findViewById(R.id.btn_reset_mibao);
        setNewPassBtn = (Button) findViewById(R.id.btn_set_pass);
        initListener();
        initUI();
    }

    private void initListener() {
        setNewPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkMibao()) {
                    String newPass = newPassEt.getText().toString();
                    if (TextUtils.isEmpty(newPass)) {
                        showToast("密码不能为空");
                    } else {
                        config.setPassWord(newPass);
                        showToast("设置成功");
                        openOtherActivity(PassWordInputActivity.class, true);
                    }
                }
            }
        });
        resetMibaoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mibaoA = mibaoAEt.getText().toString();
                if (TextUtils.isEmpty(mibaoA)) {
                    showSnackbar(v, "密保答案不能为空");
                } else {
                    if (AppConfig.getInstance(mContext).checkMibao(mibaoA.trim())) {
                        openOtherActivity(PassWordSetMibaoActivity.class, false);
                    } else {
                        showSnackbar(v, "密保答案不正确");
                    }
                }
            }
        });
    }

    private boolean checkMibao() {
        String mibao = mibaoAEt.getText().toString();
        if (TextUtils.isEmpty(mibao)) {
            showToast("密保答案不能为空");
            return false;
        } else {
            if (config.checkMibao(mibao)) {
                return true;
            } else {
                showToast("密保答案不正确");
                return false;
            }
        }
    }

    private void initUI() {
        mibaoQTv.setText(config.getMibaoQ());
        initActionBar("修改密码", null, true);
    }


}
