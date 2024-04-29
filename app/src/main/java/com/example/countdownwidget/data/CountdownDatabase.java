package com.example.countdownwidget.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.countdownwidget.ui.create.CreateViewModel;

import java.util.ArrayList;
import java.util.Calendar;
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

    public ArrayList<CountdownItem> getAllCountdowns() {
        ArrayList<CountdownItem> returnItems = new ArrayList<>();
        try {
            SQLiteDatabase database = sqliteOpenHelper.getReadableDatabase();
            String[] projection = {BaseColumns._ID, CountdownContract.Countdown.COLUMN_NAME_NAME,
                    CountdownContract.Countdown.COLUMN_NAME_DATE, CountdownContract.Countdown.COLUMN_NAME_TIME,
                    CountdownContract.Countdown.COLUMN_NAME_TIME_ZONE};

            // How you want the results sorted in the resulting Cursor
            String sortOrder = BaseColumns._ID + " ASC";
            Cursor cursor = database.query(CountdownContract.Countdown.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    null,                   // The columns for the WHERE clause
                    null,                   // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            );
            if (cursor.moveToFirst()) {
                do {
                    long itemId = cursor.getLong(0);
                    String itemName = cursor.getString(1);
                    Calendar itemDate = Calendar.getInstance();
                    itemDate.setTimeInMillis(cursor.getLong(2));
                    Calendar itemTime = Calendar.getInstance();
                    itemTime.setTimeInMillis(cursor.getLong(3));
                    String itemTimeZone = cursor.getString(4);
                    returnItems.add(new CountdownItem(itemId, itemName, itemDate, itemTime, itemTimeZone));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(LOG, e.toString());
        }
        return returnItems;
    }
}
