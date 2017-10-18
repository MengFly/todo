package com.example.mengfei.todo.dialog;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
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
import com.example.todolib.utils.ChineseSpelling;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取手机中的应用
 * Created by mengfei on 2017/8/29.
 */
public class GetAppDialog extends BaseDialog {
    private CommonAdapter<AppBean> adapter;
    private UiShower<AppBean> getAppBeanListener;
    private SearchView searchAppSV;

    public GetAppDialog(@NonNull Context context, UiShower<AppBean> getAppBeanListener) {
        super(context);
        this.getAppBeanListener = getAppBeanListener;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.layout_dialog_get_app);
        final ListView appsList = (ListView) findViewById(R.id.lv_contacts_list);
        searchAppSV = (SearchView) findViewById(R.id.sv_search_app);
        adapter = new CommonAdapter<AppBean>(getContext(), getAppBean(), R.layout.layout_item_contact) {
            @Override
            public void bindItemDatas(ViewHolder holder, AppBean bean) {
                ((ImageView) holder.getView(R.id.iv_icon)).setImageDrawable(bean.icon);
                ((TextView) holder.getView(R.id.tv_name)).setText(bean.name);
                ((TextView) holder.getView(R.id.tv_number)).setText(bean.packageName);
            }

            @Override
            public boolean isFilter(CharSequence str, AppBean appBean) {
                return appBean.name.contains(str)
                        || appBean.packageName.contains(str)
                        || ChineseSpelling.getInstance().getSelling(appBean.name).contains(str);
            }
        };
        appsList.setTextFilterEnabled(true);
        appsList.setAdapter(adapter);
        appsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getAppBeanListener.show(adapter.getItem(position));
                dismiss();
            }
        });
        searchAppSV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String searchText = newText.trim();
                if (TextUtils.isEmpty(searchText)) {
                    appsList.clearTextFilter();
                } else {
                    adapter.getFilter().filter(searchText);
                }
                return true;
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
