package com.harryio.flubo.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.harryio.flubo.data.ReminderDAO;
import com.harryio.flubo.model.Reminder;
import com.harryio.flubo.receiver.NotificationPublisher;

import java.util.List;

public class AlarmHelperService extends IntentService {
    private static final String ACTION_PREFIX = "com.harryio.flubo.service.action.";
    private static final String ACTION_CREATE_ALARM = ACTION_PREFIX + "CREATE_ALARM";
    private static final String ACTION_DELETE_ALARM = ACTION_PREFIX + "DELETE_ALARM";
    private static final String ACTION_UPDATE_ALARM = ACTION_PREFIX + "UPDATE_ALARM";
    private static final String ACTION_CREATE_ALL_ALARMS = ACTION_PREFIX + "CREATE_ALL_ALARMS";
    private static final String ACTION_DELETE_ALL_ALARMS = ACTION_PREFIX + "DELETE_ALL_ALARMS";

    private static final String EXTRA_PREFIX = "com.harryio.flubo.service.extra.";
    private static final String EXTRA_REMINDER_ID = EXTRA_PREFIX + "REMINDER_ID";

    public AlarmHelperService() {
        super("AlarmHelperService");
    }


    public static void startActionCreateAlarm(Context context, long id) {
        Intent intent = new Intent(context, AlarmHelperService.class);
        intent.putExtra(EXTRA_REMINDER_ID, id);
        intent.setAction(ACTION_CREATE_ALARM);
        context.startService(intent);
    }

    public static void startActionDeleteAlarm(Context context, long id) {
        Intent intent = new Intent(context, AlarmHelperService.class);
        intent.putExtra(EXTRA_REMINDER_ID, id);
        intent.setAction(ACTION_DELETE_ALARM);
        context.startService(intent);
    }

    public static void startActionUpdateAlarm(Context context, long id) {
        Intent intent = new Intent(context, AlarmHelperService.class);
        intent.putExtra(EXTRA_REMINDER_ID, id);
        intent.setAction(ACTION_UPDATE_ALARM);
        context.startService(intent);
    }

    public static void startActionCreateAllAlarms(Context context) {
        Intent intent = new Intent(context, AlarmHelperService.class);
        intent.setAction(ACTION_CREATE_ALL_ALARMS);
        context.startService(intent);
    }

    public static void startActionDeleteAllAlarms(Context context) {
        Intent intent = new Intent(context, AlarmHelperService.class);
        intent.setAction(ACTION_DELETE_ALL_ALARMS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_CREATE_ALARM.equals(action)) {
                final long id = intent.getLongExtra(EXTRA_REMINDER_ID, -1L);
                handleActionCreateAlarm(id);
            } else if (ACTION_DELETE_ALARM.equals(action)) {
                final long id = intent.getLongExtra(EXTRA_REMINDER_ID, -1L);
                handleActionDeleteAlarm(id);
            } else if (ACTION_UPDATE_ALARM.equals(action)) {
                final long id = intent.getLongExtra(EXTRA_REMINDER_ID, -1L);
                handleActionCreateAlarm(id);
            } else if (ACTION_CREATE_ALL_ALARMS.equals(action)) {
                handleActionCreateAllAlarms();
            } else if (ACTION_DELETE_ALL_ALARMS.equals(action)) {
                handleActionDeleteAllAlarms();
            }
        }
    }

    private void handleActionCreateAlarm(long id) {
        if (id == -1L) return;

        Reminder reminder = ReminderDAO.findReminderById(this, id);
        if (reminder != null) {
            createAlarm(reminder);
        }
    }

    private void handleActionDeleteAlarm(long id) {
        if (id == -1L) return;

        deleteAlarm(id);
    }

    private void handleActionCreateAllAlarms() {
        List<Reminder> reminders = ReminderDAO.findRemindersByCompletedStatus(this, false);
        if (reminders != null) {
            for (Reminder reminder : reminders) {
                createAlarm(reminder);
            }
        }
    }

    private void handleActionDeleteAllAlarms() {
        List<Reminder> reminders = ReminderDAO.findRemindersByCompletedStatus(this, false);
        if (reminders != null) {
            for (Reminder reminder : reminders) {
                deleteAlarm(reminder.getId());
            }
        }
    }

    public void createAlarm(Reminder reminder) {
        Intent intent = NotificationPublisher.getIntent(this, reminder.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) reminder.getId(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminder.getRemindTime(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, reminder.getRemindTime(), pendingIntent);
        }
    }

    public void deleteAlarm(long id) {
        Intent intent = new Intent(this, NotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) id, intent,
                PendingIntent.FLAG_NO_CREATE);

        if (pendingIntent != null) {
            pendingIntent.cancel();
            ((AlarmManager) getSystemService(Context.ALARM_SERVICE)).cancel(pendingIntent);
        }
    }
}
