package com.example.mengfei.todo.dialog;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.TodoApplication;
import com.example.mengfei.todo.activity.inter.UiShower;
import com.example.todolib.adapter.CommonAdapter;
import com.example.todolib.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取手机中的应用
 * Created by mengfei on 2017/8/29.
 */
public class GetAppDialog extends BaseDialog {
    private CommonAdapter<AppBean> adapter;
    private UiShower<AppBean> getAppBeanListener;

    public GetAppDialog(@NonNull Context context, UiShower<AppBean> getAppBeanListener) {
        super(context);
        this.getAppBeanListener = getAppBeanListener;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.layout_dialog_get_contacts);
        ((TextView) findViewById(R.id.tv_title)).setText("选择应用");
        ListView appsList = (ListView) findViewById(R.id.lv_contacts_list);
        findViewById(R.id.ll_dialog_btn).setVisibility(View.GONE);
        adapter = new CommonAdapter<AppBean>(getContext(), getAppBean(), R.layout.layout_item_contact) {
            @Override
            public void bindItemDatas(ViewHolder holder, AppBean bean) {
                ((ImageView) holder.getView(R.id.iv_icon)).setImageDrawable(bean.icon);
                ((TextView) holder.getView(R.id.tv_name)).setText(bean.name);
                ((TextView) holder.getView(R.id.tv_number)).setText(bean.packageName);
            }
        };
        appsList.setAdapter(adapter);
        appsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getAppBeanListener.show(adapter.getItem(position));
                dismiss();
            }
        });
    }


    private List<AppBean> getAppBean() {
        List<AppBean> beans = new ArrayList<>();
        PackageManager packageManager = getContext().getPackageManager();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
        for (PackageInfo installedPackage : installedPackages) {
            Intent startIntent = packageManager.getLaunchIntentForPackage(installedPackage.packageName);
            if (startIntent != null) {
                AppBean bean = new AppBean();
                bean.name = getApplicationName(installedPackage.packageName, packageManager);
                bean.packageName = installedPackage.packageName;
                bean.icon = installedPackage.applicationInfo.loadIcon(packageManager);
                beans.add(bean);
            }
        }
        return beans;
    }

    public static Intent getStartIntent(String packageName) {
        return TodoApplication.getContext().getPackageManager().getLaunchIntentForPackage(packageName);
    }

    /**
     * 获得应用名称
     *
     * @return 应用的名称
     */
    private static String getApplicationName(String packName, PackageManager packageManager) {
        String applicationName = null;
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packName, 0);
            applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return applicationName;
    }

    public class AppBean {
        public String name;
        public String packageName;
        public Drawable icon;

    }
}
