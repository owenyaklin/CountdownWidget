package com.example.countdownwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.countdownwidget.data.CountdownDatabase;
import com.example.countdownwidget.data.CountdownItem;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Implementation of App Widget functionality.
 */
public class CountdownWidgetProvider extends AppWidgetProvider {
    private static final String LOG = "CountdownWidgetProvider";
    private static boolean keepRunning = true;
    private final Handler handler = new Handler(Looper.getMainLooper());

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        SharedPreferences prefs = context.getSharedPreferences(CountdownWidgetConfigurationActivity.PREFS_NAME, 0);
        CountdownItem model = null;
        long countdownId = prefs.getLong("Widget_Id:" + appWidgetId, -1);
        Log.d(LOG, "countdownId = " + countdownId);
        if (countdownId != -1) {
            model = new CountdownDatabase(context).getCountdown(countdownId);
        }
        if (model == null) {
            model = new CountdownItem(-1, context.getString(R.string.appwidget_text), Calendar.getInstance(),
                    Calendar.getInstance(), TimeZone.getDefault().getID());
        }
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.countdown_widget_provider);
        views.setTextViewText(R.id.appwidget_text, model.getName());
        views.setTextViewText(R.id.widget_countdown_text, calculateCountdown(context, model));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Log.d(LOG, "onUpdate");
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        Log.d(LOG, "onEnabled");
        keepRunning = true;
        doTheAutoRefresh(context);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        Log.d(LOG, "onDisabled");
        keepRunning = false;
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.d(LOG, "onDeleted");
        SharedPreferences prefs = context.getSharedPreferences(CountdownWidgetConfigurationActivity.PREFS_NAME, 0);
        SharedPreferences.Editor edit = prefs.edit();
        for (int appWidgetId : appWidgetIds) {
            edit.remove("Widget_Id:" + appWidgetId);
        }
        edit.apply();
    }

    private void doTheAutoRefresh(Context context) {
        handler.postDelayed(() -> {
            // Write code for your refresh logic
            Log.d(LOG, "doTheAutoRefresh");
            sendUpdateIntent(context);
            if (keepRunning) {
                doTheAutoRefresh(context);
            }
        }, 1000);
    }

    private void sendUpdateIntent(Context context) {
        Intent intent = new Intent(context.getApplicationContext(), CountdownWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
        // since it seems the onUpdate() is only fired on that:
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        int[] ids = widgetManager.getAppWidgetIds(new ComponentName(context, CountdownWidgetProvider.class));

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);
    }

    private String calculateCountdown(Context context, CountdownItem model) {
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
            return context.getString(R.string.activity_main_expired);
        }
    }
}