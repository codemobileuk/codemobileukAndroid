package com.codemobile.footsqueek.codemobile.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.services.TimeConverter;
import com.codemobile.footsqueek.codemobile.database.Session;
import com.codemobile.footsqueek.codemobile.database.Speaker;
import com.codemobile.footsqueek.codemobile.interfaces.ScheduleRecyclerInterface;
import com.codemobile.footsqueek.codemobile.R;

import java.util.List;

import io.realm.Realm;

/**
 * Created by gregv on 07/01/2017.
 */

public class ScheduleRecyclerAdapter extends RecyclerView.Adapter<ScheduleRecyclerAdapter.ScheduleViewHolder> {


    private List<Session> session;
    private ScheduleRecyclerInterface scheduleRecyclerInterface;
    private Context context;
    private int actualRowCount =-1;

    public ScheduleRecyclerAdapter(List<Session> session, ScheduleRecyclerInterface scheduleRecyclerInterface, Context context) {
        this.context = context;
        this.scheduleRecyclerInterface = scheduleRecyclerInterface;
        this.session = session;
    }

    private void setClickListeners(final ScheduleViewHolder holder, final int position){

        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scheduleRecyclerInterface.talkClicked(session.get(position).getId());
            }
        });

    }

    @Override
    public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScheduleViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item_layout,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(ScheduleViewHolder holder, int position) {
        setClickListeners(holder, position);
        Realm realm = AppDelegate.getRealmInstance();
        Speaker speaker = realm.where(Speaker.class).equalTo("id",session.get(position).getSpeakerId()).findFirst();


        holder.title.setText(session.get(position).getTitle());
        holder.timeStart.setText(TimeConverter.trimTimeFromDate(session.get(position).getTimeStart())+ " - " +TimeConverter.trimTimeFromDate(session.get(position).getTimeEnd()));
        holder.speaker.setText(speaker.getFirstname() + " " + speaker.getSurname());

        session.get(position).isDoubleRow();


        Log.d("sadtimes", session.get(position).isDoubleRow()+" is double");
        //if doublerow and if even



         /*   if(session.get(position).isDoubleRow()){

                actualRowCount ++;
                holder.line.setVisibility(View.VISIBLE);

            }else if(session.get(position).isDoubleRow() && session.get(position-1).isDoubleRow()){
                holder.line.setVisibility(View.INVISIBLE);
            }else {
                actualRowCount++;
                holder.line.setVisibility(View.INVISIBLE);
            }

            if(actualRowCount % 2 ==0){
                holder.row.setBackgroundColor(ContextCompat.getColor(this.context,R.color.commonWhite));
            }else {
                holder.row.setBackgroundColor(ContextCompat.getColor(context,R.color.commonLightGrey));
            }*/

        if(position == 0){
            if(session.get(position).getTimeStart().equals(session.get(position+1).getTimeStart())){
                //rows 0+1 are a double row
                holder.line.setVisibility(View.VISIBLE);
                holder.speaker.setText("XXXX");
                actualRowCount ++;
            }


        }else if(position == session.size()-1){
            if(session.get(position).getTimeStart().equals(session.get(position-1).getTimeStart())){
                //last row is double row
                holder.line.setVisibility(View.INVISIBLE);
                holder.speaker.setText("YYYY");
            }else{
                holder.line.setVisibility(View.INVISIBLE);
                actualRowCount++;
            }
        }else {
            if (session.get(position).getTimeStart().equals(session.get(position+1).getTimeStart())) {
                //rows 1-second from last (frist double)
                holder.line.setVisibility(View.VISIBLE);
                holder.speaker.setText("TTTT");
                actualRowCount ++;
            } else if (session.get(position).getTimeStart().equals(session.get(position-1).getTimeStart())) {
                //rows 1-second from last (second double)
                holder.line.setVisibility(View.INVISIBLE);
                holder.speaker.setText("GGG");
            }else{
                holder.line.setVisibility(View.INVISIBLE);
                holder.speaker.setText("BBBB");
                actualRowCount++;
            }
        }

        if(actualRowCount % 2 ==0){
            holder.row.setBackgroundColor(ContextCompat.getColor(this.context,R.color.commonWhite));
        }else {
            holder.row.setBackgroundColor(ContextCompat.getColor(context,R.color.commonLightGrey));
        }



    }

    public boolean nextRowDouble(int position){
        if(position == 0){
            if(session.get(position).isDoubleRow() && session.get(position+1).isDoubleRow()){
                //rows 0+1 are a double row
                return true;
            }
        }else if(position == session.size()-1){
            if(session.get(position).isDoubleRow() && session.get(position-1).isDoubleRow()){
                //last row is double row
                return true;
            }
        }else {
            if (session.get(position).isDoubleRow() && session.get(position + 1).isDoubleRow()) {
                //rows 1-second from last (frist double)
                return true;
            } else if (session.get(position).isDoubleRow() && session.get(position - 1).isDoubleRow()) {
                //rows 1-second from last (second double)
                return true;
            }
        }
        return false;
    }


    @Override
    public int getItemCount() {
        return session.size();
    }

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView speaker;
        TextView timeStart;
        View line;
        LinearLayout row;


        public ScheduleViewHolder(View itemView){
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.title);
            speaker = (TextView)itemView.findViewById(R.id.speaker);
            timeStart = (TextView)itemView.findViewById(R.id.timeStart);
            row = (LinearLayout)itemView.findViewById(R.id.row);
            line = (View)itemView.findViewById(R.id.line);
        }

    }

}
