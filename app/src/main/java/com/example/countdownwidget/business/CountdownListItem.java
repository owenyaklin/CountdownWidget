package com.example.countdownwidget.business;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.countdownwidget.R;

public class CountdownListItem extends RecyclerView.ViewHolder {
    private final TextView titleTextView;
    private final TextView dateTimeTextView;
    private final TextView timeZoneTextView;

    public CountdownListItem(@NonNull View itemView) {
        super(itemView);
        titleTextView = itemView.findViewById(R.id.title_text);
        dateTimeTextView = itemView.findViewById(R.id.date_time_text);
        timeZoneTextView = itemView.findViewById(R.id.time_zone_text);
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public TextView getDateTimeTextView() {
        return dateTimeTextView;
    }

    public TextView getTimeZoneTextView() {
        return timeZoneTextView;
    }
}
