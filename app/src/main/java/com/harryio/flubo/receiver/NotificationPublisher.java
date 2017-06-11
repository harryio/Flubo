package com.harryio.flubo.receiver;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;

import com.harryio.flubo.data.ReminderDAO;
import com.harryio.flubo.model.Reminder;
import com.harryio.flubo.model.RepeatInterval;
import com.harryio.flubo.service.AlarmHelperService;
import com.harryio.flubo.utils.NotificationFactory;

import java.util.Calendar;

public class NotificationPublisher extends BroadcastReceiver {
    private static final String EXTRA_REMINDER_ID = "com.harryio.flubo.EXTRA_REMINDER_ID";

    public static Intent getIntent(Context context, long reminderId) {
        Intent intent = new Intent(context, NotificationPublisher.class);
        intent.putExtra(EXTRA_REMINDER_ID, reminderId);

        return intent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        long id = intent.getLongExtra(EXTRA_REMINDER_ID, -1L);
        Reminder reminder = ReminderDAO.findReminderById(context, id);
        if (reminder != null) {
            Notification notification = NotificationFactory
                    .getReminderNotification(context, reminder);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify((int) id, notification);

            if (reminder.getRepeatInterval() != RepeatInterval.ONE_TIME) {
                setNextAlarm(context, reminder);
            } else {
                reminder.setCompleted(true);
                ReminderDAO.update(context, reminder);
            }
        }
    }

    private void setNextAlarm(Context context, Reminder reminder) {
        RepeatInterval repeatInterval = reminder.getRepeatInterval();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(reminder.getRemindTime());

        switch (repeatInterval) {
            case DAILY:
                calendar.add(Calendar.DATE, 1);
                break;

            case WEEKLY:
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
                break;

            case MONTHLY:
                calendar.add(Calendar.MONTH, 1);
                break;

            case YEARLY:
                calendar.add(Calendar.YEAR, 1);
                break;

            default:
                throw new IllegalStateException("No interval defined for " + repeatInterval.name());
        }

        reminder.setRemindTime(calendar.getTimeInMillis());
        ReminderDAO.update(context, reminder);
        AlarmHelperService.startActionCreateAlarm(context, reminder.getId());
    }
}
