package com.harryio.flubo.data;

import android.content.ContentValues;

import com.harryio.flubo.model.Reminder;

import static com.harryio.flubo.data.ReminderContract.ReminderEntry.COLUMN_COMPLETED;
import static com.harryio.flubo.data.ReminderContract.ReminderEntry.COLUMN_DESCRIPTION;
import static com.harryio.flubo.data.ReminderContract.ReminderEntry.COLUMN_REPEAT_INTERVAL;
import static com.harryio.flubo.data.ReminderContract.ReminderEntry.COLUMN_TIME_MILLIS;
import static com.harryio.flubo.data.ReminderContract.ReminderEntry.COLUMN_TITLE;

class ContentValuesUtils {
    static ContentValues getInsertContentValues(Reminder reminder) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, reminder.getTitle());
        contentValues.put(COLUMN_DESCRIPTION, reminder.getDescription());
        contentValues.put(COLUMN_TIME_MILLIS, reminder.getRemindTime());
        contentValues.put(COLUMN_COMPLETED, reminder.isCompleted() ? 0 : 1);
        contentValues.put(COLUMN_REPEAT_INTERVAL, reminder.getRepeatInterval().name());

        return contentValues;
    }
}
