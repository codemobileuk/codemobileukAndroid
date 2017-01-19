package com.codemobile.footsqueek.codemobile.database;

import android.util.Log;

import com.codemobile.footsqueek.codemobile.AppDelegate;

import io.realm.Realm;
import io.realm.RealmObject;

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


}
