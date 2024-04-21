package com.example.countdownwidget.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CountdownSqliteOpenHelper extends SQLiteOpenHelper {
    private static final String LOG = "CountdownDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Countdown.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + CountdownContract.Countdown.TABLE_NAME + " (" + CountdownContract.Countdown._ID + " INTEGER "
                    + "PRIMARY KEY," + CountdownContract.Countdown.COLUMN_NAME_NAME + " " + CountdownContract.Countdown.COLUMN_TYPE_NAME + "," + CountdownContract.Countdown.COLUMN_NAME_DATE + " " + CountdownContract.Countdown.COLUMN_TYPE_DATE + "," + CountdownContract.Countdown.COLUMN_NAME_TIME + " " + CountdownContract.Countdown.COLUMN_TYPE_TIME + "," + CountdownContract.Countdown.COLUMN_NAME_TIME_ZONE + " " + CountdownContract.Countdown.COLUMN_TYPE_TIME_ZONE + ")";

    public CountdownSqliteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(SQL_CREATE_ENTRIES);
        } catch (Exception e) {
            Log.e(LOG, e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
