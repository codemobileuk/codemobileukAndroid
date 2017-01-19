package com.codemobile.footsqueek.codemobile.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Adapter;

import com.codemobile.footsqueek.codemobile.TimeConverter;
import com.codemobile.footsqueek.codemobile.adapters.ScheduleRecyclerAdapter;
import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.database.Session;
import com.codemobile.footsqueek.codemobile.fetcher.Fetcher;
import com.codemobile.footsqueek.codemobile.interfaces.FetcherInterface;
import com.codemobile.footsqueek.codemobile.interfaces.ScheduleRecyclerInterface;
import com.codemobile.footsqueek.codemobile.R;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.ButterKnife;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements ScheduleRecyclerInterface {

   // @BindView(R.id.tealRecycler)
    RecyclerView tealRecyclerView;
    //@BindView(R.id.cyanRecycler)

    RecyclerView cyanRecyclerView;
    ScheduleRecyclerAdapter tealAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        generateDummySchedule();
        fetchSchedule();


        final List<Session> allTalks = getSchedule();

        tealRecyclerView = (RecyclerView)findViewById(R.id.tealRecycler);
        tealRecyclerView.setHasFixedSize(true);
        GridLayoutManager glm = new GridLayoutManager(getApplicationContext(),2);
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
        tealAdapter = new ScheduleRecyclerAdapter(allTalks,this);
        tealRecyclerView.setAdapter(tealAdapter);
        tealAdapter.notifyDataSetChanged();



    }
//not sure weather it is necessary to let the first task finish before starting the next. Theoretically it would help lower end
    // devices but will take slightly longer to get all the data.
    public void fetchSchedule(){

        final Fetcher fetcher= new Fetcher();
        fetcher.setFetcherInterface(new FetcherInterface() {

            @Override
            public void onComplete() {
                fetchSpeakers();
                tealAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {

            }

            @Override
            public void onProgress() {

            }
        });
        fetcher.execute("Schedule");

    }
    public void fetchSpeakers(){

        final Fetcher fetcher= new Fetcher();
        fetcher.setFetcherInterface(new FetcherInterface() {

            @Override
            public void onComplete() {
                fetchLocations();
            }

            @Override
            public void onError() {

            }

            @Override
            public void onProgress() {

            }
        });
        fetcher.execute("Speakers");

    }
    public void fetchLocations(){

        final Fetcher fetcher= new Fetcher();
        fetcher.setFetcherInterface(new FetcherInterface() {

            @Override
            public void onComplete() {

            }

            @Override
            public void onError() {

            }

            @Override
            public void onProgress() {

            }
        });
        fetcher.execute("Locations");

    }



    public void talkClicked(String scheduleId) {
         Intent in = new Intent(MainActivity.this, TalkActivity.class);
         in.putExtra("id",scheduleId);
         startActivity(in);
    }


    public List<Session> getSchedule(){

        //todo filter by room
        Realm realm = AppDelegate.getRealmInstance();

        List <Session> allTalks = realm.where(Session.class).findAllSorted("timeStart");
        Session tempTalk = null;

        realm.beginTransaction();
        for(Session talk :allTalks){

        if(tempTalk !=null){
            if(tempTalk.getTimeStart().equals(talk.getTimeStart())){
                talk.setDoubleRow(true);
                tempTalk.setDoubleRow(true);
            }
        }
            tempTalk = talk;
        }
        realm.commitTransaction();

        return allTalks;
    }


    public void generateDummySchedule(){

 /*       Realm realm = AppDelegate.getRealmInstance();

        realm.beginTransaction();
        realm.delete(Session.class);
        realm.commitTransaction();

        realm.beginTransaction();

        Session talk1 = new Session();
        talk1.setTimeStart(new GregorianCalendar(2017,17,4,10,30).getTime());
        talk1.setTimeEnd(new GregorianCalendar(2017,17,4,11,30).getTime());
        talk1.setSpeaker("Single Row");
        talk1.setDesc("A rendition of silence");
        talk1.setTitle("Life of a silent protagonist");
        talk1.setId(1);

        Session talk2 = new Session();
        talk2.setTimeStart(new GregorianCalendar(2017,17,4,11,40).getTime());
        talk2.setTimeEnd(new GregorianCalendar(2017,17,4,12,20).getTime());
        talk2.setSpeaker("Single row 2");
        talk2.setDesc("Frodo was a pain");
        talk2.setTitle("Life of a silent protagonist");
        talk2.setId(2);

        Session talk3 = new Session();
        talk3.setTimeStart(new GregorianCalendar(2017,17,4,12,30).getTime());
        talk3.setTimeEnd(new GregorianCalendar(2017,17,4,14,0).getTime());
        talk3.setSpeaker("double row");
        talk3.setDesc("mphhhh mmmhppp");
        talk3.setTitle("Life of a silent protagonist");
        talk3.setId(3);

        Session talk4 = new Session();
        talk4.setTimeStart(new GregorianCalendar(2017,17,4,15,10).getTime());
        talk4.setTimeEnd(new GregorianCalendar(2017,17,4,14,50).getTime());
        talk4.setSpeaker("double row part 2");
        talk4.setDesc("A rendition of silence");
        talk4.setTitle("Life of a silent protagonist");
        talk4.setId(4);

        Session talk5 = new Session();
        talk5.setTimeStart(new GregorianCalendar(2017,17,4,15,0).getTime());
        talk5.setTimeEnd(new GregorianCalendar(2017,17,4,15,30).getTime());
        talk5.setSpeaker("single row");
        talk5.setDesc("A rendition of silence");
        talk5.setTitle("Life of a silent protagonist");
        talk5.setId(5);

        Session talk6 = new Session();
        talk6.setTimeStart(new GregorianCalendar(2017,17,4,15,0).getTime());
        talk6.setTimeEnd(new GregorianCalendar(2017,17,4,15,30).getTime());
        talk6.setSpeaker("double row");
        talk6.setDesc("A rendition of silence");
        talk6.setTitle("Life of a silent protagonist");
        talk6.setId(6);

        Session talk7 = new Session();
        talk7.setTimeStart(new GregorianCalendar(2017,17,4,16,0).getTime());
        talk7.setTimeEnd(new GregorianCalendar(2017,17,4,16,30).getTime());
        talk7.setSpeaker("double row");
        talk7.setDesc("A rendition of silence");
        talk7.setTitle("Life of a silent protagonist");
        talk7.setId(7);

        realm.copyToRealm(talk1);
        realm.copyToRealm(talk2);
        realm.copyToRealm(talk3);
        realm.copyToRealm(talk4);
        realm.copyToRealm(talk5);
        realm.copyToRealm(talk6);
        realm.copyToRealm(talk7);


        realm.commitTransaction();
        realm.close();*/

        }
}
