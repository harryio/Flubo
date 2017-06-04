package com.harryio.flubo.data;

import android.content.ContentValues;
import android.content.Context;

import com.harryio.flubo.model.Reminder;

import static com.harryio.flubo.data.ReminderContract.ReminderEntry.getReminderListUri;

public class ReminderDAO {
    public static void insert(Context context, Reminder reminder) {
        ContentValues contentValues = ContentValuesUtils.getInsertContentValues(reminder);
        context.getContentResolver().insert(getReminderListUri(), contentValues);
    }
}
