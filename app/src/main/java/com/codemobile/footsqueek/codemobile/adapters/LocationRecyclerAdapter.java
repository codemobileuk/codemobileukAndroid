package com.codemobile.footsqueek.codemobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codemobile.footsqueek.codemobile.R;
import com.codemobile.footsqueek.codemobile.database.Locations;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by greg on 25/01/2017.
 */

public class LocationRecyclerAdapter extends RecyclerView.Adapter<LocationRecyclerAdapter.LocationViewHolder> {

    List<Locations> location;
    Context context;

    public LocationRecyclerAdapter(List<Locations> location, Context context) {
        this.context = context;
        this.location = location;
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LocationViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.location_item_layout,parent,false)
        );
    }

    @Override
    public int getItemCount() {
        return location.size();
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, int position) {

        holder.locationName.setText(location.get(position).getLocationName());
        setOnClickListener(holder,position);

        Picasso.with(context).load(location.get(position).getImage_url()).fit().centerCrop().into(holder.imageView);

    }

    private void setOnClickListener(LocationViewHolder holder, final int pos){



        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lon = location.get(pos).getLongitude()+"";
                String lat = location.get(pos).getLatitude()+"";

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr=" + lat +"," +lon +""));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


    }

    public static class LocationViewHolder extends RecyclerView.ViewHolder{

      //  Button directionsButton;
        TextView locationName, distanceTv;
        ImageView imageView, button;

        public LocationViewHolder(View itemView) {
            super(itemView);

           // directionsButton = (Button)itemView.findViewById(R.id.navigate_button);
            locationName = (TextView)itemView.findViewById(R.id.location_name);
            distanceTv = (TextView)itemView.findViewById(R.id.distance_tv);
            imageView = (ImageView)itemView.findViewById(R.id.placeImageView);
            button = (ImageView)itemView.findViewById(R.id.button);
        }
    }

}
