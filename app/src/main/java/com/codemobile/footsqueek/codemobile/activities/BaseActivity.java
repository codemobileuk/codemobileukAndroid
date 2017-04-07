package com.codemobile.footsqueek.codemobile.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.R;
import com.codemobile.footsqueek.codemobile.fetcher.UpdateTables;
import com.codemobile.footsqueek.codemobile.interfaces.UpdateTablesInterface;
import com.codemobile.footsqueek.codemobile.services.TimeConverter;


/**
 * Created by greg on 10/02/2017.
 */

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private int[][] states;
    private int[] colors;
    private ColorStateList myList;
    int navigationViewItemPosition = 0;

    Menu submenu;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
       // fetchSchedule();
        setColours();
        setupActionBar();
        determineTwoPane();



    }

    public void setColours(){
       states = new int[][] {
                //    new int[] { android.R.attr.state_enabled}, // enabled
                new int[] {-android.R.attr.state_enabled}, // disabled
                new int[] {-android.R.attr.state_checked}, // unchecked
                new int[] { android.R.attr.state_checked}
        };

        colors = new int[] {
                //   Color.MAGENTA,
                Color.BLACK,
                ContextCompat.getColor(this.getApplicationContext(), R.color.commonDarkGrey),
                ContextCompat.getColor(this.getApplicationContext(), R.color.textRed)

        };
    }



    public void setupActionBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        myList = new ColorStateList(states, colors);
        navigationView.setItemTextColor(myList);
        navigationView.setItemIconTintList(myList);


        if(TimeConverter.isDateAfterThursdayMorning()){
            navigationView.inflateMenu(R.menu.drawer_menu_post_conference);
        }else{


        }
        if (navigationView != null) {
            navigationView.getMenu().getItem(navigationViewItemPosition).setChecked(true);

        }



    }

    private void determineTwoPane(){

        View view = findViewById(R.id.largelayout);
        if(view != null){
            AppDelegate.setTwoPane(true);
        }else{
            AppDelegate.setTwoPane(false);
        }


        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
      //      Toast.makeText(this, "Large screen", Toast.LENGTH_LONG).show();
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
  //          Toast.makeText(this, "Normal sized screen", Toast.LENGTH_LONG).show();
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
    //        Toast.makeText(this, "Small sized screen", Toast.LENGTH_LONG).show();
        }
        else {
    //        Toast.makeText(this, "Screen size is neither large, normal or small", Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view navigationViewItemPosition clicks here.
        int id = item.getItemId();

        if(!item.isChecked()){
            if (id == R.id.nav_home) {
                Intent in = new Intent(getApplicationContext(),HomeActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
            } else if (id == R.id.nav_schedule) {
                Intent in = new Intent(getApplicationContext(),ScheduleActivity.class);
                startActivity(in);
            } else if (id == R.id.nav_speakers) {
                Intent in = new Intent(getApplicationContext(),SpeakerActivity.class);
                startActivity(in);
            } else if (id == R.id.nav_map) {
                Intent in = new Intent(getApplicationContext(),LocationsActivity.class);
                startActivity(in);
            } else if (id == R.id.nav_website) {
                String url = "http://www.codemobile.co.uk/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);


            } else if (id == R.id.nav_sponsors) {

            } else if(id == R.id.nav_feedback){
                String url = "https://docs.google.com/forms/d/e/1FAIpQLSfWruGR12AtCEMVJo_RHzqwyIiaYw9KMvOrK36_DlAD2xUlQw/viewform";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }


        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if(navigationView != null){
            navigationView.getMenu().getItem(navigationViewItemPosition).setChecked(true);
        }

    }

    public void fetchSchedule(){

        final UpdateTables updateTables = new UpdateTables();
        updateTables.setUpdateTablesInterface(new UpdateTablesInterface() {
            @Override
            public void onComplete() {
                updateUi();
            }

            @Override
            public void onError() {

            }
        });
        updateTables.compareAndUpdate();


    }

    public void updateUi(){
        Log.d("activity", this.getLocalClassName()+"   ===   " +" update UI");
    }
}
