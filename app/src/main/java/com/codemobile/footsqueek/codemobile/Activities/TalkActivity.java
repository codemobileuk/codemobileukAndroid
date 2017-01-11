package com.codemobile.footsqueek.codemobile.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.Database.Schedule;
import com.codemobile.footsqueek.codemobile.R;

import io.realm.Realm;

/**
 * Created by gregv on 07/01/2017.
 */

public class TalkActivity extends AppCompatActivity {

    TextView title, speaker, timeStart, timeEnd, desc;
    int talkId =-1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);

        Intent intent = getIntent();
        if(intent != null){
            talkId =  intent.getIntExtra("id",0);//-1?
        }

        title = (TextView)findViewById(R.id.title);
        speaker = (TextView)findViewById(R.id.speaker);
        timeStart = (TextView)findViewById(R.id.timeStart);
        timeEnd = (TextView)findViewById(R.id.timeEnd);
        desc = (TextView)findViewById(R.id.desc);


        setTextViews();

    }

    private void setTextViews(){

        Schedule schedule;
        if(talkId == -1){
            title.setText("Error getting talk ");
        }else{
            Realm realm = AppDelegate.getRealmInstance();
            schedule = realm.where(Schedule.class).equalTo("id",talkId).findFirst();

            title.setText(schedule.getTitle());
            speaker.setText(schedule.getSpeaker());
            timeStart.setText(schedule.getTimeStart().toString());
            timeEnd.setText(schedule.getTimeEnd().toString());
            desc.setText(schedule.getDesc());
        }



    }
}
