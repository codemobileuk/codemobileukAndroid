package com.codemobile.footsqueek.codemobile.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.R;
import com.codemobile.footsqueek.codemobile.adapters.ScheduleHorizontalRecyclerAdapter;
import com.codemobile.footsqueek.codemobile.customUi.DoublePreviewView;
import com.codemobile.footsqueek.codemobile.customUi.PreviewView;
import com.codemobile.footsqueek.codemobile.database.RealmUtility;
import com.codemobile.footsqueek.codemobile.database.Session;
import com.codemobile.footsqueek.codemobile.interfaces.HorizontalScheduleRecyclerInterface;
import com.codemobile.footsqueek.codemobile.services.CurrentSessionChecker;
import com.codemobile.footsqueek.codemobile.services.TimeManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;

/**
 * Created by greg on 20/01/2017.
 */

public class HomeActivity extends BaseActivity {


    ConstraintLayout scheduleButton, locationButton, speakersButton;
    Context context;
    LinearLayout ll,notificationPanel;
    RecyclerView recyclerView;
    ScheduleHorizontalRecyclerAdapter adapter;
    PreviewView previewView;
    DoublePreviewView doublePreviewView;
    TextView notificationTv, mondayNotificationTv;
    ConstraintLayout mondayConstraintLayout;

    List<Session> upComingSessions;
    List<Session> allSessions;
    Date currentDate;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    HorizontalScheduleRecyclerInterface horizontalScheduleRecyclerInterface;


    //TODO reduce lines by removing the extra views that get hidden and instead resizing the original views

    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

      //  fetchSchedule();

        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        currentDate = new Date();
        currentDate.getTime();
        context = getApplicationContext();
        locationButton = (ConstraintLayout)findViewById(R.id.button_location);
        scheduleButton = (ConstraintLayout)findViewById(R.id.button_schedule);
        speakersButton = (ConstraintLayout)findViewById(R.id.button_speaker);
        notificationTv = (TextView)findViewById(R.id.notification_tv);
        notificationPanel = (LinearLayout)findViewById(R.id.notification_panel);
        ll = (LinearLayout)findViewById(R.id.ll1);
        mondayNotificationTv = (TextView)findViewById(R.id.announcementTextView);
        mondayConstraintLayout = (ConstraintLayout)findViewById(R.id.mondayEventCL);

        previewView = (PreviewView)findViewById(R.id.previewView);
        doublePreviewView = (DoublePreviewView)findViewById(R.id.previewViewDouble);

        recyclerView = (RecyclerView)findViewById(R.id.recycler);

        Realm realm = AppDelegate.getRealmInstance();
        upComingSessions = realm.where(Session.class).greaterThan("timeEnd",currentDate).notEqualTo("sessionType","Break").findAllSorted("timeStart");

        allSessions = realm.where(Session.class).findAllSorted("timeStart");

