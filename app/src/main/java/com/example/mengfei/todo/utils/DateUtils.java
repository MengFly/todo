package com.example.mengfei.todo.utils;

import java.util.Calendar;

/**
 * Created by mengfei on 2017/3/16.
 */

public class DateUtils {

    private static final  int TIME_TOO_LATE_BEGIN = 0;
    private static final int TIME_TOO_LATE_END = 4;

    /**
     * 判断时间是否是已经够晚了，判断的依据是时间在晚上12点到四点之间都会进行提醒
     */
    public static boolean isTooLate() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.HOUR_OF_DAY) >= TIME_TOO_LATE_BEGIN && now.get(Calendar.HOUR_OF_DAY) <=TIME_TOO_LATE_END;
    }
}
