package com.harryio.flubo.data;

import android.content.ContentValues;
import android.database.Cursor;

import com.harryio.flubo.model.Reminder;
import com.harryio.flubo.model.RepeatInterval;

import static android.provider.BaseColumns._ID;
import static com.harryio.flubo.data.ReminderContract.ReminderEntry.COLUMN_COMPLETED;
import static com.harryio.flubo.data.ReminderContract.ReminderEntry.COLUMN_DESCRIPTION;
import static com.harryio.flubo.data.ReminderContract.ReminderEntry.COLUMN_REPEAT_INTERVAL;
import static com.harryio.flubo.data.ReminderContract.ReminderEntry.COLUMN_TIME_MILLIS;
import static com.harryio.flubo.data.ReminderContract.ReminderEntry.COLUMN_TITLE;

class DatabaseUtils {
    static ContentValues getInsertContentValues(Reminder reminder) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, reminder.getTitle());
        contentValues.put(COLUMN_DESCRIPTION, reminder.getDescription());
        contentValues.put(COLUMN_TIME_MILLIS, reminder.getRemindTime());
        contentValues.put(COLUMN_COMPLETED, reminder.isCompleted() ? 0 : 1);
        contentValues.put(COLUMN_REPEAT_INTERVAL, reminder.getRepeatInterval().name());

        return contentValues;
    }

    static Reminder constructReminderFromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(_ID));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
        long remindTime = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TIME_MILLIS));
        String repeatInterval = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REPEAT_INTERVAL));
        boolean completed = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED)) == 1;

        return new Reminder.Builder()
                .id(id)
                .title(title)
                .description(description)
                .remindAt(remindTime)
                .withRepeatInterval(repeatInterval == null ? RepeatInterval.ONE_TIME
                        : RepeatInterval.valueOf(repeatInterval))
                .isCompleted(completed)
                .create();
    }
}
