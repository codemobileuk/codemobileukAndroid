package com.codemobile.footsqueek.codemobile.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.R;
import com.codemobile.footsqueek.codemobile.database.Session;
import com.codemobile.footsqueek.codemobile.database.Speaker;

import java.util.List;

import io.realm.Realm;

/**
 * Created by greg on 02/02/2017.
 */

public class ScheduleHorizontalRecyclerAdapter extends RecyclerView.Adapter<ScheduleHorizontalRecyclerAdapter.ScheduleViewHolder>{

    private List<Session> sessions;

    public ScheduleHorizontalRecyclerAdapter(List<Session> sessions) {
        this.sessions = sessions;
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }

    @Override
    public void onBindViewHolder(ScheduleViewHolder holder, int position) {

        Realm realm = AppDelegate.getRealmInstance();
        Speaker speaker = realm.where(Speaker.class).equalTo("id",sessions.get(position).getSpeakerId()).findFirst();

        holder.desc.setEllipsize(TextUtils.TruncateAt.END);
        holder.desc.setMaxLines(3);
        holder.desc.setText(sessions.get(position).getDesc());
        holder.speaker.setText(speaker.getFirstname() +" " + speaker.getSurname());
    }

    @Override
    public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScheduleViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item_horizontal_layout,parent,false)
        );
    }

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder{

        TextView desc, speaker;
    public ScheduleViewHolder(View itemView) {
        super(itemView);

        desc = (TextView)itemView.findViewById(R.id.desc);
        speaker = (TextView)itemView.findViewById(R.id.speaker);
    }
}

}
