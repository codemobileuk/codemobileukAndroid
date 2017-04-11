package com.codemobile.footsqueek.codemobile.adapters;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.database.RealmUtility;
import com.codemobile.footsqueek.codemobile.database.ScheduleRowType;
import com.codemobile.footsqueek.codemobile.database.SessionFavorite;
import com.codemobile.footsqueek.codemobile.database.SessionFullData;
import com.codemobile.footsqueek.codemobile.database.SessionType;
import com.codemobile.footsqueek.codemobile.database.Tag;
import com.codemobile.footsqueek.codemobile.services.TimeManager;
import com.codemobile.footsqueek.codemobile.database.Session;
import com.codemobile.footsqueek.codemobile.database.Speaker;
import com.codemobile.footsqueek.codemobile.interfaces.ScheduleRecyclerInterface;
import com.codemobile.footsqueek.codemobile.R;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by gregv on 07/01/2017.
 */

public class ScheduleRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


 //   private List<Session> session;
    private List<SessionFullData> sessionWithHeaders;
    private ScheduleRecyclerInterface scheduleRecyclerInterface;
    private Context context;
    private int actualRowCount =-1;

    private static int TYPE_BREAK = 1;
    private static int TYPE_SESSION = 0;
    private static int TYPE_HEADER = 2;
    SessionFavorite sessionFavorite;

    private  Realm realm;
    public ScheduleRecyclerAdapter(List<SessionFullData> sessionWithHeaders, ScheduleRecyclerInterface scheduleRecyclerInterface, Context context) {
        this.context = context;
        this.scheduleRecyclerInterface = scheduleRecyclerInterface;
        this.sessionWithHeaders = sessionWithHeaders;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if(viewType == TYPE_SESSION){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item_layout,parent,false);
            return new ScheduleViewHolder(v);
        }else if(viewType == TYPE_BREAK) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_break_item_layout,parent,false);
            return new BreakHolder(v);
        }else if(viewType == TYPE_HEADER){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_header_item_layout,parent,false);
            return new HeaderHolder(v);
        }else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_break_item_layout,parent,false);
            return new ScheduleViewHolder(v);//TODO CHANGE
        }


    }

    @Override
    public int getItemViewType(int position) {


        Session session = sessionWithHeaders.get(position).getSession();

        if(session != null){
            if(session.getSessionType().equals(SessionType.BREAK)){
                return TYPE_BREAK;
            }else {
                return TYPE_SESSION;
            }
        }else{
            return TYPE_HEADER;
        }


    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        realm = AppDelegate.getRealmInstance();
        Session session = sessionWithHeaders.get(position).getSession();

        if(holder instanceof ScheduleViewHolder){
            onClickListeners(holder, session, position);
            setTwoPane(holder);
            setFavorites(holder, session);
            setTagVisibility(holder, session);

            Speaker speaker = realm.where(Speaker.class).equalTo("id",session.getSpeakerId()).findFirst();

            ((ScheduleViewHolder)holder).title.setText(session.getTitle());
            if(speaker != null){
                ((ScheduleViewHolder)holder).speaker.setText(speaker.getFirstname() + " " + speaker.getSurname());
            }

            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

            ((ScheduleViewHolder)holder).row.setBackgroundColor(sessionWithHeaders.get(position).getRowColour());

                if(sessionWithHeaders.get(position).getRowType() == ScheduleRowType.DOUBLE_LEFT){
                    ((ScheduleViewHolder)holder).line.setVisibility(View.VISIBLE);
                    ((ScheduleViewHolder) holder).timeStart.setVisibility(View.GONE);
                    ((ScheduleViewHolder)holder).timeStart.setText("Act row count: " + actualRowCount +" Position: " +position);

                }
                if(sessionWithHeaders.get(position).getRowType() == ScheduleRowType.DOUBLE_RIGHT){
                    ((ScheduleViewHolder)holder).line.setVisibility(View.INVISIBLE);
                    ((ScheduleViewHolder) holder).timeStart.setVisibility(View.GONE);
                    ((ScheduleViewHolder)holder).timeStart.setText("Act row count: " + actualRowCount +" Position: " +position);

                }else if(sessionWithHeaders.get(position).getRowType() == ScheduleRowType.NORMAL){
                    ((ScheduleViewHolder)holder).line.setVisibility(View.INVISIBLE);
                    ((ScheduleViewHolder)holder).timeStart.setVisibility(View.VISIBLE);
                    ((ScheduleViewHolder)holder).timeStart.setText(TimeManager.trimTimeFromDate(session.getTimeStart())+ " - " + TimeManager.trimTimeFromDate(session.getTimeEnd()));
                    actualRowCount++;

                }


        }else if(holder instanceof BreakHolder){
            ((BreakHolder) holder).time.setText(TimeManager.trimTimeFromDate(session.getTimeStart())+ " - " + TimeManager.trimTimeFromDate(session.getTimeEnd()));
            ((BreakHolder) holder).name.setText(sessionWithHeaders.get(position).getSession().getSessionType());


        }else if(holder instanceof HeaderHolder){
            actualRowCount ++;
            ((HeaderHolder) holder).time.setText(sessionWithHeaders.get(position).getTime());
            ((HeaderHolder)holder).row.setBackgroundColor(sessionWithHeaders.get(position).getRowColour());

        }
    }

    private void setTwoPane(RecyclerView.ViewHolder holder){
        if(AppDelegate.isTwoPane()){
            ((ScheduleViewHolder) holder).favorite.setVisibility(View.INVISIBLE);
        }else{
            ((ScheduleViewHolder) holder).favorite.setVisibility(View.VISIBLE);
        }
    }
    private void setFavorites(RecyclerView.ViewHolder holder, Session session){
        if(session != null){
            sessionFavorite = realm.where(SessionFavorite.class).equalTo("sessionId",session.getId()).findFirst();
            if(sessionFavorite != null){
                if(sessionFavorite.getFavorite()){
                    ((ScheduleViewHolder) holder).favorite.setBackground(ContextCompat.getDrawable(context,R.drawable.favorite));
                }else{
                    ((ScheduleViewHolder) holder).favorite.setBackground(ContextCompat.getDrawable(context,R.drawable.favorite_not_selected));
                }
            }else{
                ((ScheduleViewHolder) holder).favorite.setBackground(ContextCompat.getDrawable(context,R.drawable.favorite_not_selected));
            }

        }
    }

    private void setTagVisibility(RecyclerView.ViewHolder holder, Session session){
        List<Tag> tags = realm.where(Tag.class).equalTo("sessionId",session.getId()).findAll();
        // hardcoded number of text views for displaying tags in the recycler to 2.
        if(tags.size() >= 2){
            ((ScheduleViewHolder) holder).tag1.setText(tags.get(1).getTag());
            ((ScheduleViewHolder) holder).tag2.setText(tags.get(0).getTag());
            ((ScheduleViewHolder) holder).tag1.setVisibility(View.VISIBLE);
            ((ScheduleViewHolder) holder).tag2.setVisibility(View.VISIBLE);
        }else if(tags.size() ==1){
            ((ScheduleViewHolder) holder).tag1.setText(tags.get(0).getTag());
            ((ScheduleViewHolder) holder).tag1.setVisibility(View.VISIBLE);
            ((ScheduleViewHolder) holder).tag2.setVisibility(View.INVISIBLE);
        }else{
            ((ScheduleViewHolder) holder).tag1.setVisibility(View.INVISIBLE);
            ((ScheduleViewHolder) holder).tag2.setVisibility(View.INVISIBLE);
        }
    }


    public void onClickListeners(final RecyclerView.ViewHolder holder, final Session session,final int position){

        ((ScheduleViewHolder)holder).row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scheduleRecyclerInterface.talkClicked(session.getId());
                AppDelegate.setSharedView(((ScheduleViewHolder)holder).title);
                AppDelegate.setSharedViewId("title" + position);

            }
        });

        ((ScheduleViewHolder)holder).favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Realm realm = AppDelegate.getRealmInstance();
                sessionFavorite = realm.where(SessionFavorite.class).equalTo("sessionId",session.getId()).findFirst();
                Animation spinIn = AnimationUtils.loadAnimation(context,R.anim.spin);
                final Animation spinOut = AnimationUtils.loadAnimation(context,R.anim.after_spin);


                    ((ScheduleViewHolder) holder).favorite.startAnimation(spinIn);
                    spinIn.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if(sessionFavorite != null) {
                                Log.d("testingstuff", "favorites2: " + sessionFavorite.getFavorite() + " id: " + session.getId());
                                if (sessionFavorite.getFavorite()) {
                                    ((ScheduleViewHolder) holder).favorite.setBackground(ContextCompat.getDrawable(context, R.drawable.favorite_not_selected));
                                    sessionFavorite = new SessionFavorite(session.getId(), "", false);
                                } else {
                                    ((ScheduleViewHolder) holder).favorite.setBackground(ContextCompat.getDrawable(context, R.drawable.favorite));
                                    sessionFavorite = new SessionFavorite(session.getId(), "", true);
                                }

                            }else{
                                ((ScheduleViewHolder) holder).favorite.setBackground(ContextCompat.getDrawable(context, R.drawable.favorite));
                                sessionFavorite = new SessionFavorite(session.getId(), "", true);
                            }
                            ((ScheduleViewHolder) holder).favorite.startAnimation(spinOut);
                            RealmUtility.addNewRow(sessionFavorite);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });


            }
        });



    }

    @Override
    public int getItemCount() {
        return sessionWithHeaders.size();
    }

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView speaker;
        TextView timeStart;
        TextView tag1, tag2;
        ImageView buildingIcon, favorite;
        View line;
        LinearLayout row;



        public ScheduleViewHolder(View itemView){
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.title);
            speaker = (TextView)itemView.findViewById(R.id.speaker);
            timeStart = (TextView)itemView.findViewById(R.id.timeStart);
            row = (LinearLayout)itemView.findViewById(R.id.row);
            line = (View)itemView.findViewById(R.id.line);
            buildingIcon = (ImageView)itemView.findViewById(R.id.buildingIcon);
            tag1 = (TextView)itemView.findViewById(R.id.tag1);
            tag2 = (TextView)itemView.findViewById(R.id.tag2);
            favorite = (ImageView)itemView.findViewById(R.id.favorite_btn);

        }

    }

    public static class BreakHolder extends RecyclerView.ViewHolder{

        TextView time;
        TextView name;

        public BreakHolder(View itemView) {
            super(itemView);

            time = (TextView)itemView.findViewById(R.id.timeStart);
            name = (TextView)itemView.findViewById(R.id.name);
        }
    }

    public static class HeaderHolder extends RecyclerView.ViewHolder{

        TextView time;
        LinearLayout row;

        public HeaderHolder(View itemView) {
            super(itemView);

            time = (TextView)itemView.findViewById(R.id.timeStart);
            row = (LinearLayout)itemView.findViewById(R.id.row);
        }
    }

}
