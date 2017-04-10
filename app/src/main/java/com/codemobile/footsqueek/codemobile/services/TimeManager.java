package com.codemobile.footsqueek.codemobile.services;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by greg on 18/01/2017.
 */

public class TimeManager {



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

    public static boolean isDateAfterThursdayMorning(){
        //Very specific method for codeMobile

        Date currentDate = new Date();
        currentDate.getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);

        Calendar thursday = Calendar.getInstance();

        thursday.set(Calendar.MONTH, 3);
        thursday.set(Calendar.YEAR, 2017);
        thursday.set(Calendar.DAY_OF_MONTH,20);
        thursday.set(Calendar.HOUR_OF_DAY,10);
        thursday.set(Calendar.MINUTE,0);

        Date thursdayMorn = thursday.getTime();


        // Date monday = calendar.getTime();

        if(currentDate.after(thursdayMorn)){
           return true;
        }
        return false;

    }

    public static boolean isBeforeEndOfThursday(){
        //Very specific method for codeMobile

        Date currentDate = new Date();
        currentDate.getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);

        Calendar thursday = Calendar.getInstance();

        thursday.set(Calendar.MONTH, 3);
        thursday.set(Calendar.YEAR, 2017);
        thursday.set(Calendar.DAY_OF_MONTH,20);
        thursday.set(Calendar.HOUR_OF_DAY,16);
        thursday.set(Calendar.MINUTE,0);

        Date thursdayaft = thursday.getTime();


        // Date monday = calendar.getTime();

        if(currentDate.before(thursdayaft)){
            return true;
        }
        return false;

    }

}
