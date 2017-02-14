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
import android.widget.ImageButton;

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
import com.codemobile.footsqueek.codemobile.interfaces.ScheduleDayChooserInterface;
import com.codemobile.footsqueek.codemobile.interfaces.ScheduleRecyclerInterface;
import com.codemobile.footsqueek.codemobile.services.TimeConverter;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.realm.Realm;
import io.realm.Sort;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by greg on 19/01/2017.
 */

public class ScheduleRecyclerFragment extends Fragment implements ScheduleRecyclerInterface{

    RecyclerView tealRecyclerView;
    ScheduleRecyclerAdapter tealAdapter;
    ScheduleDetailFragment scheduleDetailFragment;
    FragmentActivity mContext;



    ScheduleDayChooserInterface mListener;

    public ScheduleDayChooserInterface getScheduleDayChooserInterface(){
        return mListener;
    }

    private List <SessionFullData> sfd = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_schedule,container,false);

        tealRecyclerView = (RecyclerView)view.findViewById(R.id.tealRecycler);

        tealRecyclerView.setHasFixedSize(true);
        setupRecycler(createScheduleWithHeaders(18));


        mListener = new ScheduleDayChooserInterface() {
            @Override
            public void dayOne() {
                Log.d("terete", "day one!!!!!!!!!!!!!");
                setupRecycler(createScheduleWithHeaders(18));
                talkClicked(getFirstRowSessionId());
            }

            @Override
            public void dayTwo() {
                setupRecycler(createScheduleWithHeaders(19));
                talkClicked(getFirstRowSessionId());
                Log.d("terete", "day 222!!!!!!!!!!!!!");
            }

            @Override
            public void dayThree() {
                setupRecycler(createScheduleWithHeaders(20));
                talkClicked(getFirstRowSessionId());
            }
        };
        setupRecycler(createScheduleWithHeaders(18));
        talkClicked(getFirstRowSessionId());

        return view;
    }
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

    public void setupRecycler(final List<SessionFullData>sessionsAndHeaders){
        GridLayoutManager glm = new GridLayoutManager(getActivity(),2);

        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(sessionsAndHeaders.get(position).getRowType() == ScheduleRowType.DOUBLE_LEFT || sessionsAndHeaders.get(position).getRowType() == ScheduleRowType.DOUBLE_RIGHT ){
                    return 1;
                }else{
                    return 2;
                }

            }
        });

        tealRecyclerView.setLayoutManager(glm);
        tealAdapter = new ScheduleRecyclerAdapter(sessionsAndHeaders,this,mContext);
        tealRecyclerView.setAdapter(tealAdapter);
        tealAdapter.notifyDataSetChanged();

    }



    private String getFirstRowSessionId(){
        for (int i = 0; i < sfd.size(); i++) {
            if(sfd.get(i).getRowType() != ScheduleRowType.DOUBLE_TIME_HEADER){
                Session session = sfd.get(i).getSession();
                return session.getId();
            }
        }
        return "";
    }

    public void firstTimeSetUp(){

        Session session =sfd.get(0).getSession();


        if(sfd != null){
            Bundle data= new Bundle();
            data.putString("id",session.getId());
            scheduleDetailFragment = new ScheduleDetailFragment();
            scheduleDetailFragment.setArguments(data);
            FragmentManager fragmentManager = mContext.getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.ScheduleDetailContainer, scheduleDetailFragment);
            ft.commit();
        }

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

    public List<SessionFullData> createScheduleWithHeaders(int day){
        Realm realm = AppDelegate.getRealmInstance();


        Calendar cal = Calendar.getInstance();
        cal.set(2017,3,day,0,0,0);
        Date startOfDay = cal.getTime();
        cal.set(2017,3,day,23,59,59);
        Date endOfDay = cal.getTime();

       // Log.d("cresinath", date+ "  =========================" + date2);

        sfd.clear();
      //  List <Session> allTalks = realm.where(Session.class).findAllSorted("timeStart", Sort.ASCENDING, "locationName", Sort.DESCENDING);
        List <Session> allTalks = realm.where(Session.class).between("timeStart", startOfDay, endOfDay).findAllSorted("timeStart", Sort.ASCENDING, "locationName", Sort.DESCENDING);

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
            }else if(i == 0 && allTalks.size() >1){
                if(allTalks.get(i).getTimeStart().equals(allTalks.get(i+1).getTimeStart())){
                    scheduleHeader(allTalks.get(i));
                    scheduleLeftCol(allTalks.get(i));
                }else{
                    scheduleNormal(allTalks.get(i));
                }
            }else if(i == allTalks.size()-1 && allTalks.size() >1){
                if(allTalks.get(i).getTimeStart().equals(allTalks.get(i-1).getTimeStart())){
                    scheduleRightCol(allTalks.get(i));
                }else{
                    scheduleNormal(allTalks.get(i));
                }
            }else{
                scheduleNormal(allTalks.get(i));
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
