package com.codemobile.footsqueek.codemobile.customUi;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.R;
import com.codemobile.footsqueek.codemobile.activities.ScheduleActivity;
import com.codemobile.footsqueek.codemobile.database.RealmUtility;
import com.codemobile.footsqueek.codemobile.database.Session;
import com.codemobile.footsqueek.codemobile.database.Speaker;
import com.codemobile.footsqueek.codemobile.services.RoundedCornersTransform;
import com.codemobile.footsqueek.codemobile.services.TimeManager;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

import io.realm.Realm;

/**
 * Created by greg on 22/03/2017.
 */

    public class DoublePreviewView extends LinearLayout{

    TextView timestartTv1, talkTv1, speakerTv1, timestartTv2, talkTv2, speakerTv2;
    ImageView mainImage1, mainImage2;
    Date currentDate;
    Session talk1, talk2;
    Speaker speaker1, speaker2;
    Context context;
    FrameLayout frame1, frame2;

    public DoublePreviewView(Context context) {
        super(context);
        init(context);
    }

    public DoublePreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DoublePreviewView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public DoublePreviewView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context){

        View view = inflate(context, R.layout.preview_view_double, null);
        addView(view);

        this.context = context;

        timestartTv1 = (TextView)findViewById(R.id.timeStartTv1);
        talkTv1 = (TextView)findViewById(R.id.sessionTv1);
        speakerTv1 = (TextView)findViewById(R.id.speakerTv1);
        mainImage1 = (ImageView)findViewById(R.id.image1);
        timestartTv2 = (TextView)findViewById(R.id.timeStartTv2);
        talkTv2 = (TextView)findViewById(R.id.sessionTv2);
        speakerTv2 = (TextView)findViewById(R.id.speakerTv2);
        mainImage2 = (ImageView)findViewById(R.id.image2);
        frame1 = (FrameLayout)findViewById(R.id.frame1);
        frame2 = (FrameLayout)findViewById(R.id.frame2);
        setOnClickListeners();

    }

    public void loadViews(){

        Realm realm = AppDelegate.getRealmInstance();
        getCurrentTalk();
        //  Speaker speaker1 = null;
        if(talk1 !=null && talk2 != null){
            speaker1 = realm.where(Speaker.class).equalTo("id", talk1.getSpeakerId()).findFirst();
            speaker2 = realm.where(Speaker.class).equalTo("id", talk2.getSpeakerId()).findFirst();
            loadImages(talk1,mainImage1,context);
            loadImages(talk2,mainImage2,context);
            talkTv1.setText(talk1.getTitle());
            talkTv2.setText(talk2.getTitle());
            if(speaker1 != null && speaker2 != null){
                speakerTv1.setText(speaker1.getFirstname()+ " " +speaker1.getSurname());
                speakerTv2.setText(speaker2.getFirstname()+ " " +speaker2.getSurname());
            }

            timestartTv1.setText(TimeManager.trimTimeFromDate(talk1.getTimeStart()));
            timestartTv2.setText(TimeManager.trimTimeFromDate(talk2.getTimeStart()));
        }
    }

    public void loadImages(Session currentTalk, ImageView view, Context context){

        Realm realm = AppDelegate.getRealmInstance();

        Speaker speaker1 = realm.where(Speaker.class).equalTo("id", currentTalk.getSpeakerId()).findFirst();

        if(speaker1 != null){
            Picasso.with(context)
                    .load(speaker1.getPhotoUrl())
                    .fit()
                    .centerCrop()
                    .transform(new RoundedCornersTransform())
                    .into(view);
        }


    }

    public void getCurrentTalk(){


        List<Session> all = RealmUtility.getFutureSessions();

        if(all.size()>1){
            talk1 = all.get(0);
            talk2 = all.get(1);
            
        }

    }

    public void setOnClickListeners(){

        frame1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context,ScheduleActivity.class);
                if(talk1 != null){
                    in.putExtra("id",talk1.getId());
                }

                context.startActivity(in);
            }
        });

        frame2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context,ScheduleActivity.class);
                if(talk2 != null){
                    in.putExtra("id",talk2.getId());
                }
                context.startActivity(in);
            }
        });

    }
}
