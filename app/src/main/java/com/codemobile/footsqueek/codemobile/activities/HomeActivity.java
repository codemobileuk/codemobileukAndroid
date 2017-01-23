package com.codemobile.footsqueek.codemobile.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.R;
import com.squareup.picasso.Picasso;

import java.util.zip.Inflater;

/**
 * Created by greg on 20/01/2017.
 */

public class HomeActivity extends AppCompatActivity {

    TextView speakerOneTv, speakerTwoTv, buildingOneTv, buildingTwoTv,animTv,animTv2;
    ImageView speakerOneImage, speakerTwoImage;
    ConstraintLayout scheduleButton, locationButton;
    Context context;
    LinearLayout ll;

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


        setOnClickListeners();
        loadImages();


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

            }
        });

    }
}
