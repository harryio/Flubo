package com.harryio.flubo.utils;

import java.util.Date;

public class DateUtils {
    public static boolean isToday(long when) {
        return android.text.format.DateUtils.isToday(when);
    }

    public static boolean isTimeInPast(long time) {
        return time <= new Date().getTime();
    }
}
