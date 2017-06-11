package com.harryio.flubo.utils;

import android.app.Notification;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.harryio.flubo.R;
import com.harryio.flubo.model.Reminder;

public class NotificationFactory {
    public static Notification getReminderNotification(Context context, Reminder reminder) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setTicker(reminder.getTitle())
                .setWhen(reminder.getRemindTime())
                .setContentTitle(reminder.getTitle())
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true);

        String description = reminder.getDescription();
        if (!TextUtils.isEmpty(description)) {
            notificationBuilder.setContentText(description);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.ic_alarm_check_white);
            int color = ContextCompat.getColor(context, R.color.primary);
            notificationBuilder.setColor(color);
        } else {
            notificationBuilder.setSmallIcon(R.drawable.ic_alarm_check_black);
        }

        return notificationBuilder.build();
    }
}
