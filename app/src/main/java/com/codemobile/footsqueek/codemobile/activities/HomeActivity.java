package com.codemobile.footsqueek.codemobile.activities;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.R;
import com.codemobile.footsqueek.codemobile.adapters.ScheduleHorizontalRecyclerAdapter;
import com.codemobile.footsqueek.codemobile.database.Session;
import com.codemobile.footsqueek.codemobile.fetcher.Fetcher;
import com.codemobile.footsqueek.codemobile.interfaces.FetcherInterface;
import com.codemobile.footsqueek.codemobile.services.CurrentSessionChecker;
import com.codemobile.footsqueek.codemobile.services.NotificationScheduler;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.zip.Inflater;

import io.realm.Realm;

/**
 * Created by greg on 20/01/2017.
 */

public class HomeActivity extends AppCompatActivity {

    TextView speakerOneTv, speakerTwoTv, buildingOneTv, buildingTwoTv,animTv,animTv2;
    ImageView speakerOneImage, speakerTwoImage;
    ConstraintLayout scheduleButton, locationButton;
    Context context;
    LinearLayout ll;
    RecyclerView recyclerView;
    ScheduleHorizontalRecyclerAdapter adapter;

    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        context = getApplicationContext();
        speakerOneTv = (TextView)findViewById(R.id.nameTv1);
        speakerTwoTv = (TextView)findViewById(R.id.nameTv2);
        buildingOneTv = (TextView)findViewById(R.id.buildingTv1);
        buildingTwoTv = (TextView)findViewById(R.id.buildingTv2);
        speakerOneImage = (ImageView)findViewById(R.id.speakerImageView1);
        speakerTwoImage = (ImageView)findViewById(R.id.speakerImageView2);
        locationButton = (ConstraintLayout)findViewById(R.id.button_location);
        scheduleButton = (ConstraintLayout)findViewById(R.id.button_schedule);
        ll = (LinearLayout)findViewById(R.id.ll1);
        animTv = (TextView)findViewById(R.id.anim_tv);
        recyclerView = (RecyclerView)findViewById(R.id.recycler);


        fetchSchedule();

        setUpRecycler();
        setOnClickListeners();
        loadImages();


        setUpScheduledNotifications();


    }

    public void setUpRecycler(){

      //  recyclerView.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(lm);

        Realm realm = AppDelegate.getRealmInstance();
        List<Session> sessions = realm.where(Session.class).findAllSorted("timeStart");


        adapter = new ScheduleHorizontalRecyclerAdapter(sessions);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }



    public void setUpScheduledNotifications(){
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.DAY_OF_MONTH, 25);

        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.AM_PM,Calendar.PM);

        Intent myIntent = new Intent(HomeActivity.this, CurrentSessionChecker.class);
        pendingIntent = PendingIntent.getBroadcast(HomeActivity.this, 0, myIntent,0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);


    }

    public void loadImages(){
        Picasso.with(context).setLoggingEnabled(true);
        Picasso.with(context).load("http://i.imgur.com/o6VMNBW.png").fit().centerCrop().into(speakerOneImage);
        Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").fit().centerCrop().into(speakerTwoImage);

    }
    public void setOnClickListeners(){

        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(),ScheduleActivity.class);
                startActivity(in);
            }
        });

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(),SpeakerActivity.class);
                startActivity(in);
            }
        });

    }

    public void fetchSchedule(){

        final Fetcher fetcher= new Fetcher();
        fetcher.setFetcherInterface(new FetcherInterface() {

            @Override
            public void onComplete() {
                fetchSpeakers();
                setDoubleRows();

            }

            @Override
            public void onError() {

            }

            @Override
            public void onProgress() {

            }
        });
        fetcher.execute("Schedule");

    }
    public void fetchSpeakers(){

        final Fetcher fetcher= new Fetcher();
        fetcher.setFetcherInterface(new FetcherInterface() {

            @Override
            public void onComplete() {
                fetchLocations();
            }

            @Override
            public void onError() {

            }

            @Override
            public void onProgress() {

            }
        });
        fetcher.execute("Speakers");

    }

    public void fetchLocations(){

        final Fetcher fetcher= new Fetcher();
        fetcher.setFetcherInterface(new FetcherInterface() {

            @Override
            public void onComplete() {

            }

            @Override
            public void onError() {

            }

            @Override
            public void onProgress() {

            }
        });
        fetcher.execute("Locations");

    }

    public void setDoubleRows(){
        //returns all the speakers filtered on date then updates the data to allow for a concept
        //of double rows.
        //todo filter by room
        Realm realm = AppDelegate.getRealmInstance();

        List <Session> allTalks = realm.where(Session.class).findAllSorted("timeStart");
        Session tempTalk = null;

        realm.beginTransaction();
        for(Session talk :allTalks){

            if(tempTalk !=null){
                if(tempTalk.getTimeStart().equals(talk.getTimeStart())){
                    talk.setDoubleRow(true);
                    tempTalk.setDoubleRow(true);
                }else{
                    talk.setDoubleRow(false);
                }
            }
            tempTalk = talk;
        }
        realm.commitTransaction();

    }
}
