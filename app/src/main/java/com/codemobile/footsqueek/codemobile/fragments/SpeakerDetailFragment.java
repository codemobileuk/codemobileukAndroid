package com.codemobile.footsqueek.codemobile.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.R;
import com.codemobile.footsqueek.codemobile.database.Session;
import com.codemobile.footsqueek.codemobile.database.Speaker;
import com.codemobile.footsqueek.codemobile.services.CircleCroppedBitmap;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import io.realm.Realm;

/**
 * Created by greg on 23/01/2017.
 */

public class SpeakerDetailFragment extends Fragment {

    TextView nameTv,talkTv,bioTv;
    ImageView imageView , twitter, facebook;
    String speakerId = "-1";
    Context mContext;
    String twitterTag = "";


    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_speaker_detail,container,false);

        Bundle extras = getArguments();
        speakerId = extras.getString("id");

        nameTv = (TextView)view.findViewById(R.id.speakerName);
        talkTv = (TextView)view.findViewById(R.id.speakerTalk);
        bioTv = (TextView)view.findViewById(R.id.speakerBio);
        imageView = (ImageView)view.findViewById(R.id.speakerImage);
        twitter = (ImageView)view.findViewById(R.id.twitter);
        facebook = (ImageView)view.findViewById(R.id.facebook);

        setViews();
        Animation expand = AnimationUtils.loadAnimation(mContext, R.anim.expand);
        nameTv.startAnimation(expand);
        talkTv.startAnimation(expand);



        if(AppDelegate.isTwoPane()){
            Animation enterLeft = AnimationUtils.loadAnimation(mContext, R.anim.move_left_to_position);
            imageView.startAnimation(enterLeft);
         //   bioTv.startAnimation(enterLeft);

            TranslateAnimation translateAnimation = new TranslateAnimation(0f,0f,2000f,0f);
            translateAnimation.setDuration(300);
            translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            bioTv.setAnimation(translateAnimation);

        }else{
            TranslateAnimation translateAnimation = new TranslateAnimation(0f,0f,2000f,0f);
            translateAnimation.setDuration(1000);
            translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());

            Animation move = AnimationUtils.loadAnimation(mContext, R.anim.move_left_to_position);
            move.setInterpolator(new AccelerateDecelerateInterpolator());
            imageView.startAnimation(move);
            bioTv.setAnimation(translateAnimation);
        }

        setOnClickListeners();

        return view;
    }

    public void setOnClickListeners(){

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("twitter://user?screen_name=[" +twitterTag +"]"));
                    startActivity(intent);

                }catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://twitter.com/" +twitterTag +"")));
                }
            }

        });

    }
    private void setViews(){

        Speaker speaker;
        if (speakerId.equals("-1")) {
            nameTv.setText("Error getting talk ");
        } else {
            Realm realm = AppDelegate.getRealmInstance();
            speaker = realm.where(Speaker.class).equalTo("id", speakerId).findFirst();
            Session session = realm.where(Session.class).equalTo("speakerId",speakerId).findFirst();

            if(!speaker.getTwitter().equals("")){
                twitterTag = speaker.getTwitter();
            }
            setImage(speaker);

            nameTv.setText(speaker.getFirstname());
            if(session != null){
                talkTv.setText(session.getTitle());
            }else{
                talkTv.setText("No talk");
            }

            bioTv.setText(speaker.getProfile());
        }


    }

    private void setImage(Speaker speaker){

        Picasso.with(mContext)
                .load(speaker.getPhotoUrl()).fit().centerCrop()
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                        CircleCroppedBitmap circleCroppedBitmap = new CircleCroppedBitmap(imageBitmap, mContext);
                        circleCroppedBitmap.createRoundImage(imageView);
                    }

                    @Override
                    public void onError() {

                    }
                });

    }

    public void exitAnimation(){
        Animation exitRight = AnimationUtils.loadAnimation(mContext, R.anim.move_from_position_to_right);
        Animation collapse = AnimationUtils.loadAnimation(mContext, R.anim.collapse);
       // move.setInterpolator(new AccelerateDecelerateInterpolator());
        imageView.startAnimation(exitRight);
        bioTv.startAnimation(exitRight);
        talkTv.setAnimation(collapse);
        nameTv.setAnimation(collapse);
    }

    @Override
    public void onPause() {
        exitAnimation();
        super.onPause();

    }
}
