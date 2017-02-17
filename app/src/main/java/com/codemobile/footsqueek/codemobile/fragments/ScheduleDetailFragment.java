package com.codemobile.footsqueek.codemobile.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.R;
import com.codemobile.footsqueek.codemobile.database.Session;

import io.realm.Realm;

/**
 * Created by greg on 19/01/2017.
 */

public class ScheduleDetailFragment extends Fragment {

    TextView title, speaker, timeStart;
    String talkId ="-1";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.schedule_item_layout,container,false);
        Bundle extras = getArguments();
        talkId = extras.getString("id");


        title = (TextView)view.findViewById(R.id.title);
        speaker = (TextView)view.findViewById(R.id.speaker);
        timeStart = (TextView)view.findViewById(R.id.timeStart);


        setTextViews();
        return view;
    }

    private void setTextViews() {

            Session session;
            if (talkId.equals("-1")) {
                title.setText("Error getting talk ");
            } else {
                Realm realm = AppDelegate.getRealmInstance();
                session = realm.where(Session.class).equalTo("id", talkId).findFirst();


                if(session != null){
                    title.setText(session.getTitle());
                    timeStart.setText(session.getTimeStart().toString());
                }

            }
        }

    }
