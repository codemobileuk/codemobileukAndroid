package com.codemobile.footsqueek.codemobile.fetcher;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.TimeConverter;
import com.codemobile.footsqueek.codemobile.database.Location;
import com.codemobile.footsqueek.codemobile.database.RealmUtility;
import com.codemobile.footsqueek.codemobile.database.Session;
import com.codemobile.footsqueek.codemobile.database.Speaker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;

import io.realm.Realm;

/**
 * Created by greg on 17/01/2017.
 */

public class Fetcher extends AsyncTask<String,Void,String>{

    String json = null;
    private static final int SCHEDULE = 0;
    private static final int SPEAKER = 1;
    private static final int LOCATION = 2;

    @Override
    protected String doInBackground(String... params) {


        HttpURLConnection urlConnection = null;
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
            }else if(params[0].equals("Speakers")){
                parseSpeakersJson(json);
            }else if(params[0].equals("Locations")){
                parseLocationJson(json);
            }else{
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

        JSONArray locationsArray = new JSONArray(json);

        for (int i = 0; i < locationsArray.length(); i++) {

            JSONObject locationObject = locationsArray.getJSONObject(i);

            Location location = new Location(
                locationObject.getString(NAME),
                locationObject.getDouble(LON),
                locationObject.getDouble(LAT),
                locationObject.getString(DESC)
            );

            RealmUtility.addNewRow(location);
        }

    }

    private void parseSpeakersJson(String json)throws JSONException{

        final String ID = "speakerId";
        final String FIRSTNAME = "Firstname";
        final String SURNAME = "Surname";
        final String TWITTER = "Twitter";
        final String ORGANISATION = "Organisation";
        final String PROFILE = "Profile";
        final String PHOTO_URL = "PhotoURL";
        final String FULLNAME = "FullName";

        JSONArray speakerArray = new JSONArray(json);
        for (int i = 0; i < speakerArray.length(); i++) {

            JSONObject speakerJSON = speakerArray.getJSONObject(i);

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

            RealmUtility.addNewRow(speaker);

        }

    }

    private void parseSessionJson(String json)throws JSONException{

        TimeConverter timeConverter = new TimeConverter();

        final String RESULTS = "Results";
        final String ID = "SessionId";
        final String TITLE = "SessionTitle";
        final String DESCRIPTION = "SessionDescription";
        final String SPEAKER = "Speaker";
        final String SPEAKER_ID = "speakerId";
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
                speakerJSON.getString(SPEAKER_ID),
                timeConverter.convertStringToDate(sessionJSON.getString(START_TIME)),
                timeConverter.convertStringToDate(sessionJSON.getString(END_TIME)),
                locationJSON.getString(LOCATION_NAME),
                locationJSON.getString(LOCATION_DESCRIPTION),
                false
            );

            Session.addNewRow(session);


        }

    }
}
