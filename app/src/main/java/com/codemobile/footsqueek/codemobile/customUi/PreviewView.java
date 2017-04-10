package com.codemobile.footsqueek.codemobile.customUi;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import io.realm.Realm;

/**
 * Created by greg on 22/03/2017.
 */

public class PreviewView extends LinearLayout {

    TextView timeStartTv, talkTv, speakerTv;
    ImageView mainImage;
    Context context;
    Session talk;
    Date currentDate;
    RelativeLayout rl1;

    public PreviewView(Context context) {
        super(context);
        init(context);
    }

    public PreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PreviewView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public PreviewView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context){
     //   currentDate = new Date();
      //  currentDate.getTime();
        this.context = context;
        View view = inflate(context, R.layout.preview_view_single, null);
        addView(view);

        timeStartTv = (TextView)findViewById(R.id.timeStartTv);
        talkTv = (TextView)findViewById(R.id.sessionTv);
        speakerTv = (TextView)findViewById(R.id.speakerTv);
        mainImage = (ImageView)findViewById(R.id.imageView);
        rl1 = (RelativeLayout)findViewById(R.id.rl1);


        setOnClickListeners();


    }

    public void loadViews(){
        Realm realm = AppDelegate.getRealmInstance();

        talk = RealmUtility.getUpcomingSession();
        if(talk != null){
            Speaker speaker1 = realm.where(Speaker.class).equalTo("id", talk.getSpeakerId()).findFirst();
            loadImages(talk,mainImage, context);
            if(speaker1 !=null){
                speakerTv.setText(speaker1.getFirstname()+ " " +speaker1.getSurname());
            }
                talkTv.setText(talk.getTitle());
                timeStartTv.setText(TimeManager.trimTimeFromDate(talk.getTimeStart()));
        }

    }

    private void loadImages(Session currentTalk, ImageView view, Context context){

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

    private void setOnClickListeners(){
        rl1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context,ScheduleActivity.class);
                in.putExtra("id",talk.getId());
                context.startActivity(in);
            }
        });
    }

}
