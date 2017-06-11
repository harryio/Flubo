package com.harryio.flubo.data;

import android.annotation.TargetApi;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;

import com.harryio.flubo.model.Reminder;

import java.util.List;

import static com.harryio.flubo.data.ReminderContract.ReminderEntry.COLUMN_TIME_MILLIS;
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
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, COLUMN_TIME_MILLIS + " DESC");

        return DatabaseUtils.getRemindersFromCursor(cursor);
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
