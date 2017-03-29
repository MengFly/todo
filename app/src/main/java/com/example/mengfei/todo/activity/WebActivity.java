package com.example.mengfei.todo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
        initWebView();
        initUI();
        initActionBar("加载中...", null, true);
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

    @Override
    public boolean supportSlideBack() {
        return false;
    }

    private void initWebView() {
        webView = new WebView(mContext.getApplication());
        webView.setDayOrNight(true);
        webView.setWebViewClient(new MyWebClient());
        webView.setWebChromeClient(new MyWebChromeClient());
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
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
                    toolbar.setTitle("加载中...");
                    showPressBar.setVisibility(View.VISIBLE);
                }
            } else {
                toolbar.setTitle(webView.getTitle());
                showPressBar.setVisibility(View.GONE);
            }
        }
    }

    private class MyWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String s) {
            webView.loadUrl(s);
            return true;
        }

    }
}
