package com.codemobile.footsqueek.codemobile.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.codemobile.footsqueek.codemobile.Adapters.ScheduleRecyclerAdapter;
import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.Database.Schedule;
import com.codemobile.footsqueek.codemobile.Interfaces.ScheduleRecyclerInterface;
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
    ScheduleRecyclerAdapter cyanAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        generateDummySchedule();


        tealRecyclerView = (RecyclerView)findViewById(R.id.tealRecycler);
        cyanRecyclerView = (RecyclerView)findViewById(R.id.cyanRecycler);
        tealRecyclerView.setHasFixedSize(true);
        cyanRecyclerView.setHasFixedSize(true);
        GridLayoutManager glm = new GridLayoutManager(getApplicationContext(),1);
        GridLayoutManager glm2 = new GridLayoutManager(getApplicationContext(),1);
        tealRecyclerView.setLayoutManager(glm);
        cyanRecyclerView.setLayoutManager(glm2);

        tealAdapter = new ScheduleRecyclerAdapter(getSchedule(),this);
        cyanAdapter = new ScheduleRecyclerAdapter(getSchedule(),this);
        tealRecyclerView.setAdapter(tealAdapter);
        cyanRecyclerView.setAdapter(cyanAdapter);
        tealAdapter.notifyDataSetChanged();
        cyanAdapter.notifyDataSetChanged();


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

        return realm.where(Schedule.class).findAllSorted("timeStart");
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
        talk1.setSpeaker("Gordon Freeman");
        talk1.setDesc("A rendition of silence");
        talk1.setTitle("Life of a silent protagonist");
        talk1.setId(1);

        Schedule talk2 = new Schedule();
        talk2.setTimeStart(new GregorianCalendar(2017,17,4,11,40).getTime());
        talk2.setTimeEnd(new GregorianCalendar(2017,17,4,12,20).getTime());
        talk2.setSpeaker("Sam Wize");
        talk2.setDesc("Frodo was a pain");
        talk2.setTitle("Life of a silent protagonist");
        talk1.setId(2);

        Schedule talk3 = new Schedule();
        talk3.setTimeStart(new GregorianCalendar(2017,17,4,12,30).getTime());
        talk3.setTimeEnd(new GregorianCalendar(2017,17,4,14,0).getTime());
        talk3.setSpeaker("Pyro");
        talk3.setDesc("mphhhh mmmhppp");
        talk3.setTitle("Life of a silent protagonist");
        talk1.setId(3);

        Schedule talk4 = new Schedule();
        talk4.setTimeStart(new GregorianCalendar(2017,17,4,14,10).getTime());
        talk4.setTimeEnd(new GregorianCalendar(2017,17,4,14,50).getTime());
        talk4.setSpeaker("Gordon Freeman");
        talk4.setDesc("A rendition of silence");
        talk4.setTitle("Life of a silent protagonist");
        talk1.setId(4);

        Schedule talk5 = new Schedule();
        talk5.setTimeStart(new GregorianCalendar(2017,17,4,15,0).getTime());
        talk5.setTimeEnd(new GregorianCalendar(2017,17,4,15,30).getTime());
        talk5.setSpeaker("Gordon Freeman");
        talk5.setDesc("A rendition of silence");
        talk5.setTitle("Life of a silent protagonist");
        talk1.setId(5);

        realm.copyToRealm(talk1);
        realm.copyToRealm(talk2);
        realm.copyToRealm(talk3);
        realm.copyToRealm(talk4);
        realm.copyToRealm(talk5);

        realm.commitTransaction();
        realm.close();

        }
}