        setOnClickListeners();
        setUpScheduledNotifications();
        setupActionBar();
        navigationViewItemPosition = 0;
        thursdaySpecialEvent();



    }

    public void thursdaySpecialEvent(){

        boolean showDialog = sharedPreferences.getBoolean("noneticked",true);

        if(TimeManager.isDateAfterThursdayMorning() && TimeManager.isBeforeEndOfThursday() && showDialog){
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setMessage("If you have a moment please fill in our feedback form, you can access it from the main menu at anytime.");
            builder.setTitle("Code Mobile Feedback form");
            builder.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("thursdayalert", false);
                    editor.commit();

                    dialog.cancel();
                    String url = "https://docs.google.com/forms/d/e/1FAIpQLSfWruGR12AtCEMVJo_RHzqwyIiaYw9KMvOrK36_DlAD2xUlQw/viewform";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });
            builder.setNegativeButton("No Thanks", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("thursdayalert", false);
                    editor.commit();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
            showDialog = false;
        }else if(TimeManager.isDateAfterThursdayMorning()){
        }
    }

    public void setNotificationTvText(){

        boolean allTicked = sharedPreferences.getBoolean("allticked",false);
        boolean favoritesTicked = sharedPreferences.getBoolean("favoritesTicked",false);
        boolean noneTicked = sharedPreferences.getBoolean("noneticked",true);

        if(allTicked){
            notificationTv.setText("Notifications are ON");
        }else if(favoritesTicked){
            notificationTv.setText("Notifications are ON for favorites only");
        }else if(noneTicked){
            notificationTv.setText("Notifications are OFF");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationViewItemPosition = 0;
        refreshList();
        setUpHorizontalRecycler();
        setUpPreviewViews();
        setNotificationTvText();
    }

    public void setUpHorizontalRecycler(){
        LinearLayoutManager lm = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(lm);
        adapter = new ScheduleHorizontalRecyclerAdapter(upComingSessions,context);

        horizontalScheduleRecyclerInterface = new HorizontalScheduleRecyclerInterface() {
            @Override
            public void clicked(String sessionId) {
                Intent in = new Intent(getApplicationContext(),ScheduleActivity.class);
                in.putExtra("id",sessionId);
                startActivity(in);
            }
        };
        adapter.setHorizontalScheduleRecyclerInterface(horizontalScheduleRecyclerInterface);

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public boolean eventOn(){

        if(allSessions.size() != 0){
            Date firstSession = allSessions.get(0).getTimeStart();
            Date lastSession = allSessions.get(allSessions.size()-1).getTimeEnd();

            if(currentDate.after(firstSession) && currentDate.before(lastSession)){
                return true;
            }
        }

        return false;
    }
    public boolean mondayBeforeEvent(){
        Calendar monday = Calendar.getInstance();

        monday.set(Calendar.MONTH, 3);
        monday.set(Calendar.YEAR, 2017);
        monday.set(Calendar.DAY_OF_MONTH,17);

       // Date monday = calendar.getTime();

        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);

        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int mondayMonthDay = monday.get(Calendar.DAY_OF_MONTH);
        int mondayMonth = monday.get(Calendar.MONTH);

        if(day == mondayMonthDay && month == mondayMonth){
            return true;
        }else{
            return false;
        }

    }
    public boolean eventOver(){

        if(allSessions.size() >1){
            Date lastSession = allSessions.get(allSessions.size()-1).getTimeEnd();
            if(currentDate.after(lastSession)){
                return true;
            }
        }

        return false;
    }
    public boolean eventUpcoming(){


        if(allSessions.size()!=0){
            Date firstSession = allSessions.get(0).getTimeStart();
            if(currentDate.before(firstSession)){
                return true;
            }
        }

        return false;
    }

    public List<Session> getCurrentTalk(){


        List<Session> all = RealmUtility.getFutureSessions();
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

    public void refreshList(){
        Realm realm = AppDelegate.getRealmInstance();
        currentDate = new Date();
        currentDate.getTime();
        upComingSessions = realm.where(Session.class).greaterThan("timeEnd",currentDate).notEqualTo("sessionType","Break").findAllSorted("timeStart");
        allSessions = realm.where(Session.class).findAllSorted("timeStart");
        setUpPreviewViews();
        if(adapter != null){
            adapter.notifyDataSetChanged();
        }
    }



    public void setUpPreviewViews(){

        if(mondayBeforeEvent()){
            /**
             * Hardcoded an event occurring on the monday. To edit the day
             * @see #mondayBeforeEvent()
             * **/
            mondayConstraintLayout.setVisibility(View.VISIBLE);
            previewView.setVisibility(View.GONE);
            doublePreviewView.setVisibility(View.GONE);

            mondayNotificationTv.setText("Come down to Telfords bar for drinks at 7:00 \n Click here to navigate");
            mondayConstraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String lon = "-2.9003047";
                    String lat = "53.19341";

                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr=" + lat +"," +lon +""));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }else if(eventUpcoming()){

            mondayConstraintLayout.setVisibility(View.VISIBLE);
            previewView.setVisibility(View.GONE);
            doublePreviewView.setVisibility(View.GONE);

        }else if(eventOn()){
            if(getCurrentTalk().size() == 2){

                mondayConstraintLayout.setVisibility(View.GONE);
                previewView.setVisibility(View.GONE);
                doublePreviewView.setVisibility(View.VISIBLE);
                doublePreviewView.loadViews();

            }else{
                mondayConstraintLayout.setVisibility(View.GONE);
                previewView.setVisibility(View.VISIBLE);
                doublePreviewView.setVisibility(View.GONE);
                previewView.loadViews();
            }
        }else if(eventOver()) {


            mondayConstraintLayout.setVisibility(View.VISIBLE);
            previewView.setVisibility(View.GONE);
            doublePreviewView.setVisibility(View.GONE);
            mondayNotificationTv.setText("Code Mobile is now over \n See you next year!");
        }

    }

        public void setUpScheduledNotifications(){

            Intent myIntent = new Intent(HomeActivity.this, CurrentSessionChecker.class);
            pendingIntent = PendingIntent.getBroadcast(HomeActivity.this, 0, myIntent,0);

            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

            boolean noneTicked = sharedPreferences.getBoolean("noneticked",true);

            if(!noneTicked){
                Calendar calendar = Calendar.getInstance();

                calendar.set(Calendar.MONTH, 0);
                calendar.set(Calendar.YEAR, 2017);
                calendar.set(Calendar.DAY_OF_MONTH, 25);

                calendar.set(Calendar.HOUR_OF_DAY, 11);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.AM_PM,Calendar.PM);


                /**
                 * sets an alaram manager to go off every 15 minuets from the moment activated.
                 * It then checks upcoming talks and creates a notification. Turning notifications off after
                 * it has set one will not stop that specific one at the moment. Could probbably kill this intent.
                 * **/

                // TODO: set to go off at a specific time not when first run
                // TODO: check doest run again and again. Not sure if it duplicates etc.
                // TODO: see if this intent can be slaughtered


                alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);

                Log.d("Calender", "----- " +calendar.getTime());
            }else{
                alarmManager.cancel(pendingIntent);
                Log.d("Calender", "cancel");
            }



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

        previewView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        doublePreviewView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar navigationViewItemPosition clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        editor = sharedPreferences.edit();
        SharedPreferences.Editor editor = sharedPreferences.edit();



        int id = item.getItemId();
        if(item.isCheckable()){
            if(item.isChecked()){

            }else{
                item.setChecked(true);
                toggleNotificationCheckBoxes(item.getItemId());

            }

        }
        editor.commit();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setNotificationTvText();
            }
        }, 0);
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        boolean allTicked = sharedPreferences.getBoolean("allticked", false);
        boolean favoritesTicked = sharedPreferences.getBoolean("favoritesTicked", false);
        boolean noneTicked = sharedPreferences.getBoolean("noneticked", true);


        for (int i = 0; i < menu.size(); i++) {
            Log.d("menust", "size: " + menu.size());
            submenu = menu.getItem(i).getSubMenu();
            if (submenu != null) {
                for (int j = 0; j < submenu.size(); j++) {
                    if (submenu.getItem(j).getItemId() == R.id.all) {
                        submenu.getItem(j).setChecked(allTicked);
                        Log.d("menust", "setting all ticked");

                    } else if (submenu.getItem(j).getItemId() == R.id.favorites_only) {
                        submenu.getItem(j).setChecked(favoritesTicked);
                        Log.d("menust", "setting fav only");
                    } else if (submenu.getItem(j).getItemId() == R.id.off) {
                        submenu.getItem(j).setChecked(noneTicked);
                        Log.d("menust", "setting none ticked");
                    }
                }
            }


        }

        return true;
    }

    public void toggleNotificationCheckBoxes(int id){

        MenuItem all = null;
        MenuItem favorites = null;
        MenuItem none = null;
        for (int i = 0; i < submenu.size(); i++) {
            if(submenu.getItem(i).getItemId() ==R.id.all){
                all = submenu.getItem(i);
            }else  if(submenu.getItem(i).getItemId() ==R.id.favorites_only){
                favorites = submenu.getItem(i);
            }else  if(submenu.getItem(i).getItemId() ==R.id.off){
                none = submenu.getItem(i);
            }

        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("allticked", false);
        editor.putBoolean("favoritesTicked", false);
        editor.putBoolean("noneticked", false);

        if(favorites != null && all != null && none != null){
            favorites.setChecked(false);
            all.setChecked(false);
            none.setChecked(false);


            if(all.getItemId() == id){
                editor.putBoolean("allticked", true);
                all.setChecked(true);
            }else if(favorites.getItemId() == id){
                editor.putBoolean("favoritesTicked", true);
                favorites.setChecked(true);
            }else if(none.getItemId() == id){
                editor.putBoolean("noneticked", true);
                none.setChecked(true);
            }
        }
        editor.commit();
    }

}



