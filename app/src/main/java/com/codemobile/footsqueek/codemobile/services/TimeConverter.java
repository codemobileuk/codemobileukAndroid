package com.codemobile.footsqueek.codemobile.services;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by greg on 18/01/2017.
 */

public class TimeConverter {



    public Date convertStringToDate(String dateString){

        Date date;//"2017-04-17T09:45:00"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
      //  sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

        try{
            date = sdf.parse(dateString);
            Log.d("parsingdate",date+"" +"\n" +dateString);
            return date;

        }catch (ParseException e){
            Log.e("parseexception", "parsing date error" + e);
        }

        return null;
    }

    public static String trimTimeFromDate(Date date){
        //todo check date format
        String dateString = date.toString();
        String[] split  = dateString.split(" ");

        return split[3].substring(0,split[3].length()-3);

    }

}
