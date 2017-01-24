package com.codemobile.footsqueek.codemobile.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.R;

/**
 * Created by greg on 23/01/2017.
 */

public class SpeakerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker);
        determinePaneLayout();


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
