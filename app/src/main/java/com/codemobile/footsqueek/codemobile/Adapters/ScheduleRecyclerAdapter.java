package com.codemobile.footsqueek.codemobile.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codemobile.footsqueek.codemobile.database.Session;
import com.codemobile.footsqueek.codemobile.interfaces.ScheduleRecyclerInterface;
import com.codemobile.footsqueek.codemobile.R;

import java.util.List;

/**
 * Created by gregv on 07/01/2017.
 */

public class ScheduleRecyclerAdapter extends RecyclerView.Adapter<ScheduleRecyclerAdapter.ScheduleViewHolder> {


    private List<Session> session;
    private ScheduleRecyclerInterface scheduleRecyclerInterface;

    public ScheduleRecyclerAdapter(List<Session> session, ScheduleRecyclerInterface scheduleRecyclerInterface) {
        this.scheduleRecyclerInterface = scheduleRecyclerInterface;
        this.session = session;
    }

    private void setClickListeners(final ScheduleViewHolder holder, final int position){

        holder.card.setOnClickListener(new View.OnClickListener() {
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

        holder.title.setText(session.get(position).getTitle());

        holder.timeStart.setText(session.get(position).getTimeStart().toString());
        holder.timeEnd.setText(session.get(position).getTimeEnd().toString());

    }

    @Override
    public int getItemCount() {
        return session.size();
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
