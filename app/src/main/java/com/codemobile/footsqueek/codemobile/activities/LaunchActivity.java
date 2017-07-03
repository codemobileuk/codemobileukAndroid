package com.codemobile.footsqueek.codemobile.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.AnimRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.transition.Explode;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.R;
import com.codemobile.footsqueek.codemobile.database.RealmUtility;
import com.codemobile.footsqueek.codemobile.database.Session;

import io.realm.Realm;

/**
 * Created by greg on 30/03/2017.
 */

public class LaunchActivity extends BaseActivity {

    ProgressDialog pd;
    ImageView logo;
    FrameLayout frame;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        }
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_launch_loading);

        Realm realm = AppDelegate.getRealmInstance();
        logo = (ImageView)findViewById(R.id.logo);
        frame = (FrameLayout)findViewById(R.id.frame);
        pd = new ProgressDialog(LaunchActivity.this);
        pd.setMessage("Fetching Schedule");
        pd.show();

        Session session = realm.where(Session.class).findFirst();

        if(isNetworkAvailable()) {
           fetchSchedule();
        }else{
            if (session != null){
                pd.dismiss();
                noConnectionOrPopulatedDb();
            } else {
                pd.dismiss();
                setIntent();
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        try{
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
        }catch (Exception e){
            Log.e("LaunchActivity" ,e.getMessage());
        }
    }

    public void noConnectionOrPopulatedDb(){

        new AlertDialog.Builder(LaunchActivity.this)
                .setTitle("There is a problem with the connection")
                .setMessage("Could not find Schedule, please check your internet and try again")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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

        try{
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
        }catch (Exception e){
            Log.e("Launch activity" ,e.getMessage());
        }


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setIntent();

            }
        }, 500);

    }

    public void setIntent(){

        //When phone is in landscape (and view locked to portrait) the first intent throws a npe
        try{
            Intent in = new Intent(getApplicationContext(),HomeActivity.class);
            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,frame,"profile");
            //  ActivityOptionsCompat.makeCustomAnimation(this, ,-1);
            //andro ActivityCompat.finishAfterTransition(this);

            ActivityCompat.startActivity(this, in, options.toBundle());
            finish();
        }catch (Exception e){
            Intent in = new Intent(getApplicationContext(),HomeActivity.class);
            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
        }

        /**/
    }


}
