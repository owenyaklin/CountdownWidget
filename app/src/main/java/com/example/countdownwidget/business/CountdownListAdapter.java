package com.example.countdownwidget.business;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.countdownwidget.R;
import com.example.countdownwidget.data.CountdownItem;

import java.util.ArrayList;

public class CountdownListAdapter extends RecyclerView.Adapter<CountdownListItem> {
    private final ArrayList<CountdownItem> values;

    public CountdownListAdapter(ArrayList<CountdownItem> values) {
        this.values = values;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public CountdownListItem onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.countdown_list_adapter, viewGroup, false);

        return new CountdownListItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountdownListItem holder, int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        holder.getTitleTextView().setText(values.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return values.size();
    }
}
