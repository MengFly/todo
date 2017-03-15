package com.example.todolib.view.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 用于显示Date的TextView
 * 显示的格式是(N天前)yyyy年mm月rr日
 * Created by mengfei on 2017/3/7.
 */
public class DateTextView extends TextView {

    private Date mDate;

    public DateTextView(Context context) {
        this(context, null, 0);
    }

    public DateTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DateTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createUpdateThread();
    }

    private void createUpdateThread() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(getContext().getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (mDate != null) {
                            updateShowText(mDate);
                        }
                    }
                });
            }
        }, 60 * 1000);
    }


    public void setDate(Date date) {
        this.mDate = date;
        updateShowText(date);
    }

    private void updateShowText(Date date) {
        setText(getShowText(date));
    }

    private SpannableString getShowText(Date date) {
        StringBuilder sb = new StringBuilder("(");
        Calendar now = Calendar.getInstance();
        Calendar simpleDate = Calendar.getInstance();
        simpleDate.setTime(date);
        int year = now.get(Calendar.YEAR) - simpleDate.get(Calendar.YEAR);
        int month= now.get(Calendar.MONTH) - simpleDate.get(Calendar.MONTH);
        int day = now.get(Calendar.DAY_OF_MONTH) - simpleDate.get(Calendar.DAY_OF_MONTH);
        int hour= now.get(Calendar.HOUR_OF_DAY) - simpleDate.get(Calendar.HOUR_OF_DAY);
        int min = now.get(Calendar.MINUTE) - simpleDate.get(Calendar.MINUTE);
        if (min < 0) {
            min += 60;
            hour --;
        }
        if (hour < 0) {
            day --;
        }
        if (day < 0) {
            month --;
        }
        if (month < 0) {
            year--;
        }
        if (year > 0) {
            sb.append(year + "年前");
        } else if (month > 0) {
            sb.append(month + "月前");
        } else  if (day > 0) {
            sb.append(day + "天前");
        } else if (hour > 0) {
            sb.append(hour + "小时前");
        } else if (min > 0) {
            sb.append(min + "分钟前");
        } else {
            sb.append("刚刚");
        }
        sb.append(")");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        sb.append(" ").append(format.format(date));
        SpannableString showString = new SpannableString(sb.toString());
        showString.setSpan(new ForegroundColorSpan(Color.parseColor("#FFCA8619")), 0, sb.indexOf(")") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return showString;

    }


}
