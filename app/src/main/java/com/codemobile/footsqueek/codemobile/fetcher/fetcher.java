package com.codemobile.footsqueek.codemobile.fetcher;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.database.DataBaseVersion;
import com.codemobile.footsqueek.codemobile.database.Location;
import com.codemobile.footsqueek.codemobile.database.Tag;
import com.codemobile.footsqueek.codemobile.services.TimeConverter;
import com.codemobile.footsqueek.codemobile.database.RealmUtility;
import com.codemobile.footsqueek.codemobile.database.Session;
import com.codemobile.footsqueek.codemobile.database.Speaker;
import com.codemobile.footsqueek.codemobile.interfaces.FetcherInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by greg on 17/01/2017.
 */

public class Fetcher extends AsyncTask<String,Void,String>{

    String json = null;
    private static final int SCHEDULE = 0;
    private static final int SPEAKER = 1;
    private static final int LOCATION = 2;
    private String type = null;

    FetcherInterface fetcherInterface;

    List<Speaker> speakers = new ArrayList<>();
    List<Session> sessions = new ArrayList<>();
    List<Tag> tags = new ArrayList<>();
    List<Location> locations = new ArrayList<>();
    private static List<RealmObject> genericList = new ArrayList<>();

    public void setFetcherInterface(FetcherInterface fetcherInterface){
        this.fetcherInterface = fetcherInterface;
    }

    @Override
    protected String doInBackground(String... params) {

        type = params[0];
        HttpURLConnection urlConnection = null;
        if(!params[0].equals("Modified")){
            Realm realm = AppDelegate.getRealmInstance();

            realm.delete(Session.class);
            realm.delete(Speaker.class);
            realm.delete(Tag.class);
            realm.delete(Location.class);
        }

        BufferedReader reader = null;

        final String BASE_URL = "http://api.app.codemobile.co.uk/api"; //TODO get base URL

        try{

            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath(params[0])
                    .build(); //TODO add anything required here

            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");//TODO presuming
            urlConnection.setRequestProperty("Content-Type","application/json");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if(inputStream == null){
                Log.e("fetcher", "Input stream null");
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null){
                buffer.append(line + "\n");
            }
            if(buffer.length() == 0) {
                return null;
            }
            json = buffer.toString();
            if(params[0].equals("Schedule")){
                parseSessionJson(json);
                return params[0];
            }else if(params[0].equals("Speakers")){
                parseSpeakersJson(json);
                return params[0];
            }else if(params[0].equals("Locations")){
                parseLocationJson(json);
                return params[0];
            }else if(params[0].equals("Tags")){
                parseTagsJson(json);
                return params[0];
            }else if (params[0].equals("Modified")){
                parseUpdatedDbJson(json);
                return params[0];
            }
            else{
                Log.e("fetcher","No valid Json parser");
            }


            Log.d("json",json);

        }catch (IOException e){
            Log.e("fectcher", e+"");
        }catch (JSONException e){
            Log.e("fectcher","Json exception: "+ e);
        }finally
        {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(reader != null){
                try{
                    reader.close();


                }catch (IOException e){
                Log.e("fetcher", "Error closing stream", e);
                }
            }
        }

        return json;
    }

    private void parseLocationJson(String json)throws JSONException{

        final String NAME = "LocationName";
        final String LON = "Longitude";
        final String LAT = "Latitude";
        final String DESC = "Description";
        final String TYPE = "Type";
        final String IMAGE = "Image";

        JSONArray locationsArray = new JSONArray(json);

        for (int i = 0; i < locationsArray.length(); i++) {

            JSONObject locationObject = locationsArray.getJSONObject(i);

            Location location = new Location(
                locationObject.getString(NAME),
                locationObject.getDouble(LON),
                locationObject.getDouble(LAT),
                locationObject.getString(DESC),
                locationObject.getString(IMAGE),
                locationObject.getString(TYPE)
            );

            RealmUtility.addNewRow(location);
        }

    }

