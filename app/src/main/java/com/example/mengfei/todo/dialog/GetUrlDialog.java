package com.example.mengfei.todo.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.TodoApplication;
import com.example.mengfei.todo.activity.inter.UiShower;

public class GetUrlDialog extends BaseDialog {
    private UiShower<URLBean> beanUiShower;
    private EditText titleEt;
    private EditText urlEt;
    private TextInputLayout urlTIL;

    public GetUrlDialog(@NonNull Context context, UiShower<URLBean> beanUiShower) {
        super(context);
        this.beanUiShower = beanUiShower;
        initListener();
        changeOkBtnStat(false);
    }

    private void initListener() {
        urlEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (URLUtil.isNetworkUrl(s.toString())) {
                    urlTIL.setErrorEnabled(false);
                    changeOkBtnStat(true);
                } else {
                    urlTIL.setErrorEnabled(true);
                    urlTIL.setError("网络格式格式不正确");
                    changeOkBtnStat(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        setOkListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                URLBean bean = new URLBean();
                bean.address = urlEt.getText().toString();
                bean.title = titleEt.getText().toString();
                bean.icon = TodoApplication.getContext().getResources().getDrawable(R.drawable.ic_url);
                beanUiShower.show(bean);
            }
        });
        setCancelListener(null);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.layout_dialog_get_url);
        titleEt = (EditText) findViewById(R.id.et_title);
        urlTIL = (TextInputLayout) findViewById(R.id.til_url_input);
        urlEt = urlTIL.getEditText();
    }

    public static class URLBean {
        public Drawable icon;
        public String title;
        public String address;
    }
}
