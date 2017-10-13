package com.example.mengfei.todo.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.activity.inter.UiShower;
import com.example.mengfei.todo.adapter.ShowModel;
import com.example.mengfei.todo.adapter.TitleAndListAdapter;
import com.example.todolib.utils.CheckUtils;

import java.util.ArrayList;
import java.util.List;

public class GetEmailDialog extends BaseDialog {
    private UiShower<Email> uiShower;
    private String[] emailEnds = new String[]{
            "@gmail.com", "@qq.com", "@163.com", "@126.com", "@hotmail.com", "@sina.com", "@163.net", "@outlook.com"
    };

    private EditText setEmailAddressET, setEmailContentET;

    private RecyclerView emailRV;
    private List<ShowModel> emails;
    private TitleAndListAdapter emailAdapter;

    public GetEmailDialog(@NonNull Context context, UiShower<Email> emailShower) {
        super(context);
        this.uiShower = emailShower;
        initDatas();
        initListener();
    }

    private void initDatas() {
        emails = new ArrayList<>();
        emailAdapter = new TitleAndListAdapter(getContext(), emails);
        emailRV.setAdapter(emailAdapter);
    }

    private void initListener() {
        setEmailAddressET.addTextChangedListener(new TextWatcher() {
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
                Email email = new Email();
                email.emailAddress = setEmailAddressET.getText().toString();
                email.emailContent = setEmailContentET.getText().toString();
                uiShower.show(email);
            }
        });
        emailAdapter.setOnItemClickListener(new TitleAndListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                setEmailAddressET.setText(emails.get(position).getShowText());
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
                    emails.add(new ShowModel(ShowModel.TYPE_CONTENT, s + emailEnd));
                }
            }
        }
        emailAdapter.notifyDataSetChanged();

    }

    @Override
    protected void initView() {
        setContentView(R.layout.layout_dialog_get_email);
        setEmailAddressET = (EditText) findViewById(R.id.et_input_email_address);
        setEmailContentET = (EditText) findViewById(R.id.et_input_content);
        emailRV = (RecyclerView) findViewById(R.id.rv_show_email);
        emailRV.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public class Email {
        public String emailAddress;
        public String emailContent;
    }
}
