package com.harryio.flubo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.harryio.flubo.data.ReminderDbContract.COLUMN_DESCRIPTION;
import static com.harryio.flubo.data.ReminderDbContract.COLUMN_ID;
import static com.harryio.flubo.data.ReminderDbContract.COLUMN_REMIND;
import static com.harryio.flubo.data.ReminderDbContract.COLUMN_REPEAT_INTERVAL;
import static com.harryio.flubo.data.ReminderDbContract.COLUMN_TIME_MILLIS;
import static com.harryio.flubo.data.ReminderDbContract.COLUMN_TITLE;
import static com.harryio.flubo.data.ReminderDbContract.TABLE_NAME;

class ReminderDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "FLUBO";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_REMINDER_TABLE_V1 = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_ID + " TEXT NOT NULL," +
            COLUMN_TITLE + " TEXT NOT NULL," +
            COLUMN_DESCRIPTION + " TEXT," +
            COLUMN_TIME_MILLIS + " INTEGER," +
            COLUMN_REMIND + " INTEGER DEFAULT 0," +
            COLUMN_REPEAT_INTERVAL + " TEXT NOT NULL," +
            "UNIQUE (" + COLUMN_ID + ") ON CONFLICT IGNORE);";

    public ReminderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    db.execSQL(SQL_CREATE_REMINDER_TABLE_V1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
