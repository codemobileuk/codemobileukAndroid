package com.codemobile.footsqueek.codemobile.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.R;
import com.codemobile.footsqueek.codemobile.customUi.LineButton;
import com.codemobile.footsqueek.codemobile.fragments.ScheduleRecyclerFragment;
import com.codemobile.footsqueek.codemobile.interfaces.ScheduleDayChooserInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by greg on 19/01/2017.
 */

public class ScheduleActivity extends LaunchActivity {

    LineButton dayOneBtn, dayTwoBtn, dayThreeBtn;

    ScheduleDayChooserInterface scheduleDayChooserInterface;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);


        dayOneBtn = (LineButton)findViewById(R.id.dayOneButton);
        dayTwoBtn = (LineButton)findViewById(R.id.dayTwoButton);
        dayThreeBtn = (LineButton)findViewById(R.id.dayThreeButton);

        determinePaneLayout();
        setupActionBar();
        navigationViewItemPosition = 1;

        final ScheduleRecyclerFragment frag = (ScheduleRecyclerFragment)getFragmentManager().findFragmentById(R.id.fragmentItemsList);

        scheduleDayChooserInterface = frag.getScheduleDayChooserInterface();
        setupButtonListeners();

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
}
