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
 * 设置密保的界面
 * Created by mengfei on 2017/5/17.
 */
public class PassWordSetMibaoActivity extends BaseActivity {

    private EditText mibaoQEt, mibaoAEt;
    private Button okBtn;

    private AppConfig config;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        config = AppConfig.getInstance(mContext);
        setContentView(R.layout.layout_activity_set_pass_qa);
        mibaoQEt = (EditText) findViewById(R.id.et_mibao_q);
        mibaoQEt.setText(AppConfig.getInstance(mContext).getMibaoQ());
        mibaoAEt = (EditText) findViewById(R.id.et_mibao_a);
        okBtn = (Button) findViewById(R.id.btn_ok);
        initListener();
    }

    private void initListener() {
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mibaoQ = mibaoQEt.getText().toString();
                String mibaoA = mibaoAEt.getText().toString();
                if (TextUtils.isEmpty(mibaoQ)) {
                    showSnackbar(okBtn, "密保问题不能为空");
                } else {
                    if (TextUtils.isEmpty(mibaoA)) {
                        showSnackbar(okBtn, "密保答案不能为空");
                    } else {
                        config.setMibaoQ(mibaoQ);
                        config.setMibao(mibaoA);
                        showToast("设置成功");
                        finish();
                    }
                }

            }
        });
    }
}
