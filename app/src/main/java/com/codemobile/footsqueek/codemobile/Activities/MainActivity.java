package com.codemobile.footsqueek.codemobile.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.codemobile.footsqueek.codemobile.adapters.ScheduleRecyclerAdapter;
import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.database.Schedule;
import com.codemobile.footsqueek.codemobile.interfaces.ScheduleRecyclerInterface;
import com.codemobile.footsqueek.codemobile.R;

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

        final List<Schedule> allTalks = getSchedule();

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
     @Override
    public void talkClicked(int scheduleId) {
         Intent in = new Intent(MainActivity.this, TalkActivity.class);
         in.putExtra("id",scheduleId);
         startActivity(in);
    }


    public List<Schedule> getSchedule(){

        //todo filter by room
        Realm realm = AppDelegate.getRealmInstance();

        List <Schedule> allTalks = realm.where(Schedule.class).findAllSorted("timeStart");
        Schedule tempTalk = null;

        realm.beginTransaction();
        for(Schedule talk :allTalks){

        if(tempTalk !=null){
            if(tempTalk.getTimeStart().equals(talk.getTimeStart())){
                talk.setDoubleRow(true);
                talk.setSpeaker("Pichachu");
                tempTalk.setDoubleRow(true);

                tempTalk.setSpeaker("Pichachu");
            }


        }
            tempTalk = talk;
            //realm.copyToRealmOrUpdate(talk);
        }
        realm.commitTransaction();
       // realm.close();

       // allTalks = realm.where(Schedule.class).findAllSorted("timeStart");

        return allTalks;
    }


    public void generateDummySchedule(){

        Realm realm = AppDelegate.getRealmInstance();

        realm.beginTransaction();
        realm.delete(Schedule.class);
        realm.commitTransaction();

        realm.beginTransaction();

        Schedule talk1 = new Schedule();
        talk1.setTimeStart(new GregorianCalendar(2017,17,4,10,30).getTime());
        talk1.setTimeEnd(new GregorianCalendar(2017,17,4,11,30).getTime());
        talk1.setSpeaker("Single Row");
        talk1.setDesc("A rendition of silence");
        talk1.setTitle("Life of a silent protagonist");
        talk1.setId(1);

        Schedule talk2 = new Schedule();
        talk2.setTimeStart(new GregorianCalendar(2017,17,4,11,40).getTime());
        talk2.setTimeEnd(new GregorianCalendar(2017,17,4,12,20).getTime());
        talk2.setSpeaker("Single row 2");
        talk2.setDesc("Frodo was a pain");
        talk2.setTitle("Life of a silent protagonist");
        talk2.setId(2);

        Schedule talk3 = new Schedule();
        talk3.setTimeStart(new GregorianCalendar(2017,17,4,12,30).getTime());
        talk3.setTimeEnd(new GregorianCalendar(2017,17,4,14,0).getTime());
        talk3.setSpeaker("double row");
        talk3.setDesc("mphhhh mmmhppp");
        talk3.setTitle("Life of a silent protagonist");
        talk3.setId(3);

        Schedule talk4 = new Schedule();
        talk4.setTimeStart(new GregorianCalendar(2017,17,4,15,10).getTime());
        talk4.setTimeEnd(new GregorianCalendar(2017,17,4,14,50).getTime());
        talk4.setSpeaker("double row part 2");
        talk4.setDesc("A rendition of silence");
        talk4.setTitle("Life of a silent protagonist");
        talk4.setId(4);

        Schedule talk5 = new Schedule();
        talk5.setTimeStart(new GregorianCalendar(2017,17,4,15,0).getTime());
        talk5.setTimeEnd(new GregorianCalendar(2017,17,4,15,30).getTime());
        talk5.setSpeaker("single row");
        talk5.setDesc("A rendition of silence");
        talk5.setTitle("Life of a silent protagonist");
        talk5.setId(5);

        Schedule talk6 = new Schedule();
        talk6.setTimeStart(new GregorianCalendar(2017,17,4,15,0).getTime());
        talk6.setTimeEnd(new GregorianCalendar(2017,17,4,15,30).getTime());
        talk6.setSpeaker("double row");
        talk6.setDesc("A rendition of silence");
        talk6.setTitle("Life of a silent protagonist");
        talk6.setId(6);

        Schedule talk7 = new Schedule();
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
        realm.close();

        }
}
