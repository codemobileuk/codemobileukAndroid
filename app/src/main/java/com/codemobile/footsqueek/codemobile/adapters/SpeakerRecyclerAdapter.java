package com.codemobile.footsqueek.codemobile.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.R;
import com.codemobile.footsqueek.codemobile.database.Session;
import com.codemobile.footsqueek.codemobile.database.Speaker;
import com.codemobile.footsqueek.codemobile.interfaces.SpeakerRecyclerInterface;
import com.codemobile.footsqueek.codemobile.services.CircleCroppedBitmap;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.realm.Realm;

/**
 * Created by greg on 24/01/2017.
 */

public class SpeakerRecyclerAdapter extends RecyclerView.Adapter<SpeakerRecyclerAdapter.SpeakerViewHolder> {

    private List<Speaker> speakers;

    SpeakerRecyclerInterface speakerRecyclerInterface;
    Context context;


    public SpeakerRecyclerAdapter(List<Speaker> speakers, SpeakerRecyclerInterface speakerRecyclerInterface, Context context) {
        this.context = context;
        this.speakers = speakers;
        this.speakerRecyclerInterface = speakerRecyclerInterface;
    }

    @Override
    public void onBindViewHolder(final SpeakerViewHolder holder, final int position) {
        Realm realm = AppDelegate.getRealmInstance();
        Session session = realm.where(Session.class).equalTo("speakerId",speakers.get(position).getId()).findFirst();

        holder.speakerName.setText(speakers.get(position).getFirstname() +" " +speakers.get(position).getSurname() );
        if(session != null){
            holder.speakerTalk.setText(session.getTitle());
        }else{
            holder.speakerTalk.setText("A speaker with no talks :O");
        }

        Picasso.with(context)
                .load(speakers.get(position)
                .getPhotoUrl()).fit().centerCrop()
                .into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) holder.imageView.getDrawable()).getBitmap();
                        CircleCroppedBitmap circleCroppedBitmap = new CircleCroppedBitmap(imageBitmap, context);
                        circleCroppedBitmap.createRoundImage(holder.imageView);
                    }

                    @Override
                    public void onError() {

                    }
                });

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
        ImageView imageView;


        public SpeakerViewHolder(View itemView) {
            super(itemView);

            speakerName = (TextView)itemView.findViewById(R.id.speakerName);
            speakerTalk = (TextView)itemView.findViewById(R.id.speakerTalk);
            speakerBio = (TextView)itemView.findViewById(R.id.speakerBio);
            row = (LinearLayout)itemView.findViewById(R.id.row);
            imageView = (ImageView) itemView.findViewById(R.id.speakerImage);
        }
    }


}
