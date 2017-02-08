package com.codemobile.footsqueek.codemobile.fragments;

import android.app.Activity;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.R;
import com.codemobile.footsqueek.codemobile.activities.ScheduleActivity;
import com.codemobile.footsqueek.codemobile.activities.ScheduleDetailActivity;
import com.codemobile.footsqueek.codemobile.adapters.ScheduleRecyclerAdapter;
import com.codemobile.footsqueek.codemobile.database.ScheduleRowType;
import com.codemobile.footsqueek.codemobile.database.Session;
import com.codemobile.footsqueek.codemobile.database.SessionFullData;
import com.codemobile.footsqueek.codemobile.fetcher.Fetcher;
import com.codemobile.footsqueek.codemobile.interfaces.FetcherInterface;
import com.codemobile.footsqueek.codemobile.interfaces.ScheduleRecyclerInterface;
import com.codemobile.footsqueek.codemobile.services.TimeConverter;

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
    private List <SessionFullData> sfd = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        mContext=(ScheduleActivity)context;
        super.onAttach(context);
    }

    @Override
    public void onAttach(Activity activity) {
        mContext=(ScheduleActivity)activity;
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_schedule,container,false);

        tealRecyclerView = (RecyclerView)view.findViewById(R.id.tealRecycler);
        tealRecyclerView.setHasFixedSize(true);
        GridLayoutManager glm = new GridLayoutManager(getActivity(),2);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(sfd.get(position).getRowType() == ScheduleRowType.DOUBLE_LEFT || sfd.get(position).getRowType() == ScheduleRowType.DOUBLE_RIGHT ){
                    return 1;
                }else{
                    return 2;
                }

            }
        });


        tealRecyclerView.setLayoutManager(glm);
        tealAdapter = new ScheduleRecyclerAdapter(createScheduleWithHeaders(),this,mContext);
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

    public List<SessionFullData> createScheduleWithHeaders(){
        Realm realm = AppDelegate.getRealmInstance();

        List <Session> allTalks = realm.where(Session.class).findAllSorted("timeStart");

        for (int i = 0; i < allTalks.size(); i++) {
            if(i != allTalks.size()-1 && i !=0){//i isnt the last or first instance
                if(allTalks.get(i).getTimeStart().equals(allTalks.get(i+1).getTimeStart())){
                    //add header and one talk
                    scheduleHeader(allTalks.get(i));
                    scheduleLeftCol(allTalks.get(i));

                }else if(allTalks.get(i).getTimeStart().equals(allTalks.get(i-1).getTimeStart())) {
                    scheduleRightCol(allTalks.get(i));
                }else{
                    scheduleNormal(allTalks.get(i));
                }
            }else if(i == 0){
                if(allTalks.get(i).getTimeStart().equals(allTalks.get(i+1).getTimeStart())){
                    scheduleHeader(allTalks.get(i));
                    scheduleLeftCol(allTalks.get(i));
                }else{
                    scheduleNormal(allTalks.get(i));
                }
            }else if(i == allTalks.size()-1){
                if(allTalks.get(i).getTimeStart().equals(allTalks.get(i-1).getTimeStart())){
                    scheduleRightCol(allTalks.get(i));
                }else{
                    scheduleNormal(allTalks.get(i));
                }
            }
        }


        return sfd;

    }

    private void scheduleHeader(Session session){
        SessionFullData s = new SessionFullData(
                null,
                ScheduleRowType.DOUBLE_TIME_HEADER,
                TimeConverter.trimTimeFromDate(session.getTimeStart())+ " - " +TimeConverter.trimTimeFromDate(session.getTimeEnd()),
                session.getTitle()
        );
        sfd.add(s);
    }
    private void scheduleNormal(Session session){
        SessionFullData s = new SessionFullData(
                session,
                ScheduleRowType.NORMAL,
                null,
                null
        );
        sfd.add(s);

    }
    private void scheduleLeftCol(Session session){
        SessionFullData s = new SessionFullData(
                session,
                ScheduleRowType.DOUBLE_LEFT,
                null,
                null
        );
        sfd.add(s);
    }
    private void scheduleRightCol(Session session){
        SessionFullData s = new SessionFullData(
                session,
                ScheduleRowType.DOUBLE_RIGHT,
                null,
                null
        );

        sfd.add(s);
    }


}
