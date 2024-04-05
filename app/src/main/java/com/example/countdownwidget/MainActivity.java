package com.example.countdownwidget;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    private static final String LOG = "MainActivity";
    private static boolean keepRunning = false;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final TimeZone countdownTimeZone = TimeZone.getTimeZone("America/New_York");
    private final ZonedDateTime targetTime = ZonedDateTime.of(2024, 10, 20, 8, 0, 0, 0, countdownTimeZone.toZoneId());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG, "onCreate");
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        calculateCountdown();
        keepRunning = true;
        doTheAutoRefresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG, "onDestroy");
        keepRunning = false;
        handler.removeCallbacksAndMessages(null);
    }

    private void doTheAutoRefresh() {
        handler.postDelayed(() -> {
            // Write code for your refresh logic
            Log.d(LOG, "doTheAutoRefresh");
            calculateCountdown();
            if (keepRunning) {
                doTheAutoRefresh();
            }
        }, 1000);
    }

    private void calculateCountdown() {
        ZonedDateTime nowTime = ZonedDateTime.now();
        // ZonedDateTime nowZoned = ZonedDateTime.now(countdownTimeZone.toZoneId());
        Duration difference = Duration.between(nowTime, targetTime);
        long differenceDays = difference.toDays();
        difference = difference.minusDays(differenceDays);
        long differenceHours = difference.toHours();
        difference = difference.minusHours(differenceHours);
        long differenceMinutes = difference.toMinutes();
        difference = difference.minusMinutes(differenceMinutes);
        if (difference.getSeconds() > 0) differenceMinutes += 1;
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
        ((TextView) findViewById(R.id.countdown_text)).setText(finalString);
    }
}