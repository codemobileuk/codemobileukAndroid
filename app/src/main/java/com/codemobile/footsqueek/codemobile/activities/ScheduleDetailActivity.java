package com.codemobile.footsqueek.codemobile.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.codemobile.footsqueek.codemobile.R;
import com.codemobile.footsqueek.codemobile.fragments.ScheduleDetailFragment;

/**
 * Created by greg on 19/01/2017.
 */

public class ScheduleDetailActivity extends BaseActivity {

    ScheduleDetailFragment scheduleDetailFragment;
    String talkId ="-1";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_detail);
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setupActionBar();
        getSupportActionBar().setTitle("Schedule");
        Intent intent = getIntent();
        if(intent != null){
            talkId =  intent.getStringExtra("id");//-1?
        }

        Bundle data= new Bundle();
        data.putString("id",talkId);

        if (savedInstanceState == null) {
            // Insert detail fragment based on the navigationViewItemPosition passed
            scheduleDetailFragment = new ScheduleDetailFragment();
            scheduleDetailFragment.setArguments(data);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.ScheduleDetailContainer, scheduleDetailFragment);
            ft.commit();
        }
    }
}
