package com.example.mengfei.todo.adapter;

/**
 * Created by mengfei on 2017/10/13.
 */

public class ShowModel {
    public static final int TYPE_TITLE = 1;
    public static final int TYPE_CONTENT = 2;
    private int type;
    private String showText;

    public ShowModel(int type, String showText) {
        this.type = type;
        this.showText = showText;
    }

    public int getType() {
        return type;
    }

    public String getShowText() {
        return showText;
    }
}
