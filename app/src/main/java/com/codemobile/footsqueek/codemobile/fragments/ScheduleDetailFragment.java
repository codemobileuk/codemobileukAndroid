package com.codemobile.footsqueek.codemobile.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.R;
import com.codemobile.footsqueek.codemobile.customUi.LineButton;
import com.codemobile.footsqueek.codemobile.database.RealmUtility;
import com.codemobile.footsqueek.codemobile.database.Session;
import com.codemobile.footsqueek.codemobile.database.SessionFavorite;
import com.codemobile.footsqueek.codemobile.database.Speaker;
import com.codemobile.footsqueek.codemobile.database.Tag;
import com.codemobile.footsqueek.codemobile.services.CircleCroppedBitmap;
import com.codemobile.footsqueek.codemobile.services.TimeManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by greg on 19/01/2017.
 */

public class ScheduleDetailFragment extends Fragment {

    TextView title, speakerTv, timeStart, speakerOrg, talkDesc, buildingName, speakerDesc;
    ImageView speakerImg, buildingIcon, twitter, favorite;
    String talkId ="-1";
    Session session;
    Speaker speaker;
    Context mContext;
    LineButton btnProfile, btnTalk;
    List<Tag> tags;
    LinearLayout tagLL, talksLL;
    String twitterTag = "";
  //  boolean isFavorite;
    SessionFavorite sessionFavorite;

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_schedule_detail,container,false);
        Bundle extras = getArguments();
        talkId = extras.getString("id");

        Realm realm = AppDelegate.getRealmInstance();

        session = realm.where(Session.class).equalTo("id", talkId).findFirst();
        if (session != null){
            speaker = realm.where(Speaker.class).equalTo("id", session.getSpeakerId()).findFirst();
            tags = realm.where(Tag.class).equalTo("sessionId",session.getId()).findAll();
        }




        title = (TextView)view.findViewById(R.id.title);
        speakerTv = (TextView)view.findViewById(R.id.speaker);
        timeStart = (TextView)view.findViewById(R.id.timeStart);
        speakerImg = (ImageView)view.findViewById(R.id.speakerImg);
        buildingIcon = (ImageView)view.findViewById(R.id.buildingIcon);
        buildingName = (TextView)view.findViewById(R.id.buildingName);
        speakerOrg = (TextView)view.findViewById(R.id.organisation);
        talkDesc = (TextView)view.findViewById(R.id.talk_desc);
        speakerDesc = (TextView)view.findViewById(R.id.speaker_desc);
        tagLL = (LinearLayout)view.findViewById(R.id.tagsLL);
        talksLL = (LinearLayout)view.findViewById(R.id.talksLL);
        btnProfile = (LineButton)view.findViewById(R.id.btn_profile);
        btnTalk = (LineButton)view.findViewById(R.id.btn_talk);
        twitter = (ImageView)view.findViewById(R.id.twitter);
        favorite = (ImageView)view.findViewById(R.id.favorite_btn);

        addTags();
        setImage();
        setTextViews();
        setOnClickListeners();

        List<LineButton> buttons = new ArrayList<>();
        buttons.add(btnProfile);
        buttons.add(btnTalk);

        if(speaker != null){
            if(!speaker.getTwitter().equals("")){
                twitterTag = speaker.getTwitter();
            }
        }


        if(session != null){
            sessionFavorite = realm.where(SessionFavorite.class).equalTo("sessionId",session.getId()).findFirst();
            if(sessionFavorite != null){
                if(sessionFavorite.getFavorite()){
                    favorite.setBackground(ContextCompat.getDrawable(mContext,R.drawable.favorite));
                  //  sessionFavorite = new SessionFavorite(session.getId(),"",true);
                }else{
                    favorite.setBackground(ContextCompat.getDrawable(mContext,R.drawable.favorite_not_selected));
                   // sessionFavorite = new SessionFavorite(session.getId(),"",false);
                }
            }else{
                sessionFavorite = new SessionFavorite(session.getId(),"",false);
                RealmUtility.addNewRow(sessionFavorite);
            }

        }

        btnProfile.setTogglePartners(buttons, true);
        btnTalk.setTogglePartners(buttons, false);
        return view;
    }

    public void openWebPage(String url){


       // Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
      //  startActivity(intent);

    }

    private void setOnClickListeners(){
        btnTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakerDesc.setVisibility(View.VISIBLE);
                talksLL.setVisibility(View.GONE);
                btnTalk.customClick();
            }
        });
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakerDesc.setVisibility(View.GONE);
                talksLL.setVisibility(View.VISIBLE);
                btnProfile.customClick();
            }
        });

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
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Realm realm = AppDelegate.getRealmInstance();
                sessionFavorite = realm.where(SessionFavorite.class).equalTo("sessionId",session.getId()).findFirst();


                Animation spinIn = AnimationUtils.loadAnimation(mContext,R.anim.spin);
                final Animation spinOut = AnimationUtils.loadAnimation(mContext,R.anim.after_spin);


                favorite.startAnimation(spinIn);
                spinIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if(sessionFavorite != null) {
                            if(sessionFavorite.getFavorite()){
                                favorite.setBackground(ContextCompat.getDrawable(mContext,R.drawable.favorite_not_selected));
                                sessionFavorite = new SessionFavorite(session.getId(),"",false);
                                //  isFavorite = false;

                            }else{
                                favorite.setBackground(ContextCompat.getDrawable(mContext,R.drawable.favorite));
                                sessionFavorite = new SessionFavorite(session.getId(),"",true);
                                //  isFavorite = true;
                            }

                            RealmUtility.addNewRow(sessionFavorite);
                        }else{
                            favorite.setBackground(ContextCompat.getDrawable(mContext,R.drawable.favorite));
                            sessionFavorite = new SessionFavorite(session.getId(), "", true);
                        }
                        favorite.startAnimation(spinOut);
                        RealmUtility.addNewRow(sessionFavorite);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });


            }
        });


    }

    private void setImage(){


        if(speaker != null){
            Picasso.with(mContext)
                    .load(speaker.getPhotoUrl()).fit().centerCrop()
                    .into(speakerImg, new Callback() {
                        @Override
                        public void onSuccess() {
                            Bitmap imageBitmap = ((BitmapDrawable) speakerImg.getDrawable()).getBitmap();
                            CircleCroppedBitmap circleCroppedBitmap = new CircleCroppedBitmap(imageBitmap, mContext);
                            circleCroppedBitmap.createRoundImage(speakerImg);
                        }

                        @Override
                        public void onError() {

                            Log.d ("forum",speaker.getPhotoUrl()+" ===_+_+_=");
                        }
                    });


            if(session.getLocationName().equals("Molloy")){
                buildingIcon.setImageResource(R.drawable.ic_molloy);

            }else{
                buildingIcon.setImageResource(R.drawable.ic_beswick);
            }
        }

        if(session != null){
            buildingName.setText(session.getLocationName());
        }

    }

    private void addTags(){


        if(tags != null){
            for (int i = 0; i < tags.size(); i++) {
                final TextView tagTextView = new TextView(mContext);
                tagTextView.setText(tags.get(i).getTag());
                tagTextView.setTextSize(10);
                tagTextView.setBackgroundResource(R.drawable.rounded_text_box);
                tagTextView.setPadding(12,12,12,12);
                LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                llp.setMargins(4,4,4,4);
                tagTextView.setLayoutParams(llp);
                tagLL.addView(tagTextView);

            }
        }


    }

    private void setTextViews() {


            if (talkId.equals("-1")) {
                title.setText("Error getting talk ");
            } else {



                if(session != null){
                    speakerTv.setText(speaker.getFirstname() +" " +speaker.getSurname());
                    speakerOrg.setText(speaker.getOrganisation());
                    title.setText(session.getTitle());
                    talkDesc.setText(session.getDesc());
                    timeStart.setText(TimeManager.trimTimeFromDate(session.getTimeStart()) +" - " + TimeManager.trimTimeFromDate(session.getTimeEnd()));
                    speakerDesc.setText(speaker.getProfile());
                    speakerDesc.setMovementMethod(new ScrollingMovementMethod());
                }

            }
        }


}
