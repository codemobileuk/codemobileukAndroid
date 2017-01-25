package com.codemobile.footsqueek.codemobile.adapters;

import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.codemobile.footsqueek.codemobile.R;
import com.codemobile.footsqueek.codemobile.database.Locations;

import java.util.List;

/**
 * Created by greg on 25/01/2017.
 */

public class LocationRecyclerAdapter extends RecyclerView.Adapter<LocationRecyclerAdapter.LocationViewHolder> {

    List<Locations> location;


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
        setOnClickListener();
    }

    private void setOnClickListener(){

    }

    public static class LocationViewHolder extends RecyclerView.ViewHolder{

        Button directionsButton;
        TextView locationName;

        public LocationViewHolder(View itemView) {
            super(itemView);



        }
    }

}
