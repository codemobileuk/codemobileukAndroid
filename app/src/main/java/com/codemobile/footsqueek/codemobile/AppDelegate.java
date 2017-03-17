package com.codemobile.footsqueek.codemobile;

import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

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

    public static View sharedView;
    public static String sharedViewId;


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

    public static View getSharedView() {
        return sharedView;
    }

    public static void setSharedView(View sharedView) {
        AppDelegate.sharedView = sharedView;
    }

    public static String getSharedViewId() {
        return sharedViewId;
    }

    public static void setSharedViewId(String sharedViewId) {
        AppDelegate.sharedViewId = sharedViewId;
    }

    public static void setTwoPane(boolean twoPane) {
        AppDelegate.twoPane = twoPane;
    }

    public static Realm getRealmInstance(){
        return Realm.getInstance(realmConfiguration);
    }

    public static int getScreenWidth(Context context){

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
                display.getSize(size);
        return size.x;
    }
}
