package com.harryio.flubo.data;

import android.annotation.TargetApi;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;

import com.harryio.flubo.model.Reminder;
import com.harryio.flubo.model.RepeatInterval;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.harryio.flubo.data.ReminderContract.ReminderEntry.COLUMN_COMPLETED;
import static com.harryio.flubo.data.ReminderContract.ReminderEntry.COLUMN_DESCRIPTION;
import static com.harryio.flubo.data.ReminderContract.ReminderEntry.COLUMN_REPEAT_INTERVAL;
import static com.harryio.flubo.data.ReminderContract.ReminderEntry.COLUMN_TIME_MILLIS;
import static com.harryio.flubo.data.ReminderContract.ReminderEntry.COLUMN_TITLE;
import static com.harryio.flubo.data.ReminderContract.ReminderEntry.getReminderListUri;

public class ReminderLoader extends AsyncTaskLoader<List<Reminder>> {
    private List<Reminder> reminders;

    private ContentObserver reminderDataObserver = new ContentObserver(null) {
        @Override
        @TargetApi(16)
        public void onChange(boolean selfChange) {
            this.onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            onContentChanged();
        }
    };

    public ReminderLoader(Context context) {
        super(context);
    }

    @Override
    public List<Reminder> loadInBackground() {
        Uri uri = getReminderListUri();
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            List<Reminder> reminders = new ArrayList<>(cursor.getCount());
            try {
                while (cursor.moveToNext()) {
                    long id = cursor.getLong(cursor.getColumnIndexOrThrow(_ID));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
                    long remindTime = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TIME_MILLIS));
                    String repeatInterval = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REPEAT_INTERVAL));
                    boolean completed = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED)) == 1;

                    Reminder reminder = new Reminder.Builder()
                            .id(id)
                            .title(title)
                            .description(description)
                            .remindAt(remindTime)
                            .withRepeatInterval(repeatInterval == null ? RepeatInterval.ONE_TIME
                                    : RepeatInterval.valueOf(repeatInterval))
                            .isCompleted(completed)
                            .create();

                    reminders.add(reminder);
                }
            } finally {
                cursor.close();
            }

            return reminders;
        }

        return null;
    }

    @Override
    public void deliverResult(List<Reminder> newReminders) {
        if (isReset()) {
            return;
        }

        reminders = newReminders;

        if (isStarted()) {
            super.deliverResult(newReminders);
        }
    }

    @Override
    protected void onStartLoading() {
        if (reminders != null) {
            deliverResult(reminders);
        }

        getContext().getContentResolver().registerContentObserver(getReminderListUri(), true, reminderDataObserver);

        if (takeContentChanged() || reminders == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStartLoading();

        if (reminders != null) {
            reminders = null;
        }

        getContext().getContentResolver().unregisterContentObserver(reminderDataObserver);
    }
}
