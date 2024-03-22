package com.example.countdownwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.TimeZone;

/**
 * Implementation of App Widget functionality.
 */
public class CountdownWidgetProvider extends AppWidgetProvider {

    private final TimeZone countdownTimeZone = TimeZone.getTimeZone("America/New_York");
    private final ZonedDateTime targetTime = ZonedDateTime.of(2024, 10, 20, 8, 0, 0, 0, countdownTimeZone.toZoneId());

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.countdown_widget_provider);
        views.setTextViewText(R.id.appwidget_text, widgetText);
        views.setTextViewText(R.id.widget_countdown_text, calculateCountdown());

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private String calculateCountdown() {
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
        return countdownString.toString();
    }
}