    private void parseSpeakersJson(String json)throws JSONException{

        final String ID = "SpeakerId";
        final String FIRSTNAME = "Firstname";
        final String SURNAME = "Surname";
        final String TWITTER = "Twitter";
        final String ORGANISATION = "Organisation";
        final String PROFILE = "Profile";
        final String PHOTO_URL = "PhotoURL";
        final String FULLNAME = "FullName";

        List<String> ids = new ArrayList<>();
        JSONArray speakerArray = new JSONArray(json);
        for (int i = 0; i < speakerArray.length(); i++) {


           
            JSONObject speakerJSON = speakerArray.getJSONObject(i);
            ids.add(speakerJSON.getString(ID));
         //   Log.d("missing", "id size: == " + speakerJSON.getString(ID));
            Speaker speaker = new Speaker(
                    speakerJSON.getString(ID),
                    speakerJSON.getString(FIRSTNAME),
                    speakerJSON.getString(SURNAME),
                    speakerJSON.getString(TWITTER),
                    speakerJSON.getString(ORGANISATION),
                    speakerJSON.getString(PROFILE),
                    speakerJSON.getString(PHOTO_URL),
                    speakerJSON.getString(FULLNAME)

            );
          //  RealmUtility.addNewRow(speaker);
            genericList.add(speaker);

        }

        Realm realm = AppDelegate.getRealmInstance();
        List<Speaker>speakers = realm.where(Speaker.class).findAll();


        for (int j = 0; j <speakers.size() ; j++) {
            //if id's dion't match and its the last one#
            Log.d("miss3", "============" +j+"=================" +j+"======================");
            boolean match =false;
            for (int k = 0; k < ids.size(); k++) {
                if(ids.get(k).equals(speakers.get(j).getId())){
                    Log.d("miss3", "id speakers: " +speakers.get(j).getId() +"== K id= " +ids.get(k));
                   match = true;
                  // break;
                }else if(!ids.get(k).equals(speakers.get(j).getId())){

                    if( k == ids.size()-1){
                        if(match){
                            Log.d("miss3", "matched! not deleting");
                        }else{
                            Log.d("miss3", "delete: " +speakers.get(j).getId());
                        }
                    }
                }

            }
            Log.d("miss3", "===================================================");

        }


    }

    private void parseTagsJson(String json)throws JSONException{

        final String ID = "TagId";
        final String TAG = "Tag";
        final String SessionId = "SessionId";

        JSONArray tagArray = new JSONArray(json);
        for (int i = 0; i < tagArray.length(); i++) {

            JSONObject tagJSON = tagArray.getJSONObject(i);

            Tag tag = new Tag(
                    RealmUtility.generatePrimaryKey(tagJSON.getString(SessionId),tagJSON.getString(ID)),
                    tagJSON.getString(ID),
                    tagJSON.getString(TAG),
                    tagJSON.getString(SessionId)
            );

         //   RealmUtility.addNewRow(tag);
            genericList.add(tag);
        }
    }

    private void parseSessionJson(String json)throws JSONException{

        TimeConverter timeConverter = new TimeConverter();

        final String RESULTS = "Results";
        final String ID = "SessionId";
        final String TITLE = "SessionTitle";
        final String DESCRIPTION = "SessionDescription";
        final String SESSION_TYPE = "SessionType";
        final String SPEAKER = "Speaker";
        final String SPEAKER_ID = "SpeakerId";
        final String START_TIME = "SessionStartDateTime";
        final String END_TIME = "SessionEndDateTime";
        final String SESSION_LOCATION = "SessionLocation";
        final String LOCATION_NAME = "LocationName";
        final String LOCATION_DESCRIPTION = "Description";

        JSONArray sessionListJOSN = new JSONArray(json);


        for (int i = 0; i < sessionListJOSN.length(); i++) {
            JSONObject sessionJSON = sessionListJOSN.getJSONObject(i);
           // JSONObject sessionJSON = resultsArray.getJSONObject(i);
            JSONObject speakerJSON = sessionJSON.getJSONObject(SPEAKER);

            JSONObject locationJSON = sessionJSON.getJSONObject(SESSION_LOCATION);


            Session session = new Session(
                sessionJSON.getString(ID),
                sessionJSON.getString(TITLE),
                sessionJSON.getString(DESCRIPTION),
                sessionJSON.getString(SESSION_TYPE),
                speakerJSON.getString(SPEAKER_ID),
                timeConverter.convertStringToDate(sessionJSON.getString(START_TIME)),
                timeConverter.convertStringToDate(sessionJSON.getString(END_TIME)),
                locationJSON.getString(LOCATION_NAME),
                locationJSON.getString(LOCATION_DESCRIPTION),
                false
            );
          //  Session.addNewRow(session);
            genericList.add(session);
        }

    }

    private void parseUpdatedDbJson(String json)throws JSONException{

        final String ModifiedDate = "ModifiedDate";

        JSONObject tagJSON = new JSONObject(json);
      //  for (int i = 0; i < tagArray.length(); i++) {
            //tagJSON.getJSONObject(ModifiedDate);

            DataBaseVersion dbv = new DataBaseVersion(
                    "1",
                    tagJSON.getString(ModifiedDate)
            );
        Log.d("Realmstuff", "Date: " +tagJSON.getString(ModifiedDate));
            //RealmUtility.addNewRow(dbv);
      //  RealmUtility.addNewRowDelayedCommit(dbv);
        genericList.add(dbv);

    //    }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Log.d("Realmstuff", "on complete + " +s);
     //   fetcherInterface.onComplete();
        RealmUtility.addNewRows(genericList, fetcherInterface);
        genericList = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
}
