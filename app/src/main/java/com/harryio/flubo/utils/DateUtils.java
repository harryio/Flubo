package com.harryio.flubo.utils;

public class DateUtils {
    public static boolean isToday(long when) {
        return android.text.format.DateUtils.isToday(when);
    }
}
