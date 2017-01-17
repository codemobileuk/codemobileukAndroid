package com.codemobile.footsqueek.codemobile.fetcher;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;

/**
 * Created by greg on 17/01/2017.
 */

public class Fetcher extends AsyncTask<Void,Void,Void>{

    String json = null;

    @Override
    protected Void doInBackground(Void... params) {


        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        final String BASE_URL = "?!?!?!?!?!?!"; //TODO get base URL

        try{

            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .build(); //TODO add anything required here

            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");//TODO presuming
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

        }catch (IOException e){
            Log.e("fectcher", e+"");
        }finally {
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


        return null;
    }
}
