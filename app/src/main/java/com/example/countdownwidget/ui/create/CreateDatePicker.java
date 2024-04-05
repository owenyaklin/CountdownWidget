package com.example.countdownwidget.ui.create;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class CreateDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private final MutableLiveData<Calendar> mViewDate;
    private final MutableLiveData<String> mViewDateText;

    public CreateDatePicker(MutableLiveData<Calendar> viewDate, MutableLiveData<String> viewDateText) {
        mViewDate = viewDate;
        mViewDateText = viewDateText;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = mViewDate.getValue();
        assert c != null;
        DatePickerDialog dpd = new DatePickerDialog(requireContext(), this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dpd.getDatePicker().setMinDate(System.currentTimeMillis());
        return dpd;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        mViewDate.setValue(c);
        SimpleDateFormat dateSdf = new SimpleDateFormat("M/d/yyyy", Locale.US);
        Date mDateDate = Objects.requireNonNull(mViewDate.getValue()).getTime();
        mViewDateText.setValue(dateSdf.format(mDateDate));
    }
}
