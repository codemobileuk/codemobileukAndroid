package com.codemobile.footsqueek.codemobile.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

        holder.title.setText(session.get(position).getTitle());
        holder.timeStart.setText(session.get(position).getTimeStart().toString());


        //if doublerow and if even
        if(session.get(position).isDoubleRow() && session.get(position+1).isDoubleRow()){
            actualRowCount ++;
            holder.line.setVisibility(View.VISIBLE);
        }else if(session.get(position).isDoubleRow() && session.get(position-1).isDoubleRow()){
            holder.line.setVisibility(View.INVISIBLE);
        }else {
            actualRowCount++;
            holder.line.setVisibility(View.INVISIBLE);
        }

        if(actualRowCount % 2 ==0){
            holder.row.setBackgroundColor(ContextCompat.getColor(context,R.color.commonWhite));
        }else {
            holder.row.setBackgroundColor(ContextCompat.getColor(context,R.color.commonLightGrey));
        }

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
