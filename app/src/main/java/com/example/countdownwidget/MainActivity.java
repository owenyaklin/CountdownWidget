package com.example.countdownwidget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.countdownwidget.data.CountdownDatabase;
import com.example.countdownwidget.data.CountdownItem;
import com.example.countdownwidget.databinding.ActivityMainBinding;
import com.example.countdownwidget.ui.create.CreateFragment;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    public static final String DISPLAY_MODEL = "ModifyModel";
    private static final String LOG = "MainActivity";
    private static boolean keepRunning = false;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private CountdownItem baseItem = null;
    private ActivityMainBinding binding;
    private TimeZone countdownTimeZone = TimeZone.getDefault();
    private ZonedDateTime targetTime = ZonedDateTime.of(2040, 1, 1, 0, 0, 0, 0, countdownTimeZone.toZoneId());
    private final ActivityResultLauncher<Intent> createActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() != null) {
                        Bundle intentExtras = result.getData().getExtras();
                        if (intentExtras != null) {
                            baseItem = intentExtras.getSerializable(CreateFragment.MODIFY_MODEL, CountdownItem.class);
                            setCountdownValues();
                        }
                    }
                }
            });
    private CountdownDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG, "onCreate");
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Bundle intentExtras = getIntent().getExtras();
        if (intentExtras != null) {
            CountdownItem modifyItem = intentExtras.getSerializable(DISPLAY_MODEL, CountdownItem.class);
            if (modifyItem != null) {
                baseItem = modifyItem;
                setCountdownValues();
            }
        }
        binding.mainEditButton.setOnClickListener(v -> {
            Intent modifyIntent = new Intent(this, CreateActivity.class);
            modifyIntent.putExtra(CreateFragment.MODIFY_MODEL, baseItem);
            createActivityResultLauncher.launch(modifyIntent);
        });
        binding.mainDeleteButton.setOnClickListener(
                v -> new AlertDialog.Builder(this).setTitle(R.string.activity_main_confirmation_title)
                        .setMessage(R.string.activity_main_confirmation_message)
                        .setPositiveButton(R.string.activity_main_confirmation_yes, (dialog, which) -> {
                            if (baseItem != null) {
                                mDatabase.deleteCountdown(baseItem);
                            }
                            setResult(Activity.RESULT_OK);
                            finish();
                        }).setNegativeButton(R.string.activity_main_confirmation_no, null).show());
        calculateCountdown();
        mDatabase = new CountdownDatabase(this);
        keepRunning = true;
        doTheAutoRefresh();
    }

    private void setCountdownValues() {
        if (baseItem != null) {
            binding.headerText.setText(baseItem.getName());
            countdownTimeZone = TimeZone.getTimeZone(baseItem.getTimeZone());
            Calendar newDate = baseItem.getDate();
            Calendar newTime = baseItem.getTime();
            targetTime = ZonedDateTime.of(newDate.get(Calendar.YEAR), newDate.get(Calendar.MONTH) + 1,
                    newDate.get(Calendar.DAY_OF_MONTH), newTime.get(Calendar.HOUR_OF_DAY), newTime.get(Calendar.MINUTE), 0,
                    0, countdownTimeZone.toZoneId());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG, "onDestroy");
        keepRunning = false;
        handler.removeCallbacksAndMessages(null);
        binding = null;
    }

    private void doTheAutoRefresh() {
        handler.postDelayed(() -> {
            // Write code for your refresh logic
            Log.d(LOG, "doTheAutoRefresh");
            calculateCountdown();
            if (keepRunning) {
                doTheAutoRefresh();
            }
        }, DateUtils.MINUTE_IN_MILLIS - System.currentTimeMillis() % DateUtils.MINUTE_IN_MILLIS);
    }

    private void calculateCountdown() {
        ZonedDateTime nowTime = ZonedDateTime.now();
        // ZonedDateTime nowZoned = ZonedDateTime.now(countdownTimeZone.toZoneId());
        Duration difference = Duration.between(nowTime, targetTime);
        if (difference.getSeconds() > 0) {
            long differenceDays = difference.toDays();
            difference = difference.minusDays(differenceDays);
            long differenceHours = difference.toHours();
            difference = difference.minusHours(differenceHours);
            long differenceMinutes = difference.toMinutes();
            difference = difference.minusMinutes(differenceMinutes);
            if (difference.getSeconds() > 0) differenceMinutes += 1;
            if (differenceMinutes == 60) {
                differenceMinutes = 0;
                differenceHours += 1;
                if (differenceHours == 24) {
                    differenceHours = 0;
                    differenceDays += 1;
                }
            }
            StringBuilder countdownString = new StringBuilder();
            boolean started = false;
            if (differenceDays > 0) {
                countdownString.append(differenceDays);
                if (differenceDays == 1) {
                    countdownString.append(" Day, ");
                } else {
                    countdownString.append(" Days, ");
                }
                started = true;
            }
            if (started || differenceHours > 0) {
                countdownString.append(differenceHours);
                if (differenceHours == 1) {
                    countdownString.append(" Hour, ");
                } else {
                    countdownString.append(" Hours, ");
                }
                started = true;
            }
            if (started || differenceMinutes > 0) {
                countdownString.append(differenceMinutes);
                if (differenceMinutes == 1) {
                    countdownString.append(" Minute");
                } else {
                    countdownString.append(" Minutes");
                }
            }
            String finalString = countdownString.toString();
            binding.countdownText.setText(finalString);
        } else {
            binding.countdownText.setText(R.string.activity_main_expired);
        }
    }
}