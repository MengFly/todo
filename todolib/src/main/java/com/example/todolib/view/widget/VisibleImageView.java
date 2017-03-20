package com.example.todolib.view.widget;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by mengfei on 2017/3/20.
 */

public class VisibleImageView extends ImageView {

    private OnVisibilityChangedListener listener;

    public void setOnVisibilityChangedListener(OnVisibilityChangedListener listener) {
        this.listener = listener;
    }

    public VisibleImageView(Context context) {
        super(context);
    }

    public VisibleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VisibleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        listener.onVisibilityChangedListener(visibility);
        super.onWindowVisibilityChanged(visibility);
    }

    public interface OnVisibilityChangedListener {
        void onVisibilityChangedListener(int visibility);
    }
}
