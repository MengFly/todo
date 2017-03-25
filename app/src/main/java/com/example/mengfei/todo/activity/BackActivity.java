package com.example.mengfei.todo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.mengfei.todo.AppConstant;
import com.example.mengfei.todo.R;
import com.example.todolib.utils.CheckUtils;
import com.example.todolib.utils.ShareTools;

/**
 * 应用反馈的Activity
 * Created by mengfei on 2017/3/18.
 */
public class BackActivity extends BaseActivity {

    private RadioGroup backTypeRg;
    private EditText backBodyEt;
    private Button commitBtn;

    private String backType = "程序错误";

    private static final int REQUEST_CODE = 0x8484;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_back);
        initActionBar("应用反馈", null, true);
        initView();
        initListener();
    }

    private void initListener() {
        backTypeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_code_error) {
                    backType = "程序错误";
                } else {
                    backType = "软件建议";
                }
            }
        });
        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack();
            }
        });
    }

    private void callBack() {
        String backBody = backBodyEt.getText().toString();
        if (!TextUtils.isEmpty(backBody)) {
            Intent intent = ShareTools.getSendEmailIntent(AppConstant.BACK_EMAIL, backType, backBody);
            if (isUsedIntentActivity(intent)) {
                startActivityForResult(Intent.createChooser(intent, "选择邮件应用进行反馈"), REQUEST_CODE);
            } else {
                showSnackbar(toolbar, "您的手机没有邮箱应用，暂时没有办法实现反馈");
            }
        } else {
            showSnackbar(toolbar, "反馈内容不能为空");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                showToast("反馈成功，感谢您的反馈，相信由您的反馈我们的应用汇越来越好");
            } else {
                showToast("反馈取消");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initView() {
        backTypeRg = (RadioGroup) findViewById(R.id.rg_back_type);
        backBodyEt = (EditText) findViewById(R.id.et_back_body);
        commitBtn = (Button) findViewById(R.id.btn_commit);
    }
}
