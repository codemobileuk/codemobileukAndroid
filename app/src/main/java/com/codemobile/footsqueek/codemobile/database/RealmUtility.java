package com.codemobile.footsqueek.codemobile.database;

import android.util.Log;

import com.codemobile.footsqueek.codemobile.AppDelegate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by greg on 19/01/2017.
 */

public class RealmUtility {


    public static void addNewRow(RealmObject obj){

        Realm realm = AppDelegate.getRealmInstance();

        try{
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(obj);
            realm.commitTransaction();

        } catch (Exception e) {
            Log.e("RealmError", "error" + e);
            realm.cancelTransaction();

        }

    }

    public static Session getUpcomingSession(){

        Date date = new Date();
        date.getTime();
        Realm realm = AppDelegate.getRealmInstance();
        List<Session> all = realm.where(Session.class).greaterThan("timeEnd",date).findAllSorted("timeStart");


        return all.get(0);
    }

    public static List<Session> getFutureSessions(){

        Date date = new Date();
        date.getTime();
        Realm realm = AppDelegate.getRealmInstance();

        return realm.where(Session.class).greaterThan("timeEnd",date).findAllSorted("timeStart");
    }


    public static String generatePrimaryKey(String feild1, String feild2){
        return feild1 + feild2;

    }

    public static List<Tag> getUniqueTags(){

        Realm realm = AppDelegate.getRealmInstance();
        List<Tag> tags = realm.where(Tag.class).findAllSorted("tag");
        List<Tag> uniqueTags = new ArrayList<>();
        String tagName = "";
        for (int i = 0; i < tags.size() ; i++) {
            if(!tags.get(i).getTag().equals(tagName)){
                uniqueTags.add(tags.get(i));
            }
            tagName = tags.get(i).getTag();
        }
        return uniqueTags;
    }

}
