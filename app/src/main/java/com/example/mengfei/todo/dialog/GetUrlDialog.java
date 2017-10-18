package com.example.mengfei.todo.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.TodoApplication;
import com.example.mengfei.todo.activity.inter.UiShower;
import com.example.mengfei.todo.utils.SearchUtils;

public class GetUrlDialog extends BaseDialog implements View.OnClickListener {
    private UiShower<URLBean> beanUiShower;
    private EditText titleEt;
    private EditText urlEt;
    private TextInputLayout urlTIL;

    private LinearLayout searchLL;
    private TextView searchBaiduTv, searchSougouTv, searchBiYingTv, searchGithubTv;

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
                    searchLL.setVisibility(View.GONE);
                } else {
                    urlTIL.setErrorEnabled(true);
                    urlTIL.setError("网络格式格式不正确");
                    changeOkBtnStat(false);
                    if (!TextUtils.isEmpty(s)) {
                        searchLL.setVisibility(View.VISIBLE);
                    } else {
                        searchLL.setVisibility(View.GONE);
                    }
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
                bean.icon = SearchUtils.getSearchDrawable(bean.address);
                beanUiShower.show(bean);
            }
        });
        setCancelListener(null);
        searchBaiduTv.setOnClickListener(this);
        searchSougouTv.setOnClickListener(this);
        searchBiYingTv.setOnClickListener(this);
        searchGithubTv.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.layout_dialog_get_url);
        titleEt = (EditText) findViewById(R.id.et_title);
        urlTIL = (TextInputLayout) findViewById(R.id.til_url_input);
        urlEt = urlTIL.getEditText();

        //initSearchView
        searchLL = (LinearLayout) findViewById(R.id.ll_search);
        searchBaiduTv = (TextView) findViewById(R.id.tv_search_baidu);
        searchSougouTv = (TextView) findViewById(R.id.tv_search_sougou);
        searchBiYingTv = (TextView) findViewById(R.id.tv_search_biying);
        searchGithubTv = (TextView) findViewById(R.id.tv_search_github);
    }

    @Override
    public void onClick(View v) {
        String searchString = urlEt.getText().toString();
        switch (v.getId()) {
            case R.id.tv_search_baidu:
                urlEt.setText(SearchUtils.getSearchURL(SearchUtils.TYPE_BAIDU, searchString));
                break;
            case R.id.tv_search_sougou:
                urlEt.setText(SearchUtils.getSearchURL(SearchUtils.TYPE_SOUGOU, searchString));
                break;
            case R.id.tv_search_biying:
                urlEt.setText(SearchUtils.getSearchURL(SearchUtils.TYPE_BIYING, searchString));
                break;
            case R.id.tv_search_github:
                urlEt.setText(SearchUtils.getSearchURL(SearchUtils.TYPE_GITHUB, searchString));
                break;
        }
        if (TextUtils.isEmpty(titleEt.getText().toString())) {
            titleEt.setText(searchString);
        }
    }

    public static class URLBean {
        public Drawable icon;
        public String title;
        public String address;
    }
}
