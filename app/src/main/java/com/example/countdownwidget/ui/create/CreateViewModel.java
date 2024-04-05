package com.example.countdownwidget.ui.create;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;


public class CreateViewModel extends ViewModel {
    private final MutableLiveData<String> mName;
    private final MutableLiveData<Calendar> mDate;
    private final MutableLiveData<String> mDateText;
    private final MutableLiveData<Calendar> mTime;
    private final MutableLiveData<String> mTimeText;
    private final MutableLiveData<String> mTimeZone;

    public CreateViewModel() {
        mName = new MutableLiveData<>("Countdown Name");
        mDate = new MutableLiveData<>(Calendar.getInstance());
        SimpleDateFormat dateSdf = new SimpleDateFormat("M/d/yyyy", Locale.US);
        Date mDateDate = Objects.requireNonNull(mDate.getValue()).getTime();
        mDateText = new MutableLiveData<>(dateSdf.format(mDateDate));
        mTime = new MutableLiveData<>(Calendar.getInstance());
        SimpleDateFormat timeSdf = new SimpleDateFormat("h:mm a", Locale.US);
        Date mTimeDate = Objects.requireNonNull(mTime.getValue()).getTime();
        mTimeText = new MutableLiveData<>(timeSdf.format(mTimeDate));
        mTimeZone = new MutableLiveData<>(TimeZone.getDefault().getID());
    }

    public MutableLiveData<String> getName() {
        return mName;
    }

    public MutableLiveData<Calendar> getDate() {
        return mDate;
    }

    public MutableLiveData<String> getDateText() {
        return mDateText;
    }

    public MutableLiveData<Calendar> getTime() {
        return mTime;
    }

    public MutableLiveData<String> getTimeText() {
        return mTimeText;
    }

    public MutableLiveData<String> getTimeZone() {
        return mTimeZone;
    }

}