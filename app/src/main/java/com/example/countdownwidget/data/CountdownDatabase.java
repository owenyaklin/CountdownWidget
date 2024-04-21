package com.example.countdownwidget.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.countdownwidget.ui.create.CreateViewModel;

import java.util.Objects;

public class CountdownDatabase {
    private static final String LOG = "CountdownDatabase";
    private final CountdownSqliteOpenHelper sqliteOpenHelper;

    public CountdownDatabase(Context context) {
        sqliteOpenHelper = new CountdownSqliteOpenHelper(context);
    }

    public void writeNewCountdown(CreateViewModel newModel) {
        try {
            SQLiteDatabase database = sqliteOpenHelper.getWritableDatabase();
            ContentValues insertValues = new ContentValues();
            insertValues.put(CountdownContract.Countdown.COLUMN_NAME_NAME, newModel.getName().getValue());
            insertValues.put(CountdownContract.Countdown.COLUMN_NAME_DATE,
                    Objects.requireNonNull(newModel.getDate().getValue()).getTimeInMillis());
            insertValues.put(CountdownContract.Countdown.COLUMN_NAME_TIME,
                    Objects.requireNonNull(newModel.getTime().getValue()).getTimeInMillis());
            insertValues.put(CountdownContract.Countdown.COLUMN_NAME_TIME_ZONE, newModel.getTimeZone().getValue());
            database.insert(CountdownContract.Countdown.TABLE_NAME, null, insertValues);
        } catch (Exception e) {
            Log.e(LOG, e.toString());
        }
    }
}
