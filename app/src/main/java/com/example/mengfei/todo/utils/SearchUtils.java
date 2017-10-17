package com.example.mengfei.todo.utils;

import android.graphics.drawable.Drawable;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.TodoApplication;

/**
 * Created by mengfei on 2017/10/17.
 */

public class SearchUtils {

    public static final String TYPE_BAIDU = "http://www.baidu.com/s?wd=";
    public static final String TYPE_SOUGOU = "https://www.sogou.com/sogou?query=";
    public static final String TYPE_GITHUB = "https://github.com/search?q=";
    public static final String TYPE_BIYING = "http://cn.bing.com/search?q=";

    public static String getSearchURL(String type, String searchStr) {
        return type + searchStr;
    }

    public static Drawable getSearchDrawable(String url) {
        if (url.startsWith(TYPE_BAIDU)) {
            return TodoApplication.getContext().getResources().getDrawable(R.drawable.ic_search_baidu);
        } else if (url.startsWith(TYPE_SOUGOU)) {
            return TodoApplication.getContext().getResources().getDrawable(R.drawable.ic_search_sougou);
        } else if (url.startsWith(TYPE_BIYING)) {
            return TodoApplication.getContext().getResources().getDrawable(R.drawable.ic_search_biying);
        } else if (url.startsWith(TYPE_GITHUB)) {
            return TodoApplication.getContext().getResources().getDrawable(R.drawable.ic_search_github);
        } else {
            return TodoApplication.getContext().getResources().getDrawable(R.drawable.ic_url);
        }
    }
}
