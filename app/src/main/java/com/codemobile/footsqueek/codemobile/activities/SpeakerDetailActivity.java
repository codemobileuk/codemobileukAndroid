package com.codemobile.footsqueek.codemobile.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.codemobile.footsqueek.codemobile.R;
import com.codemobile.footsqueek.codemobile.fragments.ScheduleDetailFragment;
import com.codemobile.footsqueek.codemobile.fragments.SpeakerDetailFragment;

/**
 * Created by greg on 23/01/2017.
 */

public class SpeakerDetailActivity extends AppCompatActivity {


    SpeakerDetailFragment speakerDetailFragment;
    String speakerId ="-1";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker_detail);

        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        Intent intent = getIntent();
        if(intent != null){
            speakerId =  intent.getStringExtra("id");//-1?
        }

        Bundle data= new Bundle();
        data.putString("id",speakerId);

        if (savedInstanceState == null) {
            // Insert detail fragment based on the navigationViewItemPosition passed
            speakerDetailFragment = new SpeakerDetailFragment();
            speakerDetailFragment.setArguments(data);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.SpeakerDetailContainer, speakerDetailFragment);
            ft.commit();
        }
    }

}
