package com.codemobile.footsqueek.codemobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codemobile.footsqueek.codemobile.R;
import com.codemobile.footsqueek.codemobile.database.Location;
import com.codemobile.footsqueek.codemobile.database.LocationWithHeaders;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by greg on 25/01/2017.
 */

public class LocationRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<LocationWithHeaders> locationWithHeaders;
    Context context;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ROW = 1;

    public LocationRecyclerAdapter(List<LocationWithHeaders> location, Context context) {
        this.context = context;
        this.locationWithHeaders = location;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == TYPE_HEADER){
            return new HeaderViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.location_item_header,parent,false)
            );
        }else if(viewType == TYPE_ROW){
            return new LocationViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.location_item_layout,parent,false)
            );
        }
        return new LocationViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.location_item_layout,parent,false)
        );
    }

    @Override
    public int getItemCount() {
        return locationWithHeaders.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if(holder instanceof HeaderViewHolder){
            ((HeaderViewHolder) holder).title.setText(locationWithHeaders.get(position).getTitle());
        }else if(holder instanceof  LocationViewHolder){
            Location location = locationWithHeaders.get(position).getLocation();

            ((LocationViewHolder) holder).locationName.setText(location.getLocationName());
            setOnClickListener((LocationViewHolder) holder,position,location);

            Picasso.with(context).load(location.getImage_url()).fit().centerCrop().into(((LocationViewHolder) holder).imageView);
        }


    }

    @Override
    public int getItemViewType(int position) {

        if(locationWithHeaders.get(position).getRowType() == TYPE_HEADER){
            return TYPE_HEADER;
        }else if(locationWithHeaders.get(position).getRowType() == TYPE_ROW){
            return TYPE_ROW;
        }

        return super.getItemViewType(position);
    }

    private void setOnClickListener(LocationViewHolder holder, final int pos, final Location location){



        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lon = location.getLongitude()+"";
                String lat = location.getLatitude()+"";

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

    public static class HeaderViewHolder extends RecyclerView.ViewHolder{

        TextView title;

        public HeaderViewHolder(View itemView){
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.header_tv);

        }

    }

}
