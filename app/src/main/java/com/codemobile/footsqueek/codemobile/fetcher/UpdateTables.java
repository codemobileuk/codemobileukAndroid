package com.codemobile.footsqueek.codemobile.fetcher;

import android.util.Log;

import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.database.DataBaseVersion;
import com.codemobile.footsqueek.codemobile.interfaces.FetcherInterface;
import com.codemobile.footsqueek.codemobile.interfaces.UpdateTablesInterface;

import io.realm.Realm;

/**
 * Created by greg on 28/03/2017.
 */

public class UpdateTables {


    private boolean speakersUpdated = false, scheduleUpdated = false, tagsUpdated = false, locationsUpdated = false;

    UpdateTablesInterface updateTablesInterface;

    public void setUpdateTablesInterface(UpdateTablesInterface updateTablesInterface){
        this.updateTablesInterface = updateTablesInterface;

    }



    public void compareAndUpdate(){
        //get old versio
        final Realm realm = AppDelegate.getRealmInstance();
        final DataBaseVersion dbv = realm.where(DataBaseVersion.class).findFirst();

        final Fetcher fetcher= new Fetcher();
        fetcher.setFetcherInterface(new FetcherInterface() {

            @Override
            public void onComplete() {

                DataBaseVersion newDbv = realm.where(DataBaseVersion.class).findFirst();
                if(dbv != null){
                    if(!dbv.getDbVersion().equals(newDbv.getDbVersion())){
                            fetchSpeakers();
                            fetchSchedule();
                            fetchTags();
                            fetchLocations();
                    }else{
                        Log.d("fetcher", "DB on latest version ");
                        updateTablesInterface.onComplete();
                    }

                }


            }

            @Override
            public void onError() {

            }

            @Override
            public void onProgress() {

            }
        });
        fetcher.execute("Modified");
    }

    private void taskComplete(){

            if(speakersUpdated && scheduleUpdated && tagsUpdated && locationsUpdated) {
                if (updateTablesInterface != null) {
                    updateTablesInterface.onComplete();
                }

            }
    }





    private void fetchSchedule(){

        final Fetcher fetcher= new Fetcher();
        fetcher.setFetcherInterface(new FetcherInterface() {

            @Override
            public void onComplete() {
                scheduleUpdated = true;
                taskComplete();
            }

            @Override
            public void onError() {
                if(updateTablesInterface != null){
                    updateTablesInterface.onError();
                }
            }

            @Override
            public void onProgress() {

            }
        });
        fetcher.execute("Schedule");

    }
    private void fetchSpeakers(){

        final Fetcher fetcher= new Fetcher();
        fetcher.setFetcherInterface(new FetcherInterface() {

            @Override
            public void onComplete() {
                speakersUpdated = true;
                taskComplete();
            }

            @Override
            public void onError() {
                if(updateTablesInterface != null){
                    updateTablesInterface.onError();
                }
            }

            @Override
            public void onProgress() {

            }
        });
        fetcher.execute("Speakers");

    }

    private void fetchLocations(){

        final Fetcher fetcher= new Fetcher();
        fetcher.setFetcherInterface(new FetcherInterface() {

            @Override
            public void onComplete() {
                locationsUpdated = true;
                taskComplete();
            }

            @Override
            public void onError() {
                if(updateTablesInterface != null){
                    updateTablesInterface.onError();
                }
            }

            @Override
            public void onProgress() {

            }
        });
        fetcher.execute("Locations");

    }

    private void fetchTags(){

        final Fetcher fetcher = new Fetcher();
        fetcher.setFetcherInterface(new FetcherInterface() {
            @Override
            public void onComplete() {
                tagsUpdated = true;
                taskComplete();
            }

            @Override
            public void onError() {
                if(updateTablesInterface != null){
                    updateTablesInterface.onError();
                }
            }

            @Override
            public void onProgress() {

            }
        });
        fetcher.execute("Tags");
    }

}
