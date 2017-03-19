package com.example.mengfei.todo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.example.mengfei.todo.R;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * 用于显示网页信息的Activity
 * Created by mengfei on 2017/3/18.
 */
public class WebActivity extends BaseActivity {

    private static final String INTENT_KEY_LOAD_WEB_URL = "url";
    private WebView webView;
    private String loadUrl;
    private FrameLayout webLayout;
    private ProgressBar showPressBar;
    private Toolbar toolBar;

    public static void StartWebActivityWithURL(Context context, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(INTENT_KEY_LOAD_WEB_URL, url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariable();
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.layout_activity_web);
        showPressBar = (ProgressBar) findViewById(R.id.progressBar);
        webLayout = (FrameLayout) findViewById(R.id.fl_web_parent);
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        initWebView();
        initUI();
        initActionBar(webView.getTitle(), null, true);
    }

    private void initUI() {
        webView.loadUrl(loadUrl);
    }

    private void initVariable() {
        Intent intent = getIntent();
        if (intent != null) {
            loadUrl = intent.getStringExtra(INTENT_KEY_LOAD_WEB_URL);
        }
    }

    private void initWebView() {
        webView = new WebView(mContext.getApplication());
        webView.setDayOrNight(true);
        webView.setWebViewClient(new MyWebClient());
        webView.setWebChromeClient(new MyWebChromeClient());
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webLayout.addView(webView);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onDestroy() {
        webView.destroy();
        webView = null;
        super.onDestroy();
    }

    private class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView webView, int i) {
            super.onProgressChanged(webView, i);
            if (i != showPressBar.getMax()) {
                if (showPressBar.getVisibility() == View.VISIBLE) {
                    showPressBar.setProgress(i);
                } else {
                    showPressBar.setVisibility(View.VISIBLE);
                }
            } else {
                toolBar.setTitle(webView.getTitle());
                showPressBar.setVisibility(View.GONE);
            }
        }
    }

    private class MyWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String s) {
            super.shouldOverrideUrlLoading(webView, s);
            webView.loadUrl(s);
            return true;
        }

    }
}
