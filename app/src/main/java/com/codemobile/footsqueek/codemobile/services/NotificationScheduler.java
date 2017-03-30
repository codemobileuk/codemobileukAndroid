package com.codemobile.footsqueek.codemobile.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.database.Session;
import com.codemobile.footsqueek.codemobile.database.SessionFavorite;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.Sort;


/**
 * Created by greg on 25/01/2017.
 */

public class NotificationScheduler {

    private Context context;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public NotificationScheduler(Context context) {
        this.context = context;
    }


    public void checkIfNotificationNeeded(){
        NotificationCreator notification = new NotificationCreator(context);
        Date currentDate = getCurrentDate();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        boolean allTicked = sharedPreferences.getBoolean("allticked",false);
        boolean favoritesTicked = sharedPreferences.getBoolean("favoritesTicked",false);
        boolean noneTicked = sharedPreferences.getBoolean("noneticked",true);

        Realm realm = AppDelegate.getRealmInstance();

        if(noneTicked){

        }else if(allTicked || favoritesTicked){
            List<Session> sessions = realm.where(Session.class).findAllSorted("timeStart");
            Log.d("datee", sessions.size() +"   size");
            if(sessions.size() !=0){
                for (Session s: sessions) {

                    SessionFavorite fav = realm.where(SessionFavorite.class).equalTo("sessionId",s.getId()).findFirst();
                    if(fav.getFavorite()&&favoritesTicked) {
                        //Only create if difference in time from now and talk is less that 15min and more than 1min
                        if (milliBetweenTwoDates(currentDate, s.getTimeStart()) < 1020000 && milliBetweenTwoDates(currentDate, s.getTimeStart()) > 60000) {
                            double seconds = milliBetweenTwoDates(currentDate, s.getTimeStart());
                            notification.createNotification(s.getTitle(), seconds / 60000);
                            Log.d("datee", "created notification " + s.getId());
                            break;
                        } else {
                            Log.d("datee", "dif: " + milliBetweenTwoDates(currentDate, s.getTimeStart()) + "    CD:" + currentDate.toString() + "  SD:" + s.getTimeStart());
                        }
                    }
                }
            }
        }


    }

    private Date getCurrentDate(){

        Calendar calendar = Calendar.getInstance();
        Log.d("date", calendar.getTime().toString() + " Notification Scheduler");
        return calendar.getTime();
    }

    private long milliBetweenTwoDates(Date startDate, Date endDate){

        return endDate.getTime() - startDate.getTime();

    }
}
