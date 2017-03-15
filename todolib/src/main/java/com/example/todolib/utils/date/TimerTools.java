package com.example.todolib.utils.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * 提供一些有关时间的方法
 * Created by mengfei on 2017/1/1.
 */

public class TimerTools {

    /**
     * 用于检测时间是否已经太晚了
     */
    public boolean timeIsTooLate() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.HOUR_OF_DAY) >= 23 || now.get(Calendar.HOUR_OF_DAY) <= 4;
    }

    public String getDayFromOnlyDayStr(String onlyDayStr) {
        DateFormat format = DateFormat.getDateInstance();
        try {
            Date d = format.parse(onlyDayStr);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            int day = c.get(Calendar.DAY_OF_MONTH);
            if (day < 10) {
                return "0" + day;
            } else {
                return String.valueOf(day);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "DO";
        }
    }

}
