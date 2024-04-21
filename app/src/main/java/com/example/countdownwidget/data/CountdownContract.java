package com.example.countdownwidget.data;

import android.provider.BaseColumns;

public final class CountdownContract {
    private CountdownContract() {
    }

    public static class Countdown implements BaseColumns {
        public static final String TABLE_NAME = "Countdown";
        public static final String COLUMN_NAME_NAME = "Name";
        public static final String COLUMN_TYPE_NAME = "TEXT";
        public static final String COLUMN_NAME_DATE = "Date";
        public static final String COLUMN_TYPE_DATE = "INTEGER";
        public static final String COLUMN_NAME_TIME = "Time";
        public static final String COLUMN_TYPE_TIME = "INTEGER";
        public static final String COLUMN_NAME_TIME_ZONE = "TimeZone";
        public static final String COLUMN_TYPE_TIME_ZONE = "TEXT";
    }
}
