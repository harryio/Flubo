package com.harryio.flubo.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static android.provider.BaseColumns._ID;
import static com.harryio.flubo.data.ReminderDbContract.CONTENT_AUTHORITY;
import static com.harryio.flubo.data.ReminderDbContract.ReminderEntry.CONTENT_TYPE_ITEM;
import static com.harryio.flubo.data.ReminderDbContract.ReminderEntry.CONTENT_TYPE_LIST;
import static com.harryio.flubo.data.ReminderDbContract.ReminderEntry.TABLE_NAME;

//Reference https://guides.codepath.com/android/Creating-Content-Providers
public class ReminderProvider extends ContentProvider {
    private static final int REMINDER_ITEM = 100;
    private static final int REMINDER_LIST = 101;

    private ReminderDbHelper dbHelper;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(CONTENT_AUTHORITY, TABLE_NAME + "/#", REMINDER_ITEM);
        uriMatcher.addURI(CONTENT_AUTHORITY, TABLE_NAME, REMINDER_LIST);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new ReminderDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case REMINDER_ITEM:
                return CONTENT_TYPE_ITEM;

            case REMINDER_LIST:
                return CONTENT_TYPE_LIST;

            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case REMINDER_ITEM:
                long _id = ContentUris.parseId(uri);
                cursor = db.query(TABLE_NAME,
                        projection,
                        _ID + "=?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder);
                break;

            case REMINDER_LIST:
                cursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }

        //noinspection ConstantConditions
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri returnUri;
        switch (uriMatcher.match(uri)) {
            case REMINDER_LIST:
                long _id = db.insert(TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = ReminderDbContract.ReminderEntry.buildReminderUri(_id);
                } else {
                    throw new IllegalStateException("Failed to insert reminders");
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }

        //noinspection ConstantConditions
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows;
        switch (uriMatcher.match(uri)) {
            case REMINDER_LIST:
                rows = db.delete(TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }

        if (selection == null || rows != 0) {
            //noinspection ConstantConditions
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows;
        switch (uriMatcher.match(uri)) {
            case REMINDER_LIST:
                rows = db.update(TABLE_NAME, values, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }

        if (rows != 0) {
            //noinspection ConstantConditions
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }
}