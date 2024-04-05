package com.example.countdownwidget.ui.create;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.countdownwidget.R;
import com.example.countdownwidget.databinding.FragmentCreateBinding;

import java.util.TimeZone;

public class CreateFragment extends Fragment {

    private CreateViewModel mViewModel;

    private FragmentCreateBinding binding;

    public static CreateFragment newInstance() {
        return new CreateFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(CreateViewModel.class);
        binding = FragmentCreateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final EditText textName = binding.fragmentCreateName;
        mViewModel.getName().observe(getViewLifecycleOwner(), textName::setText);
        binding.fragmentCreateDate.setOnClickListener(v -> new CreateDatePicker(mViewModel.getDate(), mViewModel.getDateText()).show(requireActivity().getSupportFragmentManager(), "datePicker"));
        final TextView textDate = binding.fragmentCreateDateText;
        mViewModel.getDateText().observe(getViewLifecycleOwner(), textDate::setText);
        binding.fragmentCreateTime.setOnClickListener(v -> new CreateTimePicker(mViewModel.getTime(), mViewModel.getTimeText()).show(requireActivity().getSupportFragmentManager(), "timePicker"));
        final TextView textTime = binding.fragmentCreateTimeText;
        mViewModel.getTimeText().observe(getViewLifecycleOwner(), textTime::setText);
        final Spinner timeZoneSpinner = binding.fragmentCreateTimeZone;
        ArrayAdapter<String> tzAdapter = new ArrayAdapter<>(requireActivity(), R.layout.time_zone_spinner_item, TimeZone.getAvailableIDs());
        timeZoneSpinner.setAdapter(tzAdapter);
        if (mViewModel.getTimeZone().getValue() != null) {
            int spinnerPosition = tzAdapter.getPosition(mViewModel.getTimeZone().getValue());
            timeZoneSpinner.setSelection(spinnerPosition);
        }
        timeZoneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mViewModel.getTimeZone().setValue((String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mViewModel.getTimeZone().setValue("");
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}