package com.codemobile.footsqueek.codemobile.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.R;
import com.codemobile.footsqueek.codemobile.adapters.LocationRecyclerAdapter;
import com.codemobile.footsqueek.codemobile.database.Location;
import com.codemobile.footsqueek.codemobile.database.LocationRowType;
import com.codemobile.footsqueek.codemobile.database.LocationWithHeaders;
import com.codemobile.footsqueek.codemobile.fetcher.UpdateTables;
import com.codemobile.footsqueek.codemobile.interfaces.UpdateTablesInterface;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by greg on 06/02/2017.
 */

public class LocationsActivity extends LaunchActivity {

    LocationRecyclerAdapter adapter;
    RecyclerView recyclerView;
    List<LocationWithHeaders> locationsWithHeaders = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);
        fetchSchedule();
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        recyclerView = (RecyclerView)findViewById(R.id.recycler);

        recyclerView.setHasFixedSize(true);

        addHeaders();
        GridLayoutManager glm = new GridLayoutManager(getApplicationContext(),2);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(locationsWithHeaders.get(position).getRowType() == LocationRowType.HEADER){
                    return 2;
                }else{
                    return 1;
                }

            }
        });
        recyclerView.setLayoutManager(glm);
        adapter = new LocationRecyclerAdapter(locationsWithHeaders,getApplicationContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        setupActionBar();
        navigationViewItemPosition = 3;

    }

    public void addHeaders(){

        List<Location> locations = getLocations();

        String lastType = "";

        for (int i = 0; i < locations.size(); i++) {

            if(i == 0){
                lastType = locations.get(i).getType();
                LocationWithHeaders header = new LocationWithHeaders(
                        null,
                        LocationRowType.HEADER,
                        locations.get(i).getType()
                );
                LocationWithHeaders row = new LocationWithHeaders(
                        locations.get(i),
                        LocationRowType.ROW,
                        null
                );
                locationsWithHeaders.add(header);
                locationsWithHeaders.add(row);
            }else if(locations.get(i).getType().equals(lastType)){
                LocationWithHeaders row = new LocationWithHeaders(
                        locations.get(i),
                        LocationRowType.ROW,
                        null
                );
                locationsWithHeaders.add(row);
            }else if(!locations.get(i).getType().equals(lastType)){
                LocationWithHeaders header = new LocationWithHeaders(
                        null,
                        LocationRowType.HEADER,
                        locations.get(i).getType()
                );
                LocationWithHeaders row = new LocationWithHeaders(
                        locations.get(i),
                        LocationRowType.ROW,
                        null
                );
                locationsWithHeaders.add(header);
                locationsWithHeaders.add(row);
            }


        }

    }

    public List<Location> getLocations(){
        Realm realm = AppDelegate.getRealmInstance();
        List<Location> d= realm.where(Location.class).findAll();

        return realm.where(Location.class).findAllSorted("type");
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationViewItemPosition = 3;
    }
    public void fetchSchedule() {
    Log.d("Realmstuff","----------------------------------------------");
        final UpdateTables updateTables = new UpdateTables();
        updateTables.setUpdateTablesInterface(new UpdateTablesInterface() {
            @Override
            public void onComplete() {
                //  updateUi();
                Log.d("Realmstuff","-----COMPLETE-----");
            }

            @Override
            public void onError() {

            }
        });
        updateTables.compareAndUpdate();
    }


 /*   @Override
    public void updateUi() {
        super.updateUi();
        addHeaders();
        adapter.notifyDataSetChanged();
    }*/
}
