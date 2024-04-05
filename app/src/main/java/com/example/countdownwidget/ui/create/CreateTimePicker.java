package com.example.countdownwidget.ui.create;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class CreateTimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private final MutableLiveData<Calendar> mViewTime;
    private final MutableLiveData<String> mViewTimeText;

    public CreateTimePicker(MutableLiveData<Calendar> viewTime, MutableLiveData<String> viewTimeText) {
        mViewTime = viewTime;
        mViewTimeText = viewTimeText;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = mViewTime.getValue();
        assert c != null;
        return new TimePickerDialog(getActivity(), this, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        mViewTime.setValue(c);
        SimpleDateFormat timeSdf = new SimpleDateFormat("h:mm a", Locale.US);
        Date mTimeDate = Objects.requireNonNull(mViewTime.getValue()).getTime();
        mViewTimeText.setValue(timeSdf.format(mTimeDate));
    }
}
