package com.example.mengfei.todo.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.mengfei.todo.R;
import com.example.mengfei.todo.activity.inter.UiShower;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mengfei on 2017/3/19.
 */
public class DateTimeDialog extends Dialog {

    private TextView showDateView;
    private UiShower<Date> shower;

    private NumberPicker yearMouthAndDayNP;
    private NumberPicker hourNP;
    private NumberPicker mintsNp;

    private Button resetBtn;
    private Button okBtn;

    private Calendar beginDate = Calendar.getInstance();
    private Calendar showDate = Calendar.getInstance();


    public DateTimeDialog(Context context, Date beginDate, UiShower<Date> shower) {
        this(context, R.style.MyDialogStyle, beginDate, shower);
    }

    public DateTimeDialog(Context context, int themeResId, Date beginDate, UiShower<Date> shower) {
        super(context, themeResId);
        this.beginDate.setTime(beginDate);
        this.shower = shower;
        initView();
    }

    private void initView() {
        setContentView(R.layout.layout_dialog_date_time);
        setTitle("设置提醒时间");
        yearMouthAndDayNP = (NumberPicker) findViewById(R.id.np_year_month_day);
        hourNP = (NumberPicker) findViewById(R.id.np_hour);
        mintsNp = (NumberPicker) findViewById(R.id.np_mints);
        showDateView = (TextView) findViewById(R.id.tv_show_date_time);
        resetBtn = (Button) findViewById(R.id.btn_reset);
        okBtn = (Button) findViewById(R.id.btn_ok);
        initDatas();
    }

    private void initDatas() {
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

        yearMouthAndDayNP.setMinValue(0);
        yearMouthAndDayNP.setMaxValue(100);
        yearMouthAndDayNP.setValue(0);
        yearMouthAndDayNP.setFormatter(formatter);
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
        okBtn.setOnClickListener(new View.OnClickListener() {
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

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDatas();
            }
        });
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

    private NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
        @Override
        public String format(int value) {
            if (value == 0) {
                return "今天";
            } else if (value == 1) {
                return "明天";
            } else if (value == 2) {
                return "后天";
            } else {
                Calendar show = Calendar.getInstance();
                show.setTime(beginDate.getTime());
                show.set(Calendar.DAY_OF_MONTH, value + beginDate.get(Calendar.DAY_OF_MONTH));
                String dateStr = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA).format(show.getTime());
                return dateStr.substring(2, dateStr.length());
            }
        }
    };
}
