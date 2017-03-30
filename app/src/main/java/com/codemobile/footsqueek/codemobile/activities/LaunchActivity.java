package com.codemobile.footsqueek.codemobile.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.database.RealmUtility;

import io.realm.Realm;

/**
 * Created by greg on 30/03/2017.
 */

public class LaunchActivity extends BaseActivity {

    ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Realm realm = AppDelegate.getRealmInstance();
        pd = new ProgressDialog(this);
        pd.setMessage("Fetching Schedule");
        pd.show();
        if(isNetworkAvailable()) {
           fetchSchedule();
        }else{
            if (realm.isEmpty()) {
                pd.dismiss();
                noConnectionOrPopulatedDb();
            } else {
                pd.dismiss();
                Intent in = new Intent(getApplicationContext(),HomeActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
            }
        }

    }

    public void noConnectionOrPopulatedDb(){

        new AlertDialog.Builder(getApplicationContext())
                .setTitle("There is a problem with the connection")
                .setMessage("Could not find Schedule, please check your internet and try again")
                .setPositiveButton("Thats Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void updateUi() {
        super.updateUi();
        pd.dismiss();
        Intent in = new Intent(getApplicationContext(),HomeActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);
    }
}
