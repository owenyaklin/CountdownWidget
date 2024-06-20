package com.example.countdownwidget.ui.home;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.countdownwidget.MainActivity;
import com.example.countdownwidget.MenuActivity;
import com.example.countdownwidget.business.CountdownListAdapter;
import com.example.countdownwidget.data.CountdownDatabase;
import com.example.countdownwidget.data.CountdownItem;
import com.example.countdownwidget.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    public static final String REFRESH_LIST = "HomeFragment_refreshList";

    private FragmentHomeBinding binding;
    private CountdownDatabase mDatabase;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        mDatabase = new CountdownDatabase(getActivity());
        queryCountdowns();

        ((MenuActivity) requireActivity()).setFragmentRefreshListener(this::queryCountdowns);

        requireActivity().registerReceiver(mMessageReceiver, new IntentFilter(REFRESH_LIST), Context.RECEIVER_NOT_EXPORTED);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        requireActivity().unregisterReceiver(mMessageReceiver);
    }

    private void queryCountdowns() {
        ArrayList<CountdownItem> countdownRows = mDatabase.getAllCountdowns();
        final CountdownListAdapter cla = new CountdownListAdapter(countdownRows);
        binding.listCountdowns.setAdapter(cla);
        cla.setOnClickListener((position, model) -> {
            Intent modifyIntent = new Intent(getActivity(), MainActivity.class);
            modifyIntent.putExtra(MainActivity.DISPLAY_MODEL, model);
            mainActivityResultLauncher.launch(modifyIntent);
        });
        if (countdownRows.isEmpty()) {
            binding.textHome.setVisibility(View.VISIBLE);
            binding.listCountdowns.setVisibility(View.GONE);
        } else {
            binding.textHome.setVisibility(View.GONE);
            binding.listCountdowns.setVisibility(View.VISIBLE);
        }
    }

    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            queryCountdowns();
        }
    };

    private final ActivityResultLauncher<Intent> mainActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            queryCountdowns();
        }
    });

}