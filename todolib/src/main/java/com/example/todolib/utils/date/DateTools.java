package com.example.todolib.utils.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * 用于处理日期对象的工具类
 * Created by mengfei on 2016/12/23.
 */

public class DateTools {

    //对日期对象进行字符串格式输出，带有日期和时间
    public static String formatDate(Date d) {
        DateFormat format = DateFormat.getDateTimeInstance();
        return format.format(d);
    }

    //根据字符串获取相应的时间
    public static Date getDate(String formatStr) {
        DateFormat format = DateFormat.getDateTimeInstance();
        try {
            return format.parse(formatStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    //对日期对象进行字符串格式输出，只带有日期
    public static String formatDateOnly(Date d) {
        DateFormat format = DateFormat.getDateInstance();
        return format.format(d);
    }
}
