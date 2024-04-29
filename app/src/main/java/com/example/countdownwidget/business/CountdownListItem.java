package com.example.countdownwidget.business;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.countdownwidget.R;

public class CountdownListItem extends RecyclerView.ViewHolder {
    private final TextView titleTextView;

    public CountdownListItem(@NonNull View itemView) {
        super(itemView);
        titleTextView = itemView.findViewById(R.id.title_text);
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }
}
