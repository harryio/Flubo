package com.harryio.flubo.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

class ReminderDbContract {
    static final String CONTENT_AUTHORITY = "com.harryio.flubo.provider";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    static class ReminderEntry implements BaseColumns {
        static final String TABLE_NAME = "Reminders";

        static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_NAME).build();
        static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd.com.harryio.provider."
                + TABLE_NAME;
        static final String CONTENT_TYPE_LIST = "vnd.android.cursor.dir/vnd.com.harryio.provider."
                + TABLE_NAME;

        static final String COLUMN_TITLE = "title";
        static final String COLUMN_DESCRIPTION = "description";
        static final String COLUMN_TIME_MILLIS = "time";
        static final String COLUMN_REMIND = "remind";
        static final String COLUMN_REPEAT_INTERVAL = "repeatInterval";

        static Uri buildReminderUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
