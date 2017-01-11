package com.codemobile.footsqueek.codemobile.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codemobile.footsqueek.codemobile.Database.Schedule;
import com.codemobile.footsqueek.codemobile.Interfaces.ScheduleRecyclerInterface;
import com.codemobile.footsqueek.codemobile.R;

import java.util.List;

/**
 * Created by gregv on 07/01/2017.
 */

public class ScheduleRecyclerAdapter extends RecyclerView.Adapter<ScheduleRecyclerAdapter.ScheduleViewHolder> {


    private List<Schedule> schedule;
    private ScheduleRecyclerInterface scheduleRecyclerInterface;

    public ScheduleRecyclerAdapter(List<Schedule> schedule, ScheduleRecyclerInterface scheduleRecyclerInterface) {
        this.scheduleRecyclerInterface = scheduleRecyclerInterface;
        this.schedule = schedule;
    }

    private void setClickListeners(final ScheduleViewHolder holder, final int position){

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scheduleRecyclerInterface.talkClicked(schedule.get(position).getId());
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

        holder.title.setText(schedule.get(position).getTitle());
        holder.speaker.setText(schedule.get(position).getSpeaker());
        holder.timeStart.setText(schedule.get(position).getTimeStart().toString());
        holder.timeEnd.setText(schedule.get(position).getTimeEnd().toString());

    }

    @Override
    public int getItemCount() {
        return schedule.size();
    }

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView speaker;
        TextView timeStart;
        TextView timeEnd;
        CardView card;

        public ScheduleViewHolder(View itemView){
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.title);
            speaker = (TextView)itemView.findViewById(R.id.speaker);
            timeStart = (TextView)itemView.findViewById(R.id.timeStart);
            timeEnd = (TextView)itemView.findViewById(R.id.timeEnd);
            card = (CardView) itemView.findViewById(R.id.card);

        }

    }

}
