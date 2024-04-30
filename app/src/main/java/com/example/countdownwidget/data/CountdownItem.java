package com.example.countdownwidget.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CountdownItem implements Comparable<CountdownItem> {
    private final long mId;
    private final String mName;
    private final Calendar mDate;
    private final String mDateText;
    private final Calendar mTime;
    private final String mTimeText;
    private final String mTimeZone;

    public CountdownItem(long id, String name, Calendar date, Calendar time, String timeZone) {
        mId = id;
        mName = name;
        mDate = date;
        SimpleDateFormat dateSdf = new SimpleDateFormat("M/d/yyyy", Locale.US);
        Date mDateDate = mDate.getTime();
        mDateText = dateSdf.format(mDateDate);
        mTime = time;
        SimpleDateFormat timeSdf = new SimpleDateFormat("h:mm a", Locale.US);
        Date mTimeDate = mTime.getTime();
        mTimeText = timeSdf.format(mTimeDate);
        mTimeZone = timeZone;
    }

    public long getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getDateText() {
        return mDateText;
    }

    public String getTimeText() {
        return mTimeText;
    }

    public String getTimeZone() {
        return mTimeZone;
    }

    @Override
    public int compareTo(CountdownItem o) {
        return Long.compare(mId, o.getId());
    }

    @Override
    public boolean equals(Object another) {
        if (!(another instanceof CountdownItem)) return false;
        return ((CountdownItem) another).getId() == mId;
    }
}
