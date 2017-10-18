package com.example.mengfei.todo.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by mengfei on 2017/10/18.
 */

public class MyListView extends ListView {
    private boolean isDisParentScroll = false;

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }




}
