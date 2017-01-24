package com.codemobile.footsqueek.codemobile.interfaces;

import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.database.Session;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;

/**
 * Created by greg on 20/01/2017.
 */

public class GenericMap {

    LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();

    public LinkedHashMap<String, Integer> getMap() {
        return map;
    }

    public void setMap(LinkedHashMap<String, Integer> map) {

        this.map = map;
    }

    public  LinkedHashMap<String, Integer> createMap(List<Session> allSessions){

        //todo filter by room
       // Realm realm = AppDelegate.getRealmInstance();

    //    List <Session> allTalks = realm.where(Session.class).findAllSorted("timeStart");
    //    Session tempTalk = null;

     //   Realm realm = AppDelegate.getRealmInstance();

      //  List <Session> allTalks = realm.where(Session.class).findAllSorted("timeStart");
      //  Session tempTalk = null;

        for (int i = 0; i < allSessions.size(); i++) {
            if(allSessions.get(i).isDoubleRow()){
                map.put("Header",-1);
                map.put("Session",Integer.valueOf(allSessions.get(i).getId()));
                map.put("Session",Integer.valueOf(allSessions.get(i+1).getId()));
                i++;//should skip the next row since it is added in here to avoid two headers
            }else{
                map.put("Session",Integer.valueOf(allSessions.get(i).getId()));
            }
        }

        return map;
    }
}
