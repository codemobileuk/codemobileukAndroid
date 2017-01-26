package com.codemobile.footsqueek.codemobile.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by greg on 25/01/2017.
 */

public class AlarmService extends Service {

    private NotificationManager manager;
    private NotificationCompat.Builder builder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //  NotificationManager  manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationScheduler notificationScheduler = new NotificationScheduler(getApplicationContext());
        notificationScheduler.checkIfNotificationNeeded();
        Log.d("notification", "rteached ========++========");

        return super.onStartCommand(intent, flags, startId);
    }
}
