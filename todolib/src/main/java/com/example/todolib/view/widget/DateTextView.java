package com.example.todolib.view.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

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
public class DateTextView extends AppCompatTextView {

    private Date mDate;
    private int showTypeCount = 2;//这个变量标示在显示天数的时候可以显示几种类型,默认是显示两种类型
    //只显示日期
    public static final String SHOW_MODE_ONLY_DATE = "only_date";
    //只显示距离多久
    public static final String SHOW_MODE_ONLY_HOW_LONG = "how_long";
    //所有的都显示
    public static final String SHOW_MODE_ALL = "all";

    private String showMode = SHOW_MODE_ALL;

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

    public void setShowMode(String showmode) {
        this.showMode = showmode;
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

    public Date getDate() {
        return mDate;
    }

    private void updateShowText(Date date) {
        setText(getShowText(date));
    }

    private SpannableString getShowText(Date date) {
        if (date == null) {
            return null;
        }
        if (showMode.equals(SHOW_MODE_ONLY_DATE)) {
            return new SpannableString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(date));
        }
        int showTypeCount = 0;
        StringBuilder sb = new StringBuilder("(");
        Calendar now = Calendar.getInstance();
        Calendar simpleDate = Calendar.getInstance();
        simpleDate.setTime(date);
        int year = now.get(Calendar.YEAR) - simpleDate.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) - simpleDate.get(Calendar.MONTH);
        int day = now.get(Calendar.DAY_OF_MONTH) - simpleDate.get(Calendar.DAY_OF_MONTH);
        int hour = now.get(Calendar.HOUR_OF_DAY) - simpleDate.get(Calendar.HOUR_OF_DAY);
        int min = now.get(Calendar.MINUTE) - simpleDate.get(Calendar.MINUTE);
        if (min < 0 && hour > 0) {
            min += 60;
            hour--;
        }
        if (hour < 0 && day > 0) {
            hour += 24;
            day--;
        }
        if (day < 0 && month > 0) {
            day += now.getMaximum(Calendar.DAY_OF_MONTH);
            month--;
        }
        if (month < 0 && year > 0) {
            month += 12;
            year--;
        }
        if (year > 0) {
            showTypeCount++;
            sb.append(year).append("年");
        }
        if (month > 0 && showTypeCount < this.showTypeCount) {
            showTypeCount++;
            sb.append(month).append("月");
        }
        if (day > 0 && showTypeCount < this.showTypeCount) {
            showTypeCount++;
            sb.append(day).append("天");
        }
        if (hour > 0 && showTypeCount < this.showTypeCount) {
            showTypeCount++;
            sb.append(hour).append("小时");
        }
        if (min > 0 && showTypeCount < this.showTypeCount) {
            sb.append(min).append("分钟");
            showTypeCount ++;
        }
        if (showTypeCount ==0){
            sb.append("刚刚");
        } else {
            sb.append("前");
        }
        sb.append(")");
        if (showMode.equals(SHOW_MODE_ONLY_HOW_LONG)) {
            SpannableString showString = new SpannableString(sb.toString());
            showString.setSpan(new ForegroundColorSpan(Color.parseColor("#FFCA8619")), 0, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return showString;
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            sb.append(" ").append(format.format(date));
            SpannableString showString = new SpannableString(sb.toString());
            showString.setSpan(new ForegroundColorSpan(Color.parseColor("#FFCA8619")), 0, sb.indexOf(")") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return showString;
        }
    }


}
