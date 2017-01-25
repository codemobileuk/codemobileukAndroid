package com.codemobile.footsqueek.codemobile.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.R;
import com.codemobile.footsqueek.codemobile.activities.ScheduleDetailActivity;
import com.codemobile.footsqueek.codemobile.activities.SpeakerActivity;
import com.codemobile.footsqueek.codemobile.activities.SpeakerDetailActivity;
import com.codemobile.footsqueek.codemobile.adapters.SpeakerRecyclerAdapter;
import com.codemobile.footsqueek.codemobile.database.Speaker;
import com.codemobile.footsqueek.codemobile.interfaces.SpeakerRecyclerInterface;

import java.util.List;

import io.realm.Realm;

/**
 * Created by greg on 23/01/2017.
 */

public class SpeakerRecyclerFragment extends Fragment implements SpeakerRecyclerInterface{

    RecyclerView recyclerView;
    SpeakerRecyclerAdapter speakerRecyclerAdapter;
    SpeakerDetailFragment speakerDetailFragment;
    FragmentActivity mContext;

    @Override
    public void speakerClicked(String speakerId) {

        Bundle data= new Bundle();
        data.putString("id",speakerId);


        if(AppDelegate.isTwoPane()){
            speakerDetailFragment = new SpeakerDetailFragment();
            speakerDetailFragment.setArguments(data);
            FragmentManager fragmentManager = mContext.getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.SpeakerDetailContainer, speakerDetailFragment);
            ft.commit();
        }else{
            Intent in = new Intent(getActivity(), SpeakerDetailActivity.class);
            in.putExtra("id",speakerId);
            startActivity(in);
        }

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_speaker,container,false);

        final List<Speaker> allSpeakers = getSpeakers();
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager glm = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(glm);
        speakerRecyclerAdapter = new SpeakerRecyclerAdapter(allSpeakers,this,mContext);
        recyclerView.setAdapter(speakerRecyclerAdapter);
        speakerRecyclerAdapter.notifyDataSetChanged();

        return view;
    }

    private List<Speaker> getSpeakers(){
        Realm realm = AppDelegate.getRealmInstance();
        return realm.where(Speaker.class).findAll();

    }

    @Override
    public void onAttach(Context context) {
        mContext = (SpeakerActivity)context;
        super.onAttach(context);
    }
}
