package com.example.countdownwidget.service;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.format.DateUtils;
import android.util.Log;

import com.example.countdownwidget.CountdownWidgetConfigurationActivity;
import com.example.countdownwidget.CountdownWidgetProvider;

public class WidgetUpdateService extends Service {
    private static final String LOG = "WidgetUpdateService";
    private Handler handler;

    public WidgetUpdateService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG, "onCreate");
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG, "onStartCommand");
        handler.removeCallbacksAndMessages(null);
        doTheAutoRefresh(this);
        return START_STICKY;
    }

    private void doTheAutoRefresh(Context context) {
        long triggerInterval = DateUtils.MINUTE_IN_MILLIS / 6;
        handler.postDelayed(() -> {
            // Write code for your refresh logic
            SharedPreferences prefs = context.getSharedPreferences(CountdownWidgetConfigurationActivity.PREFS_NAME, 0);
            boolean isRunning = prefs.getBoolean(CountdownWidgetProvider.WIDGET_RUNNING, false);
            Log.d(LOG, "doTheAutoRefresh, isRunning = " + isRunning);
            sendUpdateIntent(context);
            if (isRunning) {
                doTheAutoRefresh(context);
            }
        }, triggerInterval - System.currentTimeMillis() % triggerInterval);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG, "onDestroy");
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
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
}