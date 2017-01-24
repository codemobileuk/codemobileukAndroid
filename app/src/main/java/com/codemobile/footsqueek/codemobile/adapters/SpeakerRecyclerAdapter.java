package com.codemobile.footsqueek.codemobile.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codemobile.footsqueek.codemobile.R;
import com.codemobile.footsqueek.codemobile.database.Speaker;
import com.codemobile.footsqueek.codemobile.interfaces.SpeakerRecyclerInterface;

import java.util.List;

/**
 * Created by greg on 24/01/2017.
 */

public class SpeakerRecyclerAdapter extends RecyclerView.Adapter<SpeakerRecyclerAdapter.SpeakerViewHolder> {

    private List<Speaker> speakers;

    SpeakerRecyclerInterface speakerRecyclerInterface;



    public SpeakerRecyclerAdapter(List<Speaker> speakers, SpeakerRecyclerInterface speakerRecyclerInterface) {
        this.speakers = speakers;
        this.speakerRecyclerInterface = speakerRecyclerInterface;
    }

    @Override
    public void onBindViewHolder(SpeakerViewHolder holder, int position) {

        holder.speakerName.setText(speakers.get(position).getFirstname());
        holder.speakerBio.setText("TEXT");

        setOnClickListeners(holder, position);
    }

    private void setOnClickListeners(SpeakerViewHolder holder, final int position){

        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakerRecyclerInterface.speakerClicked(speakers.get(position).getId());
            }
        });

    }

    @Override
    public SpeakerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SpeakerViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.speaker_item_layout,parent,false)
        );
    }

    @Override
    public int getItemCount() {
        return speakers.size();
    }

    public static class SpeakerViewHolder extends RecyclerView.ViewHolder{

        TextView speakerName;
        TextView speakerTalk;
        TextView speakerBio;
        LinearLayout row;


        public SpeakerViewHolder(View itemView) {
            super(itemView);

            speakerName = (TextView)itemView.findViewById(R.id.speakerName);
            speakerTalk = (TextView)itemView.findViewById(R.id.speakerTalk);
            speakerBio = (TextView)itemView.findViewById(R.id.speakerBio);
            row = (LinearLayout)itemView.findViewById(R.id.row);
        }
    }


}
