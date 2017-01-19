package com.codemobile.footsqueek.codemobile;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by greg on 18/01/2017.
 */

public class TimeConverter {



    public Date convertStringToDate(String dateString){

        Date date;//"2017-04-17T09:45:00"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        try{
            date = sdf.parse(dateString);
            Log.d("parsingdate",date+"");
            return date;

        }catch (ParseException e){
            Log.e("parseexception", "parsing date error" + e);
        }

        return null;
    }

}
