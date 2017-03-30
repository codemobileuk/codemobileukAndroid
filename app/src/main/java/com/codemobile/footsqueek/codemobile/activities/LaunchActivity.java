package com.codemobile.footsqueek.codemobile.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.database.RealmUtility;

import io.realm.Realm;

/**
 * Created by greg on 30/03/2017.
 */

public class LaunchActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Realm realm = AppDelegate.getRealmInstance();
        if(isNetworkAvailable()) {
            //look to update normal way
        }else{
            if (realm.isEmpty()) {
                // Alert dialog problem
            } else {
                //toast cant update but run anyway
            }
        }

    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
