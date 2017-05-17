package com.example.mengfei.todo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mengfei.todo.AppConfig;
import com.example.mengfei.todo.R;

/**
 * Created by mengfei on 2017/5/17.
 */

public class PassWdSetActivity extends BaseActivity {

    private EditText passEt, mibaoQEt, mibaoAet;
    private Button okBtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_set_pass);
        initView();
        initActionBar("设置密码", null, true);
    }

    private void initView() {
        passEt = (EditText) findViewById(R.id.et_input_pass);
        mibaoQEt = (EditText) findViewById(R.id.et_mibao_q);
        mibaoAet = (EditText) findViewById(R.id.et_mibao_a);
        okBtn = (Button) findViewById(R.id.btn_ok);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = passEt.getText().toString();
                String mibaoQ = mibaoQEt.getText().toString();
                String mibaoA = mibaoAet.getText().toString();

                if (TextUtils.isEmpty(pass)) {
                    showSnackbar(okBtn, "密码不能为空");
                } else {
                    if (TextUtils.isEmpty(mibaoQ)) {
                        showSnackbar(okBtn, "密保问题不能为空");
                    } else {
                        if (TextUtils.isEmpty(mibaoA)) {
                            showSnackbar(okBtn, "密保答案不能为空");
                        } else {
                            AppConfig config = AppConfig.getInstance(mContext);
                            config.setPassWord(pass);
                            config.setMibao(mibaoA);
                            config.setMibaoQ(mibaoQ);
                            config.setSwitchPass(true);
                            showToast("设置成功");
                            finish();
                        }
                    }
                }
            }
        });
    }


}
