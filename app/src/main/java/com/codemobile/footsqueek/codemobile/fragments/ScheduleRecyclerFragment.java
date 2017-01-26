package com.codemobile.footsqueek.codemobile.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.codemobile.footsqueek.codemobile.activities.ScheduleActivity;
import com.codemobile.footsqueek.codemobile.activities.ScheduleDetailActivity;
import com.codemobile.footsqueek.codemobile.adapters.ScheduleRecyclerAdapter;
import com.codemobile.footsqueek.codemobile.database.Session;
import com.codemobile.footsqueek.codemobile.fetcher.Fetcher;
import com.codemobile.footsqueek.codemobile.interfaces.FetcherInterface;
import com.codemobile.footsqueek.codemobile.interfaces.ScheduleRecyclerInterface;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.realm.Realm;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by greg on 19/01/2017.
 */

public class ScheduleRecyclerFragment extends Fragment implements ScheduleRecyclerInterface{

    RecyclerView tealRecyclerView;
    ScheduleRecyclerAdapter tealAdapter;
    ScheduleDetailFragment scheduleDetailFragment;
    FragmentActivity mContext;

    @Override
    public void onAttach(Context context) {
        mContext=(ScheduleActivity)context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_schedule,container,false);


        final List<Session> allTalks = getSchedule();


        tealRecyclerView = (RecyclerView)view.findViewById(R.id.tealRecycler);
        tealRecyclerView.setHasFixedSize(true);
        GridLayoutManager glm = new GridLayoutManager(getActivity(),2);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(allTalks.get(position).isDoubleRow()){
                    return 1;
                }else{
                    return 2;
                }

            }
        });

        tealRecyclerView.setLayoutManager(glm);
        tealAdapter = new ScheduleRecyclerAdapter(allTalks,this,mContext);
        tealRecyclerView.setAdapter(tealAdapter);
        tealAdapter.notifyDataSetChanged();
        return view;
    }


    public void talkClicked(String scheduleId) {
        Bundle data= new Bundle();
        data.putString("id",scheduleId);


        if(AppDelegate.isTwoPane()){
            scheduleDetailFragment = new ScheduleDetailFragment();
            scheduleDetailFragment.setArguments(data);
            FragmentManager fragmentManager = mContext.getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.ScheduleDetailContainer, scheduleDetailFragment);
            ft.commit();
        }else{
            Intent in = new Intent(getActivity(), ScheduleDetailActivity.class);
            in.putExtra("id",scheduleId);
            startActivity(in);
        }

    }


    public List<Session> getSchedule(){
        //returns all the speakers filtered on date then updates the data to allow for a concept
        //of double rows.
        //todo filter by room
        Realm realm = AppDelegate.getRealmInstance();

        return realm.where(Session.class).findAllSorted("timeStart");
    }


}
