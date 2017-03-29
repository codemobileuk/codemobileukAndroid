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
    Fetcher fetcher;

    public void setUpdateTablesInterface(UpdateTablesInterface updateTablesInterface){
        this.updateTablesInterface = updateTablesInterface;

    }



    public void compareAndUpdate(){
        //get old versio
        final Realm realm = AppDelegate.getRealmInstance();
        final DataBaseVersion dbv = realm.where(DataBaseVersion.class).findFirst();
        Log.d("Realmstuff", "db version before" + dbv.getDbVersion());
        fetcher = new Fetcher();
        final String dbvString = dbv.getDbVersion();
        fetcher.setFetcherInterface(new FetcherInterface() {

            @Override
            public void onComplete() {

                DataBaseVersion newDbv = realm.where(DataBaseVersion.class).findFirst();
                Log.d("Realmstuff", "db version compare o/n: " + dbv.getDbVersion() + "  " + dbvString+"  " + newDbv.getDbVersion());
                if(dbv != null){
                    //TODO if realm tables empty go get um
                    if(!dbvString.equals(newDbv.getDbVersion())){
                            fetchSpeakers();

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
                    Log.d("Realmstuff", "DB can be dropped again ");
                    fetcher.setDropping(false);
                }

            }
    }





    private void fetchSchedule(){

        final Fetcher fetcher= new Fetcher("Speakers");
        fetcher.setFetcherInterface(new FetcherInterface() {

            @Override
            public void onComplete() {
                scheduleUpdated = true;
               // taskComplete();
                fetchLocations();
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
                //taskComplete();
                fetchSchedule();

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
              //  taskComplete();
                fetchTags();

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
