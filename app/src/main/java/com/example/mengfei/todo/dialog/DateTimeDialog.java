package com.example.mengfei.todo.dialog;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.activity.inter.UiShower;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeDialog extends BaseDialog {

    private TextView showDateView;
    private UiShower<Date> shower;

    private NumberPicker yearMouthAndDayNP;
    private NumberPicker hourNP;
    private NumberPicker mintsNp;

    private Calendar beginDate;
    private Calendar showDate ;

    @Override
    protected void initView() {
        setContentView(R.layout.layout_dialog_date_time);
        setTitle("设置提醒时间");
        yearMouthAndDayNP = (NumberPicker) findViewById(R.id.np_year_month_day);
        hourNP = (NumberPicker) findViewById(R.id.np_hour);
        mintsNp = (NumberPicker) findViewById(R.id.np_mints);
        showDateView = (TextView) findViewById(R.id.tv_show_date_time);

    }

    public DateTimeDialog(Context context, Date beginDate, UiShower<Date> shower) {
        super(context);
        this.shower = shower;
        initDatas();
        this.beginDate.setTime(beginDate);
    }
    private void initDatas() {
        beginDate = Calendar.getInstance();
        showDate = Calendar.getInstance();
        showDate.setTime(beginDate.getTime());
        mintsNp.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        hourNP.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        yearMouthAndDayNP.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        yearMouthAndDayNP.setClickable(false);
        hourNP.setMinValue(0);
        hourNP.setMaxValue(23);
        hourNP.setValue(beginDate.get(Calendar.HOUR_OF_DAY));
        hourNP.setWrapSelectorWheel(false);

        mintsNp.setMinValue(0);
        mintsNp.setMaxValue(59);
        mintsNp.setValue(beginDate.get(Calendar.MINUTE));
        mintsNp.setWrapSelectorWheel(false);

        String[] sub = getDisplayValues();
        yearMouthAndDayNP.setDisplayedValues(sub);
        yearMouthAndDayNP.setMinValue(0);
        yearMouthAndDayNP.setMaxValue(sub.length - 1);
//        yearMouthAndDayNP.setFormatter(formatter);
        yearMouthAndDayNP.setValue(0);
        yearMouthAndDayNP.setWrapSelectorWheel(false);
        initListener();
        updateShowText();
    }

    private void initListener() {
        yearMouthAndDayNP.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                updateYearMouthDayText(newVal);
            }
        });
        hourNP.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                updateHourText(newVal);
            }
        });
        mintsNp.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                updateMintsText(newVal);
            }
        });
        setOkListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showDate.before(Calendar.getInstance())) {
                    Snackbar.make(showDateView, "设置的时间不能在当前系统时间之前", Snackbar.LENGTH_LONG).show();
                } else {
                    shower.show(showDate.getTime());
                    dismiss();
                }
            }
        });
        setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDatas();
            }
        }, false, "重置时间");
    }


    private void updateYearMouthDayText(int showP) {
        showDate.set(Calendar.YEAR, beginDate.get(Calendar.YEAR));
        showDate.set(Calendar.MONTH, beginDate.get(Calendar.MONTH));
        showDate.set(Calendar.DAY_OF_MONTH, beginDate.get(Calendar.DAY_OF_MONTH) + showP);
        updateShowText();
    }

    private void updateHourText(int show) {
        showDate.set(Calendar.HOUR_OF_DAY, show);
        updateShowText();
    }

    private void updateMintsText(int show) {
        showDate.set(Calendar.MINUTE, show);
        updateShowText();
    }

    private void updateShowText() {
        String timeStr = SimpleDateFormat.getDateTimeInstance().format(showDate.getTime());
        showDateView.setText(timeStr);
    }


    private String[] getDisplayValues() {
        String[] array = new String[100];
        for (int i = 0; i < 100; i++) {
            if (i == 1) {
                array[i] = "明天";
            } else if (i == 0) {
                array[i] = "今天";
            } else if (i == 2) {
                array[i] = "后天";
            } else {
                Calendar show = Calendar.getInstance();
                show.setTime(beginDate.getTime());
                show.set(Calendar.DAY_OF_MONTH, i + beginDate.get(Calendar.DAY_OF_MONTH));
                String dateStr = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA).format(show.getTime());
                array[i] = dateStr.substring(2, dateStr.length());
            }
        }
        return array;
    }
}