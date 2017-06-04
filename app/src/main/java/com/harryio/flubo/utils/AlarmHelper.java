package com.harryio.flubo.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.harryio.flubo.model.Reminder;
import com.harryio.flubo.receiver.NotificationPublisher;

public class AlarmHelper {
    public void createAlarm(Context context, Reminder reminder) {
        Intent intent = NotificationPublisher.getIntent(context, reminder.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) reminder.getId(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminder.getRemindTime(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, reminder.getRemindTime(), pendingIntent);
        }
    }

    public void deleteAlarm(Context context, long id) {
        Intent intent = new Intent(context, NotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) id, intent,
                PendingIntent.FLAG_NO_CREATE);

        if (pendingIntent != null) {
            pendingIntent.cancel();
            ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).cancel(pendingIntent);
        }
    }
}
