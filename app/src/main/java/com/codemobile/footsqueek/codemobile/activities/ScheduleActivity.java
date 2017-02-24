package com.codemobile.footsqueek.codemobile.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.R;
import com.codemobile.footsqueek.codemobile.customUi.LineButton;
import com.codemobile.footsqueek.codemobile.database.RealmUtility;
import com.codemobile.footsqueek.codemobile.database.Tag;
import com.codemobile.footsqueek.codemobile.fragments.ScheduleRecyclerFragment;
import com.codemobile.footsqueek.codemobile.interfaces.ScheduleDayChooserInterface;
import com.codemobile.footsqueek.codemobile.interfaces.ScheduleFilterInterface;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by greg on 19/01/2017.
 */

public class ScheduleActivity extends LaunchActivity {

    LineButton dayOneBtn, dayTwoBtn, dayThreeBtn;

    ScheduleDayChooserInterface scheduleDayChooserInterface;
    ScheduleFilterInterface filterInterface;

    List<Tag> uniqueTags;
    List<String> filteredTagNames = new ArrayList<>();
    Activity activity;
    String id;
    FrameLayout fragmentLayout;
    ScheduleRecyclerFragment newFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        activity = this;

        fragmentLayout = (FrameLayout)findViewById(R.id.fragmentFrame);
        fragmentLayout.setId(R.id.fragmentId);
        newFragment = new ScheduleRecyclerFragment();

        Intent intent =  getIntent();
        id = intent.getStringExtra("id");

        if (savedInstanceState == null) {

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.fragmentId, newFragment).commit();
            Bundle bundle = new Bundle();
            bundle.putString("id",id);
            newFragment.setArguments(bundle);
        }

        List<Tag> tags = RealmUtility.getUniqueTags();
        for (int i = 0; i < tags.size(); i++) {
            filteredTagNames.add(tags.get(i).getTag());
        }
        uniqueTags = RealmUtility.getUniqueTags();

        dayOneBtn = (LineButton)findViewById(R.id.dayOneButton);
        dayTwoBtn = (LineButton)findViewById(R.id.dayTwoButton);
        dayThreeBtn = (LineButton)findViewById(R.id.dayThreeButton);

        determinePaneLayout();
        setupActionBar();
        navigationViewItemPosition = 1;

        setupButtonListeners();

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        scheduleDayChooserInterface = newFragment.getScheduleDayChooserInterface();
    }

    public void determinePaneLayout() {
        FrameLayout fragmentScheduleDetail = (FrameLayout) findViewById(R.id.ScheduleDetailContainer);
        if (fragmentScheduleDetail != null) {
            AppDelegate.setTwoPane(true);
        }else{
            AppDelegate.setTwoPane(false);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        navigationViewItemPosition = 1;
    }

    public void setupButtonListeners(){

        List<LineButton> buttons = new ArrayList<>();
        buttons.add(dayOneBtn);
        buttons.add(dayTwoBtn);
        buttons.add(dayThreeBtn);

        dayOneBtn.setTogglePartners(buttons,true);
        dayTwoBtn.setTogglePartners(buttons,false);
        dayThreeBtn.setTogglePartners(buttons, false);

        dayOneBtn.isClickable();

        dayOneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleDayChooserInterface.dayOne();
                dayOneBtn.customClick();
            }
        });
        dayTwoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleDayChooserInterface.dayTwo();
                dayTwoBtn.customClick();
            }
        });
        dayThreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleDayChooserInterface.dayThree();
                dayThreeBtn.customClick();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        for (int i = 0; i < uniqueTags.size(); i++) {
            menu.add(uniqueTags.get(i).getTag());
            menu.getItem(i).setCheckable(true);
            menu.getItem(i).setChecked(true);
        }
      //  menu.add(uniqueTags.get(i).getTag());
       // menu.getItem(uniqueTags.size()+1).setCheckable(true);
      //  menu.getItem(uniqueTags.size()+1).setChecked(true);
        return super.onCreateOptionsMenu(menu);
    }

    private void sendFilterToFragment(){
        //final ScheduleRecyclerFragment frag = (ScheduleRecyclerFragment)getFragmentManager().findFragmentById(R.id.fragmentItemsList);

        //TODO id send here



        filterInterface = newFragment.getFilterInterface();
        filterInterface.onItemsFiltered(filteredTagNames);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.isCheckable()){
            if(item.isChecked()){
                item.setChecked(false);
                forceMenuToReopen();

                for (int i = 0; i < filteredTagNames.size(); i++) {
                    if(filteredTagNames.get(i).equals(item.getTitleCondensed()+"")){
                        filteredTagNames.remove(i);

                    }
                }
            }else{
                item.setChecked(true);
                int count = 0;
                for (int i = 0; i < filteredTagNames.size(); i++) {
                    if (filteredTagNames.get(i).equals(item.getTitleCondensed() + "")) {
                        count++;
                    }
                }
                if (count == 0){
                    filteredTagNames.add(item.getTitleCondensed() + "");
                }
                forceMenuToReopen();
            }
            sendFilterToFragment();
        }

        return super.onOptionsItemSelected(item);
    }

    private void forceMenuToReopen(){

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
                toolbar.showOverflowMenu();
            }
        }, 0);
    }
}
