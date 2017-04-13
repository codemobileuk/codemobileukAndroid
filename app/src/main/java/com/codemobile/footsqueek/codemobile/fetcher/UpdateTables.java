package com.codemobile.footsqueek.codemobile.fetcher;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.database.DataBaseVersion;
import com.codemobile.footsqueek.codemobile.database.RealmUtility;
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
    Context context;

    public void setUpdateTablesInterface(UpdateTablesInterface updateTablesInterface){
        this.updateTablesInterface = updateTablesInterface;

    }

    public UpdateTables(Context context) {
        this.context = context;
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void isDataBaseUpToDate(){
        final Realm realm = AppDelegate.getRealmInstance();
        final DataBaseVersion oldDbv = realm.where(DataBaseVersion.class).findFirst();
        final String dbvString;
        if(oldDbv !=null){
            Log.d("Realmstuff", "db version before" + oldDbv.getDbVersion());
            dbvString = oldDbv.getDbVersion();
        }else{
            dbvString = null;
        }

        if(isNetworkAvailable()){
            //do nothing
            fetcher = new Fetcher();
            fetcher.setFetcherInterface(new FetcherInterface() {

                @Override
                public void onComplete() {

                    DataBaseVersion newDbv = realm.where(DataBaseVersion.class).findFirst();
                    if(oldDbv != null){
                        //TODO if realm tables empty go get um
                        if(!dbvString.equals(newDbv.getDbVersion())){

                            updateTablesInterface.onUpdateNeeded(true);
                            RealmUtility.addNewRow(oldDbv);
                        }else{
                            Log.d("fetcher", "DB on latest version ");
                            updateTablesInterface.onUpdateNeeded(false);
                            RealmUtility.addNewRow(oldDbv);
                        }
                    }else{

                        updateTablesInterface.onUpdateNeeded(true);
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
    }

    public void compareAndUpdate(){
        //get old version
        final Realm realm = AppDelegate.getRealmInstance();
        final DataBaseVersion dbv = realm.where(DataBaseVersion.class).findFirst();
        final String dbvString;
        if(dbv !=null){
            Log.d("Realmstuff", "db version before" + dbv.getDbVersion());
            dbvString = dbv.getDbVersion();
        }else{
            dbvString = null;
        }

        if(isNetworkAvailable()){
            //do nothing

            fetcher = new Fetcher();

            fetcher.setFetcherInterface(new FetcherInterface() {

                @Override
                public void onComplete() {

                    DataBaseVersion newDbv = realm.where(DataBaseVersion.class).findFirst();
                     if(dbv != null){
                        //TODO if realm tables empty go get um
                        if(!dbvString.equals(newDbv.getDbVersion())){
                            Log.d("Realmstuff", "TOLD TO GET SPEAKERS SO HERE I GO!==============");
                                fetchSpeakers();

                        }else{
                            Log.d("fetcher", "DB on latest version ");
                            updateTablesInterface.onComplete();

                        }

                    }else{
                         Log.d("Realmstuff", "TOLD TO GET SPEAKERS SO HERE I GO!==============");
                        fetchSpeakers();
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
    }

    private void taskComplete(){

            if(speakersUpdated && scheduleUpdated && tagsUpdated && locationsUpdated) {
                if (updateTablesInterface != null) {
                    updateTablesInterface.onComplete();
                    Log.d("Realmstuff", "READY!==============");
                    fetcher.setDropping(false);
                }

            }
    }



    private void fetchSchedule(){
        Log.d("realmstuff", "fetching schedule inSC");

        final Fetcher fetcher= new Fetcher();
        fetcher.setFetcherInterface(new FetcherInterface() {

            @Override
            public void onComplete() {
                scheduleUpdated = true;
               // taskComplete();
                fetchLocations();
                Log.d("realmstuff", "fetching loc got sched");
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
        Log.d("realmstuff", "fetching speakers");
        final Fetcher fetcher= new Fetcher("Speakers");
        fetcher.setFetcherInterface(new FetcherInterface() {

            @Override
            public void onComplete() {
                speakersUpdated = true;
                //taskComplete();
                Log.d("realmstuff", "fetching schedule");
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
