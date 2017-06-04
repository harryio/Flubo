package com.harryio.flubo.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.harryio.flubo.model.Reminder;

import static com.harryio.flubo.data.ReminderContract.ReminderEntry.buildReminderUri;
import static com.harryio.flubo.data.ReminderContract.ReminderEntry.getReminderListUri;

public class ReminderDAO {
    public static void insert(Context context, Reminder reminder) {
        ContentValues contentValues = DatabaseUtils.getInsertContentValues(reminder);
        context.getContentResolver().insert(getReminderListUri(), contentValues);
    }

    public static Reminder query(Context context, long id) {
        if (id == -1L) return null;

        Cursor cursor = context.getContentResolver().query(buildReminderUri(id), null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Reminder reminder = DatabaseUtils.constructReminderFromCursor(cursor);
            cursor.close();

            return reminder;
        }

        return null;
    }
}
