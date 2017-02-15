package com.codemobile.footsqueek.codemobile.adapters;

import android.content.Context;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.database.ScheduleRowType;
import com.codemobile.footsqueek.codemobile.database.SessionFullData;
import com.codemobile.footsqueek.codemobile.database.SessionType;
import com.codemobile.footsqueek.codemobile.database.Tag;
import com.codemobile.footsqueek.codemobile.services.TimeConverter;
import com.codemobile.footsqueek.codemobile.database.Session;
import com.codemobile.footsqueek.codemobile.database.Speaker;
import com.codemobile.footsqueek.codemobile.interfaces.ScheduleRecyclerInterface;
import com.codemobile.footsqueek.codemobile.R;
import com.squareup.picasso.Picasso;

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

    public ScheduleRecyclerAdapter(List<SessionFullData> sessionWithHeaders, ScheduleRecyclerInterface scheduleRecyclerInterface, Context context) {
        this.context = context;
        this.scheduleRecyclerInterface = scheduleRecyclerInterface;
        this.sessionWithHeaders = sessionWithHeaders;
    }

    private void setClickListeners(final RecyclerView.ViewHolder holder, final Session session){

        ((ScheduleViewHolder)holder).row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scheduleRecyclerInterface.talkClicked(session.getId());
            }
        });

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

        Session session = sessionWithHeaders.get(position).getSession();

        if(holder instanceof ScheduleViewHolder){
            setClickListeners(holder, session);
            Realm realm = AppDelegate.getRealmInstance();
            Speaker speaker = realm.where(Speaker.class).equalTo("id",session.getSpeakerId()).findFirst();

            ((ScheduleViewHolder)holder).title.setText(session.getTitle());
            ((ScheduleViewHolder)holder).speaker.setText(speaker.getFirstname() + " " + speaker.getSurname());
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
            if(session.getLocationName().equals("Molloy")){
                ((ScheduleViewHolder) holder).buildingIcon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_molloy));
            }else{
                ((ScheduleViewHolder) holder).buildingIcon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_beswick));
            }

            List<Tag> tags = realm.where(Tag.class).equalTo("sessionId",session.getId()).findAll();

               // tagsString = tagsString +" "+ tags.get(i).getTag();
                if(tags.size() == 2){
                    Log.d("uhhhhh", session.getId() +"  " + tags.get(0).getTag() +" == " + tags.get(1).getTag());
                    ((ScheduleViewHolder) holder).tag1.setText(tags.get(1).getTag());
                    ((ScheduleViewHolder) holder).tag2.setText(tags.get(0).getTag());
                }else if(tags.size() ==1){
                    Log.d("uhhhhh", session.getId() +"  " + tags.get(0).getTag());
                    ((ScheduleViewHolder) holder).tag1.setText(tags.get(0).getTag());
                    ((ScheduleViewHolder) holder).tag2.setVisibility(View.INVISIBLE);
                }else{
                    Log.d("uhhhhh", session.getId());
                    ((ScheduleViewHolder) holder).tag2.setVisibility(View.INVISIBLE);
                    ((ScheduleViewHolder) holder).tag2.setVisibility(View.INVISIBLE);
                }

                if(sessionWithHeaders.get(position).getRowType() == ScheduleRowType.DOUBLE_LEFT)
                {
                    ((ScheduleViewHolder)holder).line.setVisibility(View.VISIBLE);
                    ((ScheduleViewHolder) holder).timeStart.setVisibility(View.GONE);

                }
                if(sessionWithHeaders.get(position).getRowType() == ScheduleRowType.DOUBLE_RIGHT){
                    ((ScheduleViewHolder)holder).line.setVisibility(View.INVISIBLE);
                    ((ScheduleViewHolder) holder).timeStart.setVisibility(View.GONE);
                }else if(sessionWithHeaders.get(position).getRowType() == ScheduleRowType.NORMAL){
                    ((ScheduleViewHolder)holder).line.setVisibility(View.INVISIBLE);
                    ((ScheduleViewHolder)holder).timeStart.setText(TimeConverter.trimTimeFromDate(session.getTimeStart())+ " - " +TimeConverter.trimTimeFromDate(session.getTimeEnd()));
                    actualRowCount++;
                }

            if(actualRowCount % 2 ==0){
                ((ScheduleViewHolder)holder).row.setBackgroundColor(ContextCompat.getColor(this.context,R.color.commonWhite));
            }else {
                ((ScheduleViewHolder)holder).row.setBackgroundColor(ContextCompat.getColor(this.context,R.color.commonLightGrey));
            }

        }else if(holder instanceof BreakHolder){
            //actualRowCount++;
            ((BreakHolder) holder).time.setText(TimeConverter.trimTimeFromDate(session.getTimeStart())+ " - " +TimeConverter.trimTimeFromDate(session.getTimeEnd()));
            ((BreakHolder) holder).name.setText(sessionWithHeaders.get(position).getSession().getSessionType());

        }else if(holder instanceof HeaderHolder){
            actualRowCount ++;
            ((HeaderHolder) holder).time.setText(sessionWithHeaders.get(position).getTime());
            if(actualRowCount % 2 ==0){
                ((HeaderHolder)holder).row.setBackgroundColor(ContextCompat.getColor(this.context,R.color.commonWhite));
            }else {
                ((HeaderHolder)holder).row.setBackgroundColor(ContextCompat.getColor(context,R.color.commonLightGrey));
            }
        }
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
        ImageView buildingIcon;
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
      //  TextView name;

        public HeaderHolder(View itemView) {
            super(itemView);

            time = (TextView)itemView.findViewById(R.id.timeStart);
            //name = (TextView)itemView.findViewById(R.id.name);
            row = (LinearLayout)itemView.findViewById(R.id.row);
        }
    }

}
