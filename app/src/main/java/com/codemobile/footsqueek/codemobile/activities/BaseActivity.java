package com.codemobile.footsqueek.codemobile.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.R;
import com.codemobile.footsqueek.codemobile.fetcher.UpdateTables;
import com.codemobile.footsqueek.codemobile.interfaces.UpdateTablesInterface;

/**
 * Created by greg on 10/02/2017.
 */

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private int[][] states;
    private int[] colors;
    private ColorStateList myList;
    int navigationViewItemPosition = 0;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
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
        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);


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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        myList = new ColorStateList(states, colors);
        navigationView.setItemTextColor(myList);
        navigationView.setItemIconTintList(myList);

        navigationView.getMenu().getItem(0).setChecked(true);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        boolean allTicked = sharedPreferences.getBoolean("allticked",false);
        boolean favoritesTicked = sharedPreferences.getBoolean("favoritesTicked",false);
        boolean noneTicked = sharedPreferences.getBoolean("noneticked",true);


        for (int i = 0; i < menu.size(); i++) {
            Log.d("menust", "size: " +menu.size());
            submenu =  menu.getItem(i).getSubMenu();
            if(submenu != null){
                for (int j = 0; j < submenu.size(); j++) {
                    if(submenu.getItem(j).getItemId()==R.id.all){
                        submenu.getItem(j).setChecked(allTicked);
                        Log.d("menust", "setting all ticked");

                    }else if(submenu.getItem(j).getItemId() == R.id.favorites_only){
                        submenu.getItem(j).setChecked(favoritesTicked);
                        Log.d("menust", "setting fav only");
                    }else if(submenu.getItem(j).getItemId() == R.id.off){
                        submenu.getItem(j).setChecked(noneTicked);
                        Log.d("menust", "setting none ticked");
                    }
                }
            }


        }

        return true;
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
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
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
        if (navigationView != null) {
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
