package com.harryio.flubo.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.harryio.flubo.model.Reminder;

import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.harryio.flubo.data.ReminderContract.ReminderEntry.buildReminderUri;
import static com.harryio.flubo.data.ReminderContract.ReminderEntry.getReminderListUri;
import static com.harryio.flubo.data.ReminderContract.ReminderEntry.getScheduledRemindersUri;

public class ReminderDAO {
    public static Uri insert(Context context, Reminder reminder) {
        ContentValues contentValues = DatabaseUtils.getContentValuesForReminder(reminder);
        return context.getContentResolver().insert(getReminderListUri(), contentValues);
    }

    public static Reminder findReminderById(Context context, long id) {
        if (id == -1L) return null;

        Cursor cursor = context.getContentResolver().query(buildReminderUri(id), null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Reminder reminder = DatabaseUtils.constructReminderFromCursor(cursor);
            cursor.close();

            return reminder;
        }

        return null;
    }

    public static List<Reminder> findRemindersByCompletedStatus(Context context, boolean completed) {
        Cursor cursor = context.getContentResolver().query(getScheduledRemindersUri(completed),
                null, null, null, null);

        return DatabaseUtils.getRemindersFromCursor(cursor);
    }

    public static void update(Context context, Reminder reminder) {
        ContentValues contentValues = DatabaseUtils.getContentValuesForReminder(reminder);
        context.getContentResolver().update(getReminderListUri(), contentValues, _ID + " = ? ",
                new String[]{String.valueOf(reminder.getId())});
    }

    public static void delete(Context context, long id) {
        context.getContentResolver().delete(getReminderListUri(), _ID + " = ? ",
                new String[]{String.valueOf(id)});
    }

    public static void deleteAll(Context context) {
        context.getContentResolver().delete(getReminderListUri(), null, null);
    }

    public static void insert(Context context, List<Reminder> reminders) {
        int size = reminders.size();
        ContentValues[] contentValues = new ContentValues[size];
        for (int i = 0; i < size; ++i) {
            contentValues[i] = DatabaseUtils.getContentValuesForReminder(reminders.get(i));
        }

        context.getContentResolver().bulkInsert(getReminderListUri(), contentValues);
    }
}
