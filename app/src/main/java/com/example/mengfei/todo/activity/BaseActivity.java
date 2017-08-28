package com.example.mengfei.todo.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aitangba.swipeback.SwipeBackActivity;
import com.example.mengfei.todo.R;
import com.example.mengfei.todo.activity.inter.UiShower;
import com.example.mengfei.todo.utils.dialog.MyDialog;

import java.util.List;


/**
 * 所有Activity的基类
 * Created by mengfei on 2017/3/14.
 */
public class BaseActivity extends SwipeBackActivity {

    protected Toast mToast;
    protected BaseActivity mContext;
    protected Toolbar toolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        initToolBar();
    }

    //初始化Toolbar
    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    protected View getEmptyView(String tip) {
        View view = findViewById(R.id.ly_empty);
        if (tip != null)
            ((TextView) findViewById(R.id.tv_empty)).setText(tip);
        return view;
    }

    public Snackbar showSnackbar(View view, CharSequence text) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
        snackbar.show();
        return snackbar;
    }

    public void showMessageDialog(int icon, String title, String message, View.OnClickListener ok, View.OnClickListener cancel) {
        MyDialog dialog = new MyDialog(mContext);
        dialog.setIcon(icon);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setOkListener(ok);
        dialog.setCancelListener(cancel);
        dialog.show();
    }

    public void initActionBar(String actionTitle, String actionBarSubTitle, boolean isBack) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(actionTitle);
        if (actionBarSubTitle != null) {
            actionBar.setSubtitle(actionBarSubTitle);
        }
        actionBar.setDisplayHomeAsUpEnabled(isBack);
        actionBar.setDisplayShowHomeEnabled(isBack);
    }

    public void showInputKeyBord() {
        InputMethodManager keyBordManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        keyBordManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }


    /**
     * 显示一个Toast
     *
     * @param toastStr Toast要显示的字符串
     */
    public void showToast(String toastStr) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, toastStr, Toast.LENGTH_LONG);
        } else {
            mToast.setText(toastStr);
        }
        mToast.show();
    }


    /**
     * 返回目标的Intent时候存在支持的Activity
     *
     * @param intent 目标的Intent
     * @return 是否存在可以前往的Activity或者是Service
     */
    public boolean isUsedIntentActivity(Intent intent) {
        PackageManager manager = getPackageManager();
        List<ResolveInfo> list = manager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 进入其他的ctivity
     *
     * @param actClass class
     * @param isFinish 进入其他的Activity后时候销毁当前的Activity
     */
    public void openOtherActivity(Class<? extends Activity> actClass, boolean isFinish) {
        Intent intent = new Intent(mContext, actClass);
        startActivity(intent);
        if (isFinish) {
            mContext.finish();
        }
    }


}
