package com.codemobile.footsqueek.codemobile;

import android.app.Application;
import android.content.Context;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;

/**
 * Created by gregv on 07/01/2017.
 */

public class AppDelegate extends Application{


    public static RealmConfiguration realmConfiguration;
    public static boolean twoPane = false;


    @Override
    public void onCreate() {
        super.onCreate();

        //TODO add in realm migration once data set is more stable
        Context ctx = getApplicationContext();
        Realm.init(ctx);
        realmConfiguration = new RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build();
    }

    public static boolean isTwoPane() {
        return twoPane;
    }

    public static void setTwoPane(boolean twoPane) {
        AppDelegate.twoPane = twoPane;
    }

    public static Realm getRealmInstance(){
        return Realm.getInstance(realmConfiguration);
    }
}
