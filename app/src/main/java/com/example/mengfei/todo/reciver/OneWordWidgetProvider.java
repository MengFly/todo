package com.example.mengfei.todo.reciver;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.activity.inter.UiShower;
import com.example.mengfei.todo.entity.DailySentence;
import com.example.mengfei.todo.entity.DailySentenceManager;

/**
 * 每日一句的Widget
 * Created by mengfei on 2017/8/30.
 */
public class OneWordWidgetProvider extends AppWidgetProvider {

    private static final String ACTION_RANDOM_STYLE = "com.mengfei.todo.randomStyle";
    private static final String ACTION_RANDOM_ONE_WORD = "com.mengfei.todo.randomOneWord";
    private int[] styleRes = new int[]{
            R.drawable.back_one_words, R.drawable.bg_dialog
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        switch (intent.getAction()) {
            case ACTION_RANDOM_ONE_WORD:
                int[] ids = intent.getIntArrayExtra("ids");
                onUpdate(context, AppWidgetManager.getInstance(context), ids);
                break;
            case ACTION_RANDOM_STYLE:
                int[] ids2 = intent.getIntArrayExtra("ids");
                onUpdateStyle(context, AppWidgetManager.getInstance(context), ids2);
                break;
        }
    }

    static int nowIndex = 0;

    private void onUpdateStyle(Context context, AppWidgetManager instance, int[] ids2) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget_one_word);
        if (nowIndex == 1) {
            remoteViews.setTextColor(R.id.tv_one_word_content, context.getResources().getColor(R.color.color_text_main_dark));
        } else {
            remoteViews.setTextColor(R.id.tv_one_word_content, context.getResources().getColor(R.color.color_text_main_light));
        }
        if (nowIndex == 2) {
            nowIndex = 0;
        }
        remoteViews.setImageViewResource(R.id.iv_bg, styleRes[nowIndex]);
        nowIndex++;
        instance.updateAppWidget(ids2, remoteViews);
    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        DailySentenceManager.getRandomOneWords(new UiShower<DailySentence>() {
            @Override
            public void show(DailySentence dailySentence) {
                final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget_one_word);
                remoteViews.setTextViewText(R.id.tv_one_word_content, dailySentence.getShowSpannableString());
                remoteViews.setOnClickPendingIntent(R.id.tv_other_style, getIntent(context, appWidgetIds, ACTION_RANDOM_STYLE, 0));
                remoteViews.setOnClickPendingIntent(R.id.tv_other_word, getIntent(context, appWidgetIds, ACTION_RANDOM_ONE_WORD, 1));
                appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
            }
        });
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }

    public PendingIntent getIntent(Context context, int[] appWidgetIds, String actionRandomStyle, int requestCode) {
        Intent intent = new Intent(actionRandomStyle);
        intent.putExtra("ids", appWidgetIds);
        return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}