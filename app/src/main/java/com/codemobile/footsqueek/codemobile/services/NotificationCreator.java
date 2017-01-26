package com.codemobile.footsqueek.codemobile.services;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.codemobile.footsqueek.codemobile.R;

/**
 * Created by greg on 25/01/2017.
 */

public class NotificationCreator {

    Context context;

    public NotificationCreator(Context context) {
        this.context = context;
    }

    public void createNotification(String title, Double mins){

        int minsInt = mins.intValue();

        NotificationManager manager;
        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("A Code mobile talk is starting soon!");
        builder.setContentText(title +" will be starting in "+ minsInt +" minuets");
        builder.setSmallIcon(R.drawable.ic_local_library_black_24dp);
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1,builder.build());

    }

}
