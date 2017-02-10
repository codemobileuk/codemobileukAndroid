package com.codemobile.footsqueek.codemobile.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.R;
import com.codemobile.footsqueek.codemobile.adapters.LocationRecyclerAdapter;
import com.codemobile.footsqueek.codemobile.database.Locations;

import java.util.List;

import io.realm.Realm;

/**
 * Created by greg on 06/02/2017.
 */

public class LocationsActivity extends LaunchActivity {

    LocationRecyclerAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

        recyclerView = (RecyclerView)findViewById(R.id.recycler);

        recyclerView.setHasFixedSize(true);
        GridLayoutManager glm = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(glm);

        adapter = new LocationRecyclerAdapter(getLocations(),getApplicationContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        setupActionBar();
        navigationViewItemPosition = 3;

    }

    public List<Locations> getLocations(){
        Realm realm = AppDelegate.getRealmInstance();
        return realm.where(Locations.class).findAll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationViewItemPosition = 3;
    }
}
