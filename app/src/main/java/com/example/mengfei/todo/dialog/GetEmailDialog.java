package com.example.mengfei.todo.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.activity.inter.UiShower;
import com.example.todolib.utils.CheckUtils;

import java.util.ArrayList;
import java.util.List;

public class GetEmailDialog extends BaseDialog {
    private UiShower<String> uiShower;
    private String[] emailEnds = new String[]{
            "@gmail.com", "@qq.com", "@163.com", "@126.com", "@hotmail.com", "@sina.com", "@163.net", "@outlook.com"
    };


    private AutoCompleteTextView setEmailACTV;
    private List<String> emails;
    private ArrayAdapter<String> emailAdapter;

    public GetEmailDialog(@NonNull Context context, UiShower<String> emailShower) {
        super(context, R.style.ScreenDialog);
        this.uiShower = emailShower;
        initListener();
        initDatas();
    }

    private void initDatas() {
        emails = new ArrayList<>();
        setEmailACTV.setThreshold(2);
        emailAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, emails);
        setEmailACTV.setAdapter(emailAdapter);
    }

    private void initListener() {
        setEmailACTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (CheckUtils.isEmail(s)) {
                    changeOkBtnStat(true);
                } else {
                    changeOkBtnStat(false);
                }
                updateExtra(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        setCancelListener(null);
        setOkListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uiShower.show(setEmailACTV.getText().toString());
            }
        });
    }

    private void updateExtra(CharSequence s) {
        emails.clear();
        if (TextUtils.isEmpty(s)) {
            // TODO: 2017/10/8 在这里添加获取历史Email的逻辑
        } else {
            if (!s.toString().contains("@")) {
                for (String emailEnd : emailEnds) {
                    emails.add(s + emailEnd);
                }
            }
        }
        emailAdapter.notifyDataSetChanged();

    }

    @Override
    protected void initView() {
        setContentView(R.layout.layout_dialog_get_email);
        setEmailACTV = (AutoCompleteTextView) findViewById(R.id.mactv_input_email);
    }
}
