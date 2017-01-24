package com.codemobile.footsqueek.codemobile.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.R;
import com.codemobile.footsqueek.codemobile.database.Session;
import com.codemobile.footsqueek.codemobile.database.Speaker;

import io.realm.Realm;

/**
 * Created by greg on 23/01/2017.
 */

public class SpeakerDetailFragment extends Fragment {

    TextView nameTv,talkTv,bioTv;
    String speakerId = "-1";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.speaker_item_layout,container,false);

        Bundle extras = getArguments();
        speakerId = extras.getString("id");

        nameTv = (TextView)view.findViewById(R.id.speakerName);
        talkTv = (TextView)view.findViewById(R.id.speakerTalk);
        bioTv = (TextView)view.findViewById(R.id.speakerBio);

        setTextViews();

        return view;
    }

    private void setTextViews(){

        Speaker speaker;
        if (speakerId.equals("-1")) {
            nameTv.setText("Error getting talk ");
        } else {
            Realm realm = AppDelegate.getRealmInstance();
            speaker = realm.where(Speaker.class).equalTo("id", speakerId).findFirst();


            nameTv.setText(speaker.getFirstname());
        }
    }
}
