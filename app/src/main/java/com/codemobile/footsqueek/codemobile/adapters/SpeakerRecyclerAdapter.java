package com.codemobile.footsqueek.codemobile.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codemobile.footsqueek.codemobile.Animations.ExpandViewAnimation;
import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.R;
import com.codemobile.footsqueek.codemobile.database.Session;
import com.codemobile.footsqueek.codemobile.database.SessionFavorite;
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
    boolean animating = false;
    int clickedPos = -1;
    ExpandViewAnimation expandViewAnimation;
    ExpandViewAnimation reduceSize;


    public SpeakerRecyclerAdapter(List<Speaker> speakers, SpeakerRecyclerInterface speakerRecyclerInterface, Context context) {
        this.context = context;
        this.speakers = speakers;
        this.speakerRecyclerInterface = speakerRecyclerInterface;
    }

    public void setAnimating(boolean animating){
        this.animating = animating;

    }
    public void cancelAnimations(){


        if(reduceSize != null && expandViewAnimation != null){
            reduceSize.cancel();
            expandViewAnimation.cancel();
        }else{
            Log.d("testingha", "null... ");
        }

    }

    @Override
    public void onViewAttachedToWindow(SpeakerViewHolder holder) {
        super.onViewAttachedToWindow(holder);

    }

    @Override
    public void onViewDetachedFromWindow(SpeakerViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.row.clearAnimation();
        Log.d("nicera", "====+==" );
    }

    @Override
    public void onBindViewHolder(final SpeakerViewHolder holder, final int position) {
        Realm realm = AppDelegate.getRealmInstance();
        Session session = realm.where(Session.class).equalTo("speakerId",speakers.get(position).getId()).findFirst();

      /*  if(animating && clickedPos != position){

            final ExpandViewAnimation reduceSize = new ExpandViewAnimation(holder.row,100,holder.row.getHeight(),false);
            reduceSize.setDuration(4000);
            reduceSize.setInterpolator(new DecelerateInterpolator());
          //  holder.row.setAnimation(reduceSize);

            reduceSize.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    animating = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    animating = false;
                   // holder.row.clearAnimation();
                    animation.cancel();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }else if(animating){
            Log.d("testingha", "height2: " + holder.row.getHeight() + "  " + position);
            expandViewAnimation = new ExpandViewAnimation(holder.row,1000,holder.row.getHeight(),true);
            expandViewAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            expandViewAnimation.setDuration(4000);

            expandViewAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    animating = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    animating = false;
                   // expandViewAnimation.reset();
                //    holder.row.clearAnimation();
                    speakerRecyclerInterface.speakerClicked(speakers.get(position).getId());
                    animation.cancel();

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }*/



        holder.speakerName.setText(speakers.get(position).getFirstname() +" " +speakers.get(position).getSurname() );
        holder.speakerTalk.setText(speakers.get(position).getOrganisation());

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

    private int getRelativeLeft(View myView) {
        if (myView.getParent() == myView.getRootView())
            return myView.getLeft();
        else
            return myView.getLeft() + getRelativeLeft((View) myView.getParent());
    }

    private int getRelativeTop(View myView) {
        if (myView.getParent() == myView.getRootView())
            return myView.getTop();
        else
            return myView.getTop() + getRelativeTop((View) myView.getParent());
    }

    private void setOnClickListeners(final SpeakerViewHolder holder, final int position){

        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // speakerRecyclerInterface.speakerClicked(speakers.get(position).getId());

                animating = true;
                speakerRecyclerInterface.notifyDataSetChanged(position);
                speakerRecyclerInterface.speakerClicked(speakers.get(position).getId());
                clickedPos = position;

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
        ConstraintLayout row;
        ImageView imageView;


        public SpeakerViewHolder(View itemView) {
            super(itemView);

            speakerName = (TextView)itemView.findViewById(R.id.speakerName);
            speakerTalk = (TextView)itemView.findViewById(R.id.speakerTalk);
            speakerBio = (TextView)itemView.findViewById(R.id.speakerBio);
            row = (ConstraintLayout)itemView.findViewById(R.id.row);
            imageView = (ImageView)itemView.findViewById(R.id.speakerImage);
        }
    }


}
