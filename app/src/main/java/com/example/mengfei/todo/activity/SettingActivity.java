package com.example.mengfei.todo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.mengfei.todo.AppConfig;
import com.example.mengfei.todo.AppConstant;
import com.example.mengfei.todo.R;

import static com.example.mengfei.todo.R.id.s_delete_task_tip;

/**
 * 手机设置
 * Created by mengfei on 2017/3/18.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private AppConfig config;

    private Switch deleteTipS;
    private Switch mainTooLateTipS;
//    private TextView clearCacheTv;
    private TextView helpTv;
    private TextView aboutAppTv;
    private TextView licenceTv;
    private TextView privateTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        config = AppConfig.getInstance(mContext);
        setContentView(R.layout.layout_activity_setting);
        initActionBar("设置", null, true);
        initView();
        initUI();
        initListener();
    }

    private void initUI() {
        deleteTipS.setChecked(config.isDeleteTipShow());
        mainTooLateTipS.setChecked(config.isTimeTooLateTipShow());
    }

    private void initListener() {
//        clearCacheTv.setOnClickListener(this);
        helpTv.setOnClickListener(this);
        aboutAppTv.setOnClickListener(this);
        licenceTv.setOnClickListener(this);
        privateTv.setOnClickListener(this);
        deleteTipS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                config.setDeleteTipShow(isChecked);
            }
        });
        mainTooLateTipS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                config.setTimeTooLateTipShow(isChecked);
            }
        });
    }

    private void initView() {
        deleteTipS = (Switch) findViewById(s_delete_task_tip);
        mainTooLateTipS = (Switch) findViewById(R.id.s_time_too_late_tip);
//        clearCacheTv = (TextView) findViewById(R.id.tv_clear_cache);
        helpTv = (TextView) findViewById(R.id.tv_help);
        aboutAppTv = (TextView) findViewById(R.id.tv_about_app);
        licenceTv = (TextView) findViewById(R.id.tv_license);
        privateTv = (TextView) findViewById(R.id.tv_private);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_help:
                WebActivity.StartWebActivityWithURL(mContext, AppConstant.APP_HELP_URL);
                break;
            case R.id.tv_about_app:
                WebActivity.StartWebActivityWithURL(mContext, AppConstant.ABOUT_APP_URL);
                break;
            case R.id.tv_license:
                WebActivity.StartWebActivityWithURL(mContext, AppConstant.APP_LICENSE_URL);
                break;
            case R.id.tv_private:
                WebActivity.StartWebActivityWithURL(mContext, AppConstant.APP_PRIVATE_URL);
                break;
        }

    }
}
