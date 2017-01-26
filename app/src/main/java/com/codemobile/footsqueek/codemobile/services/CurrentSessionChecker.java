package com.codemobile.footsqueek.codemobile.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by greg on 25/01/2017.
 */

public class CurrentSessionChecker extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service1 = new Intent(context, AlarmService.class);
        context.startService(service1);
    }
}
