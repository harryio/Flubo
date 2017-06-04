package com.harryio.flubo.receiver;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;

import com.harryio.flubo.data.ReminderDAO;
import com.harryio.flubo.model.Reminder;
import com.harryio.flubo.utils.NotificationFactory;

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
        Reminder reminder = ReminderDAO.query(context, id);
        if (reminder != null) {
            Notification notification = NotificationFactory
                    .getReminderNotification(context, reminder);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify((int) id, notification);
        }
    }
}
