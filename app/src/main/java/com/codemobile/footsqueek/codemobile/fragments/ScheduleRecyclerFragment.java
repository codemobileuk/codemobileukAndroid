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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.R;
import com.codemobile.footsqueek.codemobile.activities.ScheduleActivity;
import com.codemobile.footsqueek.codemobile.activities.ScheduleDetailActivity;
import com.codemobile.footsqueek.codemobile.adapters.ScheduleRecyclerAdapter;
import com.codemobile.footsqueek.codemobile.database.RealmUtility;
import com.codemobile.footsqueek.codemobile.database.ScheduleRowType;
import com.codemobile.footsqueek.codemobile.database.Session;
import com.codemobile.footsqueek.codemobile.database.SessionFullData;
import com.codemobile.footsqueek.codemobile.database.Tag;
import com.codemobile.footsqueek.codemobile.interfaces.ScheduleDayChooserInterface;
import com.codemobile.footsqueek.codemobile.interfaces.ScheduleFilterInterface;
import com.codemobile.footsqueek.codemobile.interfaces.ScheduleRecyclerInterface;
import com.codemobile.footsqueek.codemobile.services.TimeManager;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import io.realm.Realm;
import io.realm.Sort;

/**
 * Created by greg on 19/01/2017.
 */

public class ScheduleRecyclerFragment extends Fragment implements ScheduleRecyclerInterface{

    RecyclerView tealRecyclerView;
    ScheduleRecyclerAdapter tealAdapter;
    ScheduleDetailFragment scheduleDetailFragment;
    ScheduleFilterInterface filterInterface;
    FragmentActivity mContext;

    final static int DAY_ONE = 0;
    final static int DAY_TWO = 1;
    final static int DAY_THREE = 2;

    private int selectedDay= DAY_ONE;

    private List<String> filterTagNames = new ArrayList<>();
    private List <SessionFullData> sfd = new ArrayList<>();

    ScheduleDayChooserInterface mListener;

    public ScheduleDayChooserInterface getScheduleDayChooserInterface(){
        return mListener;
    }

    public ScheduleFilterInterface getFilterInterface(){
        return filterInterface;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_schedule,container,false);

        tealRecyclerView = (RecyclerView)view.findViewById(R.id.tealRecycler);
        tealRecyclerView.setHasFixedSize(true);

        initFilter();
        setupRecycler(createScheduleWithHeaders(18));
        daySelectorListener();

        if(AppDelegate.isTwoPane()){
            talkClicked(getFirstRowSessionId());
        }

        return view;
    }

    public void daySelectorListener(){
        mListener = new ScheduleDayChooserInterface() {
            @Override
            public void dayOne() {
                selectedDay = DAY_ONE;
                setupRecycler(createScheduleWithHeaders(18));
                if(AppDelegate.isTwoPane()){
                    talkClicked(getFirstRowSessionId());
                }

            }

            @Override
            public void dayTwo() {
                selectedDay = DAY_TWO;
                setupRecycler(createScheduleWithHeaders(19));
                if(AppDelegate.isTwoPane()){
                    talkClicked(getFirstRowSessionId());
                }
            }

            @Override
            public void dayThree() {
                selectedDay = DAY_THREE;
                setupRecycler(createScheduleWithHeaders(20));
                if(AppDelegate.isTwoPane()){
                    talkClicked(getFirstRowSessionId());
                }
            }
        };
    }
    public void initFilter(){
        filterTagNames.clear();
        List<Tag> tags = RealmUtility.getUniqueTags();
        for (int i = 0; i < tags.size(); i++) {
            filterTagNames.add(tags.get(i).getTag());
        }

        filterInterface = new ScheduleFilterInterface() {
            @Override
            public void onItemsFiltered(List<String> tagNames) {
                filterTagNames = tagNames;
                if(selectedDay == DAY_ONE){
                    setupRecycler(createScheduleWithHeaders(18));
                }else if(selectedDay == DAY_TWO){
                    setupRecycler(createScheduleWithHeaders(19));
                }else if(selectedDay == DAY_THREE){
                    setupRecycler(createScheduleWithHeaders(20));
                }
                if(AppDelegate.isTwoPane()){
                    talkClicked(getFirstRowSessionId());
                }
            }
        };
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = this.getArguments();
        if(bundle !=null){
            String id = bundle.getString("id", "-1");
            if(!id.equals("-1")){
                talkClicked(id);
            }
        }
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


    private String[] getFilteredSessionIds(){
        Realm realm = AppDelegate.getRealmInstance();
        List<String> sessionsIds = new ArrayList<>();

        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < filterTagNames.size(); i++) {

            tags = realm.where(Tag.class).equalTo("tag",filterTagNames.get(i)).findAll();
            for (int t = 0; t < tags.size(); t++) {
                sessionsIds.add(tags.get(t).getSessionId());

            }

        }
        String [] filteredSessionIds = new String[sessionsIds.size()];

        return filteredSessionIds = sessionsIds.toArray(filteredSessionIds);
    }

    public List<SessionFullData> createScheduleWithHeaders(int day){
        Realm realm = AppDelegate.getRealmInstance();

        Calendar cal = Calendar.getInstance();
        cal.set(2017,3,day,0,0,0);
        Date startOfDay = cal.getTime();
        cal.set(2017,3,day,23,59,59);
        Date endOfDay = cal.getTime();
        List <Session> allTalks = new ArrayList<>();
        sfd.clear();
        if(getFilteredSessionIds().length !=0){
            allTalks = realm.where(Session.class).between("timeStart", startOfDay, endOfDay).in("id",getFilteredSessionIds()).findAllSorted("timeStart", Sort.ASCENDING, "locationName", Sort.DESCENDING);
        }
        for (int i = 0; i < allTalks.size(); i++) {
            if(i != allTalks.size()-1 && i !=0){//All cases where i isnt the last of first
                if(allTalks.get(i).getTimeStart().equals(allTalks.get(i+1).getTimeStart())){//left bound item
                    //add header and one talk
                    scheduleHeader(allTalks.get(i));
                    scheduleLeftCol(allTalks.get(i));

                }else if(allTalks.get(i).getTimeStart().equals(allTalks.get(i-1).getTimeStart())) {//right bound item
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
                TimeManager.trimTimeFromDate(session.getTimeStart())+ " - " + TimeManager.trimTimeFromDate(session.getTimeEnd()),
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
    @Override
    public void onResume() {
        super.onResume();
        tealAdapter.notifyDataSetChanged();

    }

}
