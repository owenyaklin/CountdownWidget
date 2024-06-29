package com.example.countdownwidget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RemoteViews;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.countdownwidget.business.CountdownListAdapter;
import com.example.countdownwidget.data.CountdownDatabase;
import com.example.countdownwidget.data.CountdownItem;
import com.example.countdownwidget.databinding.ActivityCountdownWidgetConfigurationBinding;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class CountdownWidgetConfigurationActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "CountdownWidgetConfigurationActivity_Prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActivityCountdownWidgetConfigurationBinding binding =
                ActivityCountdownWidgetConfigurationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CountdownDatabase mDatabase = new CountdownDatabase(this);
        ArrayList<CountdownItem> countdownRows = mDatabase.getAllCountdowns();
        final CountdownListAdapter cla = new CountdownListAdapter(countdownRows);
        binding.listConfCountdowns.setAdapter(cla);
        cla.setOnClickListener((position, model) -> {
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
            if (extras != null) {
                appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            }
            Intent tempResultValue = new Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            setResult(Activity.RESULT_CANCELED, tempResultValue);
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putLong("Widget_Id:" + appWidgetId, model.getId());
            edit.apply();

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.countdown_widget_provider);
            views.setTextViewText(R.id.appwidget_text, model.getName());
            views.setTextViewText(R.id.widget_countdown_text, calculateCountdown(model));
            appWidgetManager.updateAppWidget(appWidgetId, views);
            Intent resultValue = new Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        });
    }

    private String calculateCountdown(CountdownItem model) {
        ZonedDateTime nowTime = ZonedDateTime.now();
        TimeZone countdownTimeZone = TimeZone.getTimeZone(model.getTimeZone());
        Calendar newDate = model.getDate();
        Calendar newTime = model.getTime();
        ZonedDateTime targetTime = ZonedDateTime.of(newDate.get(Calendar.YEAR), newDate.get(Calendar.MONTH) + 1,
                newDate.get(Calendar.DAY_OF_MONTH), newTime.get(Calendar.HOUR_OF_DAY), newTime.get(Calendar.MINUTE), 0, 0,
                countdownTimeZone.toZoneId());
        Duration difference = Duration.between(nowTime, targetTime);
        if (difference.getSeconds() > 0) {
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
            return countdownString.toString();
        } else {
            return getString(R.string.activity_main_expired);
        }
    }
}