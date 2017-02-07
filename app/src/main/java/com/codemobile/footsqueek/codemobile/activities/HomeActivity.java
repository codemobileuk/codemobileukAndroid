package com.codemobile.footsqueek.codemobile.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.R;
import com.codemobile.footsqueek.codemobile.adapters.ScheduleHorizontalRecyclerAdapter;
import com.codemobile.footsqueek.codemobile.database.Session;
import com.codemobile.footsqueek.codemobile.database.Speaker;
import com.codemobile.footsqueek.codemobile.fetcher.Fetcher;
import com.codemobile.footsqueek.codemobile.interfaces.FetcherInterface;
import com.codemobile.footsqueek.codemobile.services.CurrentSessionChecker;
import com.codemobile.footsqueek.codemobile.services.RoundedCornersTransform;
import com.codemobile.footsqueek.codemobile.services.TimeConverter;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import io.realm.Realm;

/**
 * Created by greg on 20/01/2017.
 */

public class HomeActivity extends AppCompatActivity {

    TextView speakerOneTv, speakerTwoTv, buildingOneTv, buildingTwoTv, speakerOneTv2, buildingOneTv2, startTimeOneTv, startTimeTwoTv, startTimeOneTv2;
    ImageView speakerTwoImage;
    ImageView speakerOneImage;
    ImageView speakerOneImage2;
    ConstraintLayout scheduleButton, locationButton, speakersButton;
    Context context;
    LinearLayout ll;
    RelativeLayout rl1, rl2;
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
        speakerOneTv2 = (TextView)findViewById(R.id.nameTv3);
        buildingOneTv = (TextView)findViewById(R.id.buildingTv1);
        buildingTwoTv = (TextView)findViewById(R.id.buildingTv2);
        buildingOneTv2 = (TextView)findViewById(R.id.buildingTv3);
        speakerOneImage = (ImageView) findViewById(R.id.speakerImageView1);
        speakerTwoImage = (ImageView)findViewById(R.id.speakerImageView2);
        speakerOneImage2 = (ImageView) findViewById(R.id.speakerImageView3);
        locationButton = (ConstraintLayout)findViewById(R.id.button_location);
        scheduleButton = (ConstraintLayout)findViewById(R.id.button_schedule);
        speakersButton = (ConstraintLayout)findViewById(R.id.button_speaker);
        startTimeOneTv = (TextView)findViewById(R.id.timeStartTv1);
        startTimeTwoTv = (TextView)findViewById(R.id.timeStartTv2);
        startTimeOneTv2 = (TextView)findViewById(R.id.timeStartTv3);
        ll = (LinearLayout)findViewById(R.id.ll1);
        rl1 = (RelativeLayout)findViewById(R.id.rl1);
        rl2 = (RelativeLayout)findViewById(R.id.rl2);

        recyclerView = (RecyclerView)findViewById(R.id.recycler);

        fetchSchedule();


        setOnClickListeners();


        setUpScheduledNotifications();
        getCurrentTalk();
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

    public List<Session> getCurrentTalk(){

        Date date = new Date();
        date.getTime();

        Realm realm = AppDelegate.getRealmInstance();
       // Session session = realm.where(Session.class).greaterThan("timeEnd",date).findAllSorted("timeStart").first();
        List<Session> all = realm.where(Session.class).greaterThan("timeEnd",date).findAllSorted("timeStart");
        List<Session> currentSession = new ArrayList<>();
        String prevSessionDate = "";
        for (int i = 0; i < all.size(); i++) {

            if (all.size() > 0){
                //not empty
                if(i == 0 ){
                    currentSession.add(all.get(i));
                }else if(i == 1 && all.get(i).getTimeStart().toString().equals(prevSessionDate) ) {
                    currentSession.add(all.get(i));
                    break;
                }else{
                    break;
                }
                    
                prevSessionDate = all.get(i).getTimeStart().toString();
                
            }
           
            }
        return currentSession;
        
    }

    public void setUpSessionViews(){

        Realm realm = AppDelegate.getRealmInstance();
        List<Session> currentTalks = getCurrentTalk();
        Speaker speaker1 = realm.where(Speaker.class).equalTo("id", currentTalks.get(0).getSpeakerId()).findFirst();

        if(getCurrentTalk().size() ==2){
            Speaker speaker2 = realm.where(Speaker.class).equalTo("id", currentTalks.get(1).getSpeakerId()).findFirst();
            //2
            rl1.setVisibility(View.GONE);
            rl2.setVisibility(View.VISIBLE);
            loadImages(currentTalks.get(0),speakerOneImage);
            loadImages(currentTalks.get(1),speakerTwoImage);
            speakerOneTv.setText(currentTalks.get(0).getTitle());
            speakerTwoTv.setText(currentTalks.get(1).getTitle());
            buildingOneTv.setText(speaker1.getFirstname()+" " +speaker1.getSurname());
            buildingTwoTv.setText(speaker2.getFirstname()+ " " +speaker2.getSurname());

            startTimeOneTv.setText(TimeConverter.trimTimeFromDate(currentTalks.get(0).getTimeStart()));
            startTimeTwoTv.setText(TimeConverter.trimTimeFromDate(currentTalks.get(0).getTimeStart()));
        }else{
            //1

            rl1.setVisibility(View.VISIBLE);
            rl2.setVisibility(View.GONE);
            loadImages(currentTalks.get(0),speakerOneImage2);
            speakerOneTv2.setText(currentTalks.get(0).getTitle());
            buildingOneTv2.setText(speaker1.getFirstname()+" " +speaker1.getSurname());
            startTimeOneTv2.setText(TimeConverter.trimTimeFromDate(currentTalks.get(0).getTimeStart()));
        }


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

    public void loadImages(Session currentTalk, ImageView view){

        Realm realm = AppDelegate.getRealmInstance();

        Speaker speaker1 = realm.where(Speaker.class).equalTo("id", currentTalk.getSpeakerId()).findFirst();

            Picasso.with(HomeActivity.this)
                    .load(speaker1.getPhotoUrl())
                    .fit()
                    .centerCrop()
                    .transform(new RoundedCornersTransform())
                    .into(view);

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
                Intent in = new Intent(getApplicationContext(),LocationsActivity.class);
                startActivity(in);
            }
        });

        speakersButton.setOnClickListener(new View.OnClickListener() {
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
                setUpRecycler();
                setUpSessionViews();
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
        Session nextTalk = null;

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
