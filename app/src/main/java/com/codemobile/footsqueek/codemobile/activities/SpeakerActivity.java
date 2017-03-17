package com.codemobile.footsqueek.codemobile.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.R;

/**
 * Created by greg on 23/01/2017.
 */

public class SpeakerActivity extends LaunchActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker);
        determinePaneLayout();
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        setupActionBar();
        navigationViewItemPosition = 2;
    }



    @Override
    protected void onResume() {
        super.onResume();
        navigationViewItemPosition = 2;
    }

    public void determinePaneLayout() {
        FrameLayout fragmentScheduleDetail = (FrameLayout) findViewById(R.id.SpeakerDetailContainer);
        if (fragmentScheduleDetail != null) {
            AppDelegate.setTwoPane(true);
        }else{
            AppDelegate.setTwoPane(false);
        }
    }

}
