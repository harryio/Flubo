package com.harryio.flubo.utils;

import android.content.Context;
import android.text.format.DateFormat;

import java.util.Date;

public class DateUtils {
    public static boolean isToday(long when) {
        return android.text.format.DateUtils.isToday(when);
    }

    public static boolean isTimeInPast(long time) {
        return time <= new Date().getTime();
    }

    public static String getDateString(Context context, long time) {
        boolean isToday = isToday(time);
        if (!isToday) {
            return DateFormat.getDateFormat(context).format(time);
        }

        return "Today";
    }

    public static String getTimeString(Context context, long time) {
        return DateFormat.getTimeFormat(context).format(time);
    }
}